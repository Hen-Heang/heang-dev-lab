# saas-olv Deployment Guide
## 배포 도구 완전 이해 가이드 (Deployment Tools Complete Guide)

> **대상 (Audience):** Java 개발자 입문자 — 배포를 처음 배우는 사람
> **목적 (Purpose):** saas-olv 프로젝트에서 사용하는 모든 배포 도구를 쉽게 이해하기
> **기반 파일 (Source files):** `Jenkinsfile`, `Dockerfile-was`, `Dockerfile-web-dev`,
> `Dockerfile-web-prd`, `Jenkins-k8s-develop.yaml`, `Jenkins-k8s-main.yaml`,
> `build.gradle`, `settings.gradle`

---

## 1. 왜 배포가 복잡한가? (Why Is Deployment Complex?)

이 프로젝트는 단순한 "하나의 앱"이 아닙니다.

```
4개의 Java 모듈 (4 Java Modules)
├── olv-core   → 공유 라이브러리 (shared code — used by all modules)
├── olv-api    → REST API 서버  → 빌드 결과: apilunch.war
├── olv-oper   → 관리자 웹      → 빌드 결과: adlunch.war
└── olv-pfom   → 사용자 포털   → 빌드 결과: lunch.war

2개의 환경 (2 Environments)
├── DEV  (develop 브랜치) → 개발/테스트용
└── PROD (main 브랜치)    → 실제 사용자용

2가지 서버 타입 (2 Server Types)
├── WAS (Web Application Server) → Java WAR 파일 실행 (Tomcat)
└── WEB (Nginx)                  → CSS/JS/이미지 등 정적 파일 서빙
```

수동으로 하면:
1. 개발자 PC에서 각 모듈 빌드
2. WAR 파일을 서버로 복사
3. Tomcat 재시작
4. Kubernetes 업데이트
5. 실수 가능성 높음, 시간 낭비

**→ 배포 도구가 이 모든 것을 자동화합니다.**

---

## 2. 전체 흐름 한눈에 보기 (Big Picture)

```
개발자 git push
        │
        ▼
Jenkins (Jenkinsfile) ── 전체 파이프라인 조율
        │
        ├─ 1. 파라미터 검증 (validate params)
        │
        ├─ 2. 베이스 이미지 사전 다운로드 (warm up)
        │
        ├─ 3. 설정 파일 가져오기 (pull config) ← 별도 private git repo
        │
        ├─ 4. Gradle 빌드 → .war 파일 생성 (build)
        │         └── olv-oper/build/libs/adlunch.war
        │         └── olv-pfom/build/libs/lunch.war
        │         └── olv-api/build/libs/apilunch.war
        │
        ├─ 5. Kaniko로 Docker 이미지 빌드 (image) ← 병렬 실행
        │         └── Dockerfile-was + .war → 이미지 생성
        │         └── NCP Private Registry에 push
        │
        └─ 6. kubectl로 Kubernetes 배포 (deploy) ← 병렬 실행
                  └── kubectl set image → rolling update → 무중단 배포
```

---

## 3. 도구별 상세 설명 (Tool-by-Tool Explanation)

---

### Tool 1 — Gradle (빌드 도구 / Build Tool)

**관련 파일:** `build.gradle`, `settings.gradle`, `gradlew`, `gradlew.bat`

#### Gradle이란?
Java 소스코드(.java 파일들)를 서버에서 실행할 수 있는 파일(.war)로 변환해주는 공장.

```
Java 소스코드 (.java)
        ↓  Gradle
배포 가능한 파일 (.war) ← 서버에 올라가는 파일
```

#### 이 프로젝트에서 왜 Gradle인가?
4개 모듈이 서로 의존 관계를 가지기 때문. Gradle이 올바른 빌드 순서를 자동으로 관리함.

