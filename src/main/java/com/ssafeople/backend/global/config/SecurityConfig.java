package com.ssafeople.backend.global.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ssafeople.backend.domain.auth.security.JwtAuthenticationFilter;
import com.ssafeople.backend.domain.auth.security.JwtFilter;
import com.ssafeople.backend.domain.auth.security.JwtUtil;
import com.ssafeople.backend.global.exception.ExceptionFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.filter.OncePerRequestFilter;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final AuthenticationConfiguration authenticationConfiguration;
    private final JwtUtil jwtUtil;
    private final ObjectMapper objectMapper;
    private final OncePerRequestFilter jwtFilter;

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager(
            AuthenticationConfiguration configuration) throws Exception {
        return configuration.getAuthenticationManager();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.csrf(AbstractHttpConfigurer::disable)
                .formLogin(AbstractHttpConfigurer::disable)
                .httpBasic(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests((auth) -> auth
                        .requestMatchers(HttpMethod.POST, "/api/v1/users/**").permitAll()
                        .requestMatchers("/api/v1/users/**").permitAll()
                        .requestMatchers("/api/v1/login").permitAll()
                        .requestMatchers("/resources/static/**").permitAll()
                        .requestMatchers("/", "/api-docs/**", "/swagger-ui/**").permitAll()
                        .requestMatchers("/**").permitAll()
                        .anyRequest().permitAll())//.authenticated())
                .sessionManagement((session) -> session
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS));

        http.addFilterAt(
                new JwtAuthenticationFilter(authenticationManager(authenticationConfiguration),
                        objectMapper, jwtUtil), UsernamePasswordAuthenticationFilter.class);

        http.addFilterBefore(jwtFilter, JwtAuthenticationFilter.class);

        http.addFilterBefore(new ExceptionFilter(objectMapper), JwtFilter.class);

        return http.build();
    }
}
