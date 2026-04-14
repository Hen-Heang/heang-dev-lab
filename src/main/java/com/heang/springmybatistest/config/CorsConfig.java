package com.heang.springmybatistest.config;


import org.jspecify.annotations.NonNull;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

// @Configuration: 이 클래스가 Spring 설정 클래스임을 알림 — 앱 시작 시 자동으로 로드됨
// @Configuration: Marks this as a Spring config class — loaded automatically on startup
@Configuration
public class CorsConfig {

    // @Bean: 이 메서드의 반환값을 Spring 컨테이너에 등록
    // @Bean: Registers the returned object into the Spring container
    @Bean
    public WebMvcConfigurer corsConfigurer() {
        return new WebMvcConfigurer() {

            // CORS 설정: 브라우저가 다른 출처(origin)의 API를 호출할 수 있도록 허용
            // CORS config: allows the browser to call APIs from a different origin
            @Override
            public void addCorsMappings(@NonNull CorsRegistry registry) {

                // "/**" — 모든 API 엔드포인트에 CORS 규칙 적용
                // "/**" — apply CORS rules to ALL endpoints
                registry.addMapping("/**")
                        .allowedOrigins(
                                "http://localhost:3000",   // React 개발 서버 / React dev server
                                "http://127.0.0.1:5500",  // VS Code Live Server
                                "http://localhost:8080",   // 같은 서버 (Swagger, JSP) / Same server
                                "file://"                  // 로컬 HTML 파일 직접 열었을 때 / Local HTML file
                        )
                        // 허용할 HTTP 메서드 / Allowed HTTP methods
                        .allowedMethods("GET", "POST", "PUT", "DELETE")
                        // 모든 요청 헤더 허용 / Allow any request header
                        .allowedHeaders("*")
                        // 쿠키/인증 정보를 다른 출처로 보내지 않음 / Do not send cookies across origins
                        .allowCredentials(false);
            }

            // 정적 리소스 경로 매핑 (CORS와 별개)
            // Static resource mapping (separate from CORS)
            @Override
            public void addResourceHandlers(@NonNull ResourceHandlerRegistry registry) {
                // URL /css/** 요청 → classpath:/css/ 폴더에서 파일 제공
                // Requests to /css/** → serve files from resources/css/
                registry.addResourceHandler("/css/**")
                        .addResourceLocations("classpath:/css/");
            }
        };
    }
}