```groovy
// settings.gradle — 모듈 구조 정의
rootProject.name = 'olv-root'
include 'olv-core'    // 1번 먼저 빌드 (다른 모듈이 사용)
include 'olv-pfom'    // olv-core 의존
include 'olv-oper'    // olv-core 의존
include 'olv-api'     // olv-core 의존
```

#### Nexus 설정 (내부 Maven 저장소)
```groovy
// build.gradle
gradle.ext.IS_NEXUS_MODE = true
gradle.ext.NEXUS_URL = 'http://new-nexus-svc.nexus.svc:8081/repository/maven-public/'
```

**왜 Nexus를 사용하나?**
이 프로젝트는 정부 클라우드(NCP Gov Cloud) 환경에서 실행됨 → 인터넷 접근 불가.
Nexus = 회사 내부에 있는 Maven Central 미러. Spring Boot, MyBatis 등 모든 라이브러리를
Nexus에서 다운로드.

```
인터넷 없을 때:
  Gradle → Nexus (사내 내부망) → Spring Boot 3.3.5 다운로드 ✅

인터넷 직접 접근:
  Gradle → Maven Central (정부망에서 불가 ❌)
```

#### gradlew (Gradle Wrapper)란?
PC마다 Gradle 버전이 달라서 생기는 "내 PC에서는 됐는데..." 문제를 해결.

```
gradlew → "항상 Gradle 8.12 버전만 써라"고 강제함
→ 개발자 PC, Jenkins 서버 어디서나 동일한 버전으로 빌드
```

#### Jenkins에서 실행하는 핵심 명령어
```bash
# Jenkinsfile stage('build') 에서 실행되는 명령어
gradle clean :olv-oper:build :olv-pfom:build :olv-api:build

# clean             = 이전 빌드 파일 삭제
# :olv-oper:build   = olv-oper 모듈만 빌드 (선택한 모듈만)
# 결과:
#   olv-oper/build/libs/adlunch.war
#   olv-pfom/build/libs/lunch.war
#   olv-api/build/libs/apilunch.war
```

#### 각 모듈의 출력 파일
| 모듈 | 출력 파일 | 역할 |
|---|---|---|
| `olv-core` | `olv-core.jar` | 공유 라이브러리 (직접 배포 안 함) |
| `olv-api` | `apilunch.war` | REST API 서버 |
| `olv-oper` | `adlunch.war` | 관리자 웹 |
| `olv-pfom` | `lunch.war` | 사용자 포털 |

---

### Tool 2 — Docker (컨테이너 / Container)

**관련 파일:** `Dockerfile-was`, `Dockerfile-web-dev`, `Dockerfile-web-prd`

#### Docker란?
"내 PC에서는 됐는데 서버에서 안 돼요" 문제를 해결하는 기술.

**문제 (Docker 없을 때):**
```
개발자 PC:   Java 21, Tomcat 10 → 앱 잘 됨
테스트 서버: Java 17, Tomcat 9  → 앱 오류
운영 서버:   Java 11            → 완전히 다른 오류
```

**해결 (Docker 사용):**
```
Docker 이미지 = WAR 파일 + Java 21 + Tomcat 10 + 설정 파일을 하나로 묶은 패키지
→ 어느 서버에서 실행해도 동일하게 동작
```

- **이미지(Image):** 앱의 "설계도" — 실행 전 상태의 패키지
- **컨테이너(Container):** 이미지를 실행한 인스턴스 — 실제로 돌아가는 프로세스

---

#### Dockerfile-was — Java 앱 서버용

