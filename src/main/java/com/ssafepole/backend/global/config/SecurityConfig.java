package com.ssafepole.backend.global.config;

import static org.springframework.security.config.Customizer.withDefaults;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            .csrf(AbstractHttpConfigurer::disable // CSRF 보호 비활성화 (Swagger UI 접근을 위해)
            )
            .authorizeHttpRequests(authz -> authz
                .requestMatchers("/**").permitAll() // 전체 허용
                .anyRequest().authenticated() // 나머지 요청은 인증 필요
            )
            .httpBasic(withDefaults()); // 기본 인증 사용

        return http.build(); // SecurityFilterChain 반환
    }

}
