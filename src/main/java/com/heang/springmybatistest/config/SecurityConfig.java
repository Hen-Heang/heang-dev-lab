package com.heang.springmybatistest.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

/**
 * SecurityConfig — Spring Security configuration (보안 설정)
 * <p>
 * Korean enterprise pattern:
 *   - Session-based login (not JWT) for admin web pages
 *   - Custom login page at /login
 *   - REST APIs (/api/**) permitted without login — they use JWT separately
 *   - MVC pages require authentication
 *
 * @EnableMethodSecurity → enables @PreAuthorize on controller methods
 */
@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfig {

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
            // ── URL access rules (접근 권한 설정) ─────────────────────────
            .authorizeHttpRequests(auth -> auth
                // Static resources — no login needed (정적 리소스)
                .requestMatchers("/css/**", "/js/**", "/images/**", "/uploads/**", "/webjars/**").permitAll()
                // Login page — must be open (로그인 페이지)
                .requestMatchers("/login").permitAll()
                // Swagger — open for dev (개발용 API 문서)
                .requestMatchers("/swagger-ui/**", "/swagger-ui.html", "/v3/api-docs/**").permitAll()
                // REST APIs — permitted so existing AJAX calls keep working (기존 REST API)
                // In a real project these would have their own JWT filter chain
                .requestMatchers("/api/**", "/users/**", "/board/api/**").permitAll()
                // Everything else requires login (나머지는 로그인 필요)
                .anyRequest().authenticated()
            )

            // ── Login form (로그인 폼 설정) ────────────────────────────────
            .formLogin(form -> form
                .loginPage("/login")                    // GET /login → show our page
                .loginProcessingUrl("/login")           // POST /login → Security handles it
                .usernameParameter("username")          // <input name="username">
                .passwordParameter("password")          // <input name="password">
                .defaultSuccessUrl("/dashboard", true)  // success → /dashboard
                .failureUrl("/login?error")             // fail → /login?error
                .permitAll()
            )

            // ── Logout (로그아웃 설정) ─────────────────────────────────────
            .logout(logout -> logout
                .logoutUrl("/logout")                   // POST /logout
                .logoutSuccessUrl("/login?logout")      // after logout → login page
                .invalidateHttpSession(true)            // destroy session (세션 삭제)
                .clearAuthentication(true)
                .permitAll()
            )

            // ── CSRF ───────────────────────────────────────────────────────
            // Disable CSRF for REST API paths (Thymeleaf forms get CSRF automatically)
            // th:action in Thymeleaf forms automatically injects the CSRF hidden field
            .csrf(csrf -> csrf
                .ignoringRequestMatchers("/api/**", "/users/**")
            );

        return http.build();
    }

    /**
     * BCryptPasswordEncoder — used to verify login password against DB hash
     * The DB already stores BCrypt hashes (password = "password123" for all test users)
     *
     * @Bean → Spring injects this wherever PasswordEncoder is needed
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