```dockerfile
ARG BASE_IMAGE
FROM ${BASE_IMAGE}                   # Tomcat 서버 베이스 이미지로 시작

WORKDIR /usr/local/tomcat/webapps   # Tomcat이 WAR를 찾는 폴더로 이동

ARG COPY_TARGET
ARG APP_UID=1000
ARG APP_GID=1000

# ksbiz 보안 라이브러리 복사 (한국 공인인증서 전자서명 SDK — 정부 요구사항)
COPY --chown=${APP_UID}:${APP_GID} ./ksbiz/*.jar              /usr/local/tomcat/lib/
COPY --chown=${APP_UID}:${APP_GID} ./ksbiz/ksbiz.conf         /usr/local/tomcat/conf/ksbiz.conf
COPY --chown=${APP_UID}:${APP_GID} ./ksbiz/ksbiz_license.rsl  /usr/local/tomcat/ksbiz2/license/

# 빌드된 WAR 파일을 Tomcat 배포 폴더에 복사
COPY --chown=${APP_UID}:${APP_GID} ./${COPY_TARGET} .

USER ${APP_UID}    # 보안: root가 아닌 일반 사용자로 실행
```

**이 파일이 `ARG`(파라미터)로 되어있는 이유:**
olv-api, olv-oper, olv-pfom 세 모듈이 이 하나의 파일을 공유함.
Kaniko 실행 시 `--build-arg`로 각각 다른 값을 주입.

```bash
# olv-oper 이미지 빌드 시
--build-arg COPY_TARGET="olv-oper/build/libs/adlunch.war"

# olv-pfom 이미지 빌드 시
--build-arg COPY_TARGET="olv-pfom/build/libs/lunch.war"
```

---

#### Dockerfile-web-dev — 정적 파일 서버 (개발용)

```dockerfile
ARG BASE_IMAGE
FROM ${BASE_IMAGE}    # Nginx 베이스 이미지 (정적 파일 서빙 전용 서버)

ARG PFOM_DIR
ARG OPER_DIR

# 각 모듈의 CSS/JS/이미지 파일을 Nginx 서빙 폴더로 복사
COPY --chown=1000:1000 ./olv-pfom/src/main/resources/static /app/web/${PFOM_DIR}/lunch/static
COPY --chown=1000:1000 ./olv-oper/src/main/resources/static /app/web/${OPER_DIR}/adlunch/static
```

**왜 Nginx 컨테이너를 따로 사용하나?**
```
Tomcat: Java 코드 실행에 특화 → 정적 파일 서빙은 상대적으로 느림
Nginx:  정적 파일 서빙 전용 C 서버 → Tomcat 대비 10배 빠름

→ CSS/JS/이미지  → Nginx 컨테이너 (빠름)
   API 요청      → Tomcat 컨테이너 (Java 처리)
```

---

#### Dockerfile-web-prd — 정적 파일 서버 (운영용)

개발용과 동일하지만 **UID/GID가 65532** (운영 보안 요구사항):

```dockerfile
COPY --chown=65532:65532 ./olv-pfom/src/main/resources/static ...
COPY --chown=65532:65532 ./olv-oper/src/main/resources/static ...
```

#### Jenkinsfile에서 어떻게 선택하나?
```groovy
def webDockerfile = env.BRANCH_NAME == 'main' ? 'Dockerfile-web-prd' : 'Dockerfile-web-dev'
//                    main 브랜치(운영) → prd 파일
//                    develop 브랜치(개발) → dev 파일
```

#### Docker 이미지 레이어 개념 (Layer)
```
FROM tomcat            ← 레이어 1 (캐시됨 — 거의 변경 없음)
COPY ksbiz/*.jar       ← 레이어 2 (캐시됨 — 드물게 변경)
COPY adlunch.war       ← 레이어 3 (매 빌드마다 변경)

→ Docker는 변경된 레이어만 다시 빌드 = 빌드 속도 향상
```

---

### Tool 3 — Kaniko (이미지 빌더 / Image Builder)

**참조 위치:** `Jenkinsfile` stage('[image]'), `Jenkins-k8s-develop.yaml`

#### Kaniko란?
Kubernetes 안에서 Docker 이미지를 빌드하는 도구.

**왜 `docker build` 대신 Kaniko인가?**
```
docker build 사용 시:
  → Docker 데몬(백그라운드 서비스) 필요
  → Docker 데몬은 호스트 서버의 root 권한 필요
  → Jenkins 컨테이너에 root 권한 = 보안 위험 ❌

Kaniko 사용 시:
  → Docker 데몬 없이 이미지 빌드 가능
  → 일반 Kubernetes 컨테이너 안에서 실행
  → 호스트 서버 접근 불필요 = 보안 안전 ✅
```

#### Jenkinsfile에서의 실제 사용 예시 (wasOper)
```groovy
stage('wasOper') {
    when {
        expression { params.WAS_OPER }   // 체크박스 선택 시에만 실행
    }
    steps {
        container('kaniko-c2') {
            sh """
                /kaniko/executor \
                --dockerfile Dockerfile-was \
                --context `pwd` \
                --cache=true \
                --build-arg BASE_IMAGE="${pcrEndPoint}/${baseWasOperImage}:latest" \
                --build-arg COPY_TARGET="olv-oper/build/libs/adlunch.war" \
                --build-arg APP_UID="1000" \
                --build-arg APP_GID="1000" \
                --destination "${pcrEndPoint}/olv-oper:develop-abc1234" \
                --destination "${pcrEndPoint}/olv-oper:latest"
            """
        }
    }
}
```

#### 이미지 태그 네이밍 규칙
```groovy
// Jenkinsfile
ciRefTag = env.BRANCH_NAME + "-" + sh(script: "printf \$(git rev-parse --short HEAD)", returnStdout: true)
// 결과 예시: develop-abc1234
//            main-f7e2c9a
```

| 태그 | 예시 | 의미 |
|---|---|---|
| `develop-abc1234` | 브랜치명 + 커밋 해시 | 특정 버전 고정 (롤백 시 사용) |
| `latest` | latest | 항상 최신 버전을 가리킴 |

#### 3개의 Kaniko 컨테이너를 쓰는 이유
```
kaniko-c0 → olv-api 이미지 빌드   ┐
kaniko-c1 → olv-web 이미지 빌드   ├ 동시에 병렬 실행!
kaniko-c2 → olv-oper 이미지 빌드  ┘

순차 실행: 3 + 2 + 3 + 3 = 11분
병렬 실행: max(3, 2, 3, 3) = 3분
```

#### Warm Up 단계가 있는 이유
```groovy
stage('warm up') {
    steps {
        container('kaniko-warmer') {
            sh """
                /kaniko/warmer \
                --image=${pcrEndPoint}/${baseWebImage}:latest \
                --image=${pcrEndPoint}/${baseWasApiImage}:latest \
                --image=${pcrEndPoint}/${baseWasOperImage}:latest \
                --image=${pcrEndPoint}/${baseWasPfomImage}:latest
            """
        }
    }
}
```

Kaniko 빌드 전에 베이스 이미지를 미리 `/cache`에 다운로드.
다음 빌드 시 이미 있으므로 속도 향상.
마치 요리 전에 재료를 미리 꺼내두는 것과 같음.

---

### Tool 4 — NCP Container Registry (이미지 저장소)

**참조 위치:** `Jenkinsfile` 상단 변수 선언

#### Container Registry란?
Docker 이미지를 저장하고 배포하는 저장소.
코드를 저장하는 GitHub와 같은 개념 — 단 Docker 이미지용.

#### 이 프로젝트의 두 Registry
```groovy
// Jenkinsfile
def pcrEndPoint = env.BRANCH_NAME == 'main'
    ? 'avobxiry.private-ncr.gov-ntruss.com'   // 운영(PROD) Registry
    : 'pqf1vv9m.private-ncr.gov-ntruss.com'   // 개발(DEV) Registry
```

| 환경 | Registry | 사용 브랜치 |
|---|---|---|
| DEV | `pqf1vv9m.private-ncr.gov-ntruss.com` | develop |
| PROD | `avobxiry.private-ncr.gov-ntruss.com` | main |

**왜 두 개로 분리하나?**
- 운영/개발 이미지가 섞이지 않도록
- 정부 클라우드(NCP Gov Cloud) 보안 요구사항
- 운영 파이프라인은 반드시 운영 Registry 이미지만 사용

**NCP Private = 비공개 이유:**
```
이 앱은 정부기관 점심 복지 결제를 처리
→ ksbiz 인증서 라이브러리 포함
→ 절대 공개 Docker Hub에 올리면 안 됨
→ NCP 정부 전용 클라우드의 Private Registry 사용
```

#### Registry 인증 방법
```yaml
# Jenkins-k8s-develop.yaml
imagePullSecrets:
  - name: olv-dev-reg-secret        # K8s Secret에 Registry 로그인 정보 저장

volumes:
  - name: jenkins-pcr
    projected:
      sources:
        - secret:
            name: olv-dev-reg-secret
            items:
              - key: .dockerconfigjson
                path: config.json   # Kaniko가 이 파일로 Registry에 로그인
```

---

### Tool 5 — Kubernetes (컨테이너 관리 / Container Orchestration)

**관련 파일:** `Jenkins-k8s-develop.yaml`, `Jenkins-k8s-main.yaml`
**참조 위치:** `Jenkinsfile` stage('[deploy]')

#### Kubernetes란?
Docker 컨테이너를 자동으로 관리하는 시스템.

**Docker만 있을 때의 문제:**
```
❌ 컨테이너가 죽으면?    → 아무도 재시작 안 함
❌ 서버가 다운되면?      → 서비스 중단
❌ 새 버전 배포 시?      → 서비스 중단
```

**Kubernetes가 해결:**
```
✅ 컨테이너 죽으면 → 자동 재시작
✅ 서버 다운      → 다른 서버로 자동 이동
✅ 새 버전 배포   → Rolling Update (무중단 배포)
```

#### 이 프로젝트의 Namespace 구조
```
dev-olv-was  → 개발 환경 WAS 컨테이너 (olv-api, olv-oper, olv-pfom)
dev-olv-web  → 개발 환경 WEB 컨테이너 (Nginx)
prd-olv-was  → 운영 환경 WAS 컨테이너
prd-olv-web  → 운영 환경 WEB 컨테이너
```

Namespace = 논리적 분리 공간. 개발과 운영이 같은 클러스터에 있지만 서로 격리됨.

#### StatefulSet이란?
```groovy
// Jenkinsfile
def stsApi  = env.BRANCH_NAME == 'main' ? 'prd-olv-api'  : 'dev-olv-api'
def stsPfom = env.BRANCH_NAME == 'main' ? 'prd-olv-pfom' : 'dev-olv-pfom'
def stsOper = env.BRANCH_NAME == 'main' ? 'prd-olv-oper' : 'dev-olv-oper'
def stsWeb  = env.BRANCH_NAME == 'main' ? 'prd-olv-web'  : 'dev-olv-web'
```

StatefulSet의 특징:
- 파드 이름이 고정: `dev-olv-oper-0` (숫자 고정)
- 재시작해도 같은 이름 유지
- 안정적인 네트워크 ID가 필요한 앱에 사용

#### Rolling Update — 무중단 배포 흐름

Jenkins가 실행하는 명령어:
```bash
kubectl set image statefulset/dev-olv-oper \
  dev-olv-oper=pqf1vv9m.private-ncr.gov-ntruss.com/olv-oper:develop-abc1234 \
  -n dev-olv-was
```

이 명령어 이후 Kubernetes가 자동으로:
```
1. 새 이미지(develop-abc1234) 다운로드
        ↓
2. 새 파드 시작 (dev-olv-oper-0 NEW)
        ↓
3. Health Check 통과 확인
        ↓
4. 트래픽을 새 파드로 전환
        ↓
5. 기존 파드 종료 (dev-olv-oper-0 OLD)
        ↓
✅ 사용자는 배포 중에도 서비스 이용 가능 (무중단)
```

#### Persistent Volume (PVC) — 빌드 캐시 보존
```yaml
# Jenkins-k8s-develop.yaml
volumes:
  - name: volume-gradle
    persistentVolumeClaim:
      claimName: maven-pv-claim    # Gradle 라이브러리 캐시

  - name: volume-kaniko
    persistentVolumeClaim:
      claimName: kaniko-pv-claim   # Docker 레이어 캐시
```

Jenkins 빌드 파드는 빌드 후 삭제되지만, PVC에 저장된 캐시는 남아있어
다음 빌드 시 재사용 → 빌드 속도 향상.

---

### Tool 6 — Jenkins (CI/CD 자동화)

**관련 파일:** `Jenkinsfile`

#### Jenkins란?
코드 push → 자동으로 빌드/이미지/배포까지 전부 실행해주는 자동화 서버.

- **CI (Continuous Integration):** 코드 병합 → 자동 빌드
- **CD (Continuous Deployment):** 자동 빌드 → 자동 배포

#### Jenkins Kubernetes 에이전트
```yaml
# Jenkins-k8s-develop.yaml — 빌드 시 생성되는 임시 파드
containers:
  - name: gradle         # Java 빌드용
    image: gradle:8.12-jdk21

  - name: kaniko-warmer  # 베이스 이미지 사전 다운로드용
    image: .../custom-kaniko-warmer:1.0

  - name: kaniko-c0      # 이미지 빌더 1번
  - name: kaniko-c1      # 이미지 빌더 2번
  - name: kaniko-c2      # 이미지 빌더 3번
    image: gcr.io/kaniko-project/executor:debug

  - name: kubectl        # Kubernetes 배포 명령용
    image: .../kubectl:1.34.1
```

모든 컨테이너는 `/workspace` 볼륨을 공유:
```
gradle 컨테이너   → WAR 파일을 /workspace에 씀
kaniko 컨테이너   → /workspace에서 WAR 파일 읽어서 이미지 빌드
kubectl 컨테이너  → 빌드된 이미지 태그로 K8s 배포 명령 실행
```

#### Jenkinsfile 파라미터 구조
```groovy
parameters {
    booleanParam(name: 'API',      defaultValue: false, description: 'deploy was-olv-api')
    booleanParam(name: 'WEB',      defaultValue: false, description: 'deploy olv-web (static)')
    booleanParam(name: 'WAS_PFOM', defaultValue: false, description: 'deploy was-olv-pfom')
    booleanParam(name: 'WAS_OPER', defaultValue: false, description: 'deploy was-olv-oper')
}
```

Jenkins 실행 시 4개 체크박스가 보임.
olv-oper만 수정했으면 WAS_OPER만 체크 → 나머지 3개는 빌드 안 함 → 시간 절약.

#### 브랜치별 자동 환경 분기
```groovy
// Jenkinsfile 상단 — 브랜치에 따라 모든 설정이 자동 변경
def pcrEndPoint   = env.BRANCH_NAME == 'main' ? 'avobxiry...' : 'pqf1vv9m...'
def agentYaml     = env.BRANCH_NAME == 'main' ? 'Jenkins-k8s-main.yaml' : 'Jenkins-k8s-develop.yaml'
def wasNs         = env.BRANCH_NAME == 'main' ? 'prd-olv-was' : 'dev-olv-was'
def webDockerfile = env.BRANCH_NAME == 'main' ? 'Dockerfile-web-prd' : 'Dockerfile-web-dev'
```

| 브랜치 | Registry | Namespace | Dockerfile |
|---|---|---|---|
| `develop` | DEV Registry | dev-olv-was | Dockerfile-web-dev |
| `main` | PROD Registry | prd-olv-was | Dockerfile-web-prd |

#### Config Repository 분리 (보안)
```groovy
stage('pull config') {
    steps {
        script {
            dir('config-repo') {
                git branch: configBranch,
                    credentialsId: configCredentials,
                    url: configRepo    // 별도 private git repo
            }
            if (params.WAS_OPER) {
                sh "cp config-repo/olv-oper/application.yml olv-oper/src/main/resources/"
            }
        }
    }
}
```

`application.yml`(DB 비밀번호, API 키 등)은 **별도 private git 저장소**에 보관.
이 코드 저장소에는 없음 → 실수로 GitHub에 비밀번호가 올라가는 사고 방지.

```
config 저장소 구조:
config-repo/
    ├── develop/
    │   ├── olv-api/application.yml    (DEV DB URL, DEV 비밀번호)
    │   ├── olv-oper/application.yml
    │   └── olv-pfom/application.yml
    └── main/
        ├── olv-api/application.yml    (PROD DB URL, PROD 비밀번호)
        ├── olv-oper/application.yml
        └── olv-pfom/application.yml
```

#### 병렬 실행 (parallel)
```groovy
stage('[image]') {
    parallel {
        stage('api')     { ... }   // 동시 실행
        stage('web')     { ... }   // 동시 실행
        stage('wasPfom') { ... }   // 동시 실행
        stage('wasOper') { ... }   // 동시 실행
    }
}
```

```
순차 실행: 3분 + 2분 + 3분 + 3분 = 11분
병렬 실행: max(3, 2, 3, 3)       =  3분
```

#### disableConcurrentBuilds() — 동시 빌드 방지
```groovy
options {
    disableConcurrentBuilds()
    buildDiscarder(logRotator(numToKeepStr: '10'))
}
```

빌드 중인데 다른 사람이 또 빌드 시작하면 충돌 → 이 옵션으로 방지.
최근 10개 빌드 기록만 보관 (디스크 절약).

---

## 4. 전체 배포 흐름 — 실제 시나리오

**시나리오:** 개발자가 olv-oper 코드를 수정하고 develop 브랜치에 push

```
Step 1  git push origin develop
        ↓
Step 2  Jenkins 감지 → Jenkinsfile 실행 시작
        ↓
Step 3  K8s에 임시 빌드 파드 생성 (Jenkins-k8s-develop.yaml 기반)
        컨테이너 6개: gradle, kaniko-warmer, kaniko-c0, c1, c2, kubectl
        ↓
Step 4  [validate params]
        WAS_OPER 체크됨 → 계속 진행
        ciRefTag = "develop-f7e2c9a"
        ↓
Step 5  [warm up]
        kaniko-warmer → /cache에 Tomcat 베이스 이미지 사전 다운로드
        ↓
Step 6  [pull config]
        config-repo clone → olv-oper/application.yml 복사 (DEV DB 접속 정보)
        ↓
Step 7  [build]
        gradle 컨테이너 → gradle clean :olv-oper:build
        결과: olv-oper/build/libs/adlunch.war 생성
        ↓
Step 8  [image] kaniko-c2 컨테이너
        /kaniko/executor \
          --dockerfile Dockerfile-was \
          --build-arg COPY_TARGET=olv-oper/build/libs/adlunch.war \
          --destination DEV_REGISTRY/olv-oper:develop-f7e2c9a \
          --destination DEV_REGISTRY/olv-oper:latest

        이미지 구성:
          [dev-olv-was-base:latest] ← Tomcat + Java 21
          + ksbiz/*.jar              ← 전자서명 SDK
          + adlunch.war              ← 빌드된 WAR 파일

        완료 → DEV Registry에 push
        ↓
Step 9  [deploy] kubectl 컨테이너
        kubectl set image statefulset/dev-olv-oper \
          dev-olv-oper=DEV_REGISTRY/olv-oper:develop-f7e2c9a \
          -n dev-olv-was
        ↓
Step 10 Kubernetes Rolling Update
        새 파드 시작 → Health Check → 트래픽 전환 → 구 파드 종료
        ↓
Step 11 임시 빌드 파드 삭제

✅ 완료! 개발 서버에 새 코드 배포됨 (무중단)
   총 소요 시간: 약 8~12분
```

---

## 5. 도구 요약 테이블 (Tool Summary Table)

| 도구 | 파일 | 역할 | 왜 이 도구인가 |
|---|---|---|---|
| **Gradle** | `build.gradle`, `gradlew` | Java 소스 → WAR 파일 | 멀티 모듈 프로젝트, 의존성 순서 관리 |
| **Nexus** | `build.gradle` 내 URL | Maven 라이브러리 저장소 | 정부망 인터넷 불가 → 사내 미러 필요 |
| **Docker** | `Dockerfile-was`, `Dockerfile-web-*` | 앱 + 서버 환경 패키징 | 어느 서버에서나 동일하게 실행 |
| **Kaniko** | `Jenkinsfile` stage('[image]') | K8s 안에서 이미지 빌드 | Docker 데몬 없이 안전하게 빌드 |
| **NCP Registry** | `Jenkinsfile` 변수 | Docker 이미지 저장소 | 비공개, 한국 정부 클라우드 |
| **Kubernetes** | `Jenkins-k8s-*.yaml` | 컨테이너 관리, 무중단 배포 | 자동 재시작, Rolling Update |
| **Jenkins** | `Jenkinsfile` | 전체 CI/CD 파이프라인 자동화 | push → 자동 빌드/배포 |

---

## 6. 디버깅할 때 알아야 할 명령어 (Debugging Commands)

배포 후 문제가 생겼을 때 사용하는 kubectl 명령어:

```bash
# 파드 상태 확인
kubectl get pods -n dev-olv-was
kubectl get pods -n dev-olv-web

# 파드 상세 정보 (이미지 버전, 이벤트, 오류 확인)
kubectl describe pod dev-olv-oper-0 -n dev-olv-was

# 실시간 로그 보기
kubectl logs -f dev-olv-oper-0 -n dev-olv-was

# 이전 (크래시된) 파드의 로그
kubectl logs dev-olv-oper-0 -n dev-olv-was --previous

# 파드 안으로 접속 (내부 디버깅)
kubectl exec -it dev-olv-oper-0 -n dev-olv-was -- bash

# 현재 배포된 이미지 버전 확인
kubectl get statefulset dev-olv-oper -n dev-olv-was \
  -o jsonpath='{.spec.template.spec.containers[*].image}'

# 롤백 — 이전 버전으로 되돌리기
kubectl set image statefulset/dev-olv-oper \
  dev-olv-oper=pqf1vv9m.private-ncr.gov-ntruss.com/olv-oper:develop-이전커밋해시 \
  -n dev-olv-was
```

---

## 7. 학습 순서 권장 (Recommended Learning Order)

이 프로젝트의 배포를 완전히 이해하기 위한 순서:

| 순서 | 학습 주제 | 이 프로젝트와의 연결 |
|---|---|---|
| 1 | **Git** — branch, commit hash | `ciRefTag = branch + commit hash` |
| 2 | **Linux CLI** — sh 명령어 이해 | `Jenkinsfile` 안의 모든 `sh "..."` |
| 3 | **Gradle** — 빌드, 멀티모듈 | `build.gradle`, `settings.gradle` |
| 4 | **Docker** — 이미지, 컨테이너, Dockerfile | `Dockerfile-was`, `Dockerfile-web-*` |
| 5 | **Kubernetes** — 파드, StatefulSet, kubectl | `Jenkins-k8s-*.yaml`, deploy stage |
| 6 | **Jenkins** — 파이프라인 전체 읽기 | `Jenkinsfile` 처음부터 끝까지 |
| 7 | **이 파일 다시 읽기** | 전체 흐름이 자연스럽게 이해됨 |

---

*이 문서는 `C:\saas project\saas-olv` 프로젝트의 실제 파일을 기반으로 작성되었습니다.*
*`Jenkinsfile`, `Dockerfile-*`, `Jenkins-k8s-*.yaml`, `build.gradle`, `settings.gradle`*
