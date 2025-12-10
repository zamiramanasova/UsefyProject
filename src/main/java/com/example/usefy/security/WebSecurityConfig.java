package com.example.usefy.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class WebSecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                // Для простого учебного REST-API CSRF отключаем
                .csrf(csrf -> csrf.disable())

                // H2-консоль во фреймах
                .headers(headers -> headers
                        .frameOptions(frame -> frame.sameOrigin())
                )

                .authorizeHttpRequests(auth -> auth
                        .requestMatchers(
                                "/",              // главная
                                "/hello",
                                "/h2-console/**", // H2
                                "/api/auth/**",   // НАШИ REST-эндпоинты регистрации/логина
                                "/css/**",
                                "/js/**",
                                "/images/**"
                        ).permitAll()
                        .anyRequest().authenticated()
                )

                // Стандартная форма логина (на будущее, если понадобиться веб-логин)
                .formLogin(form -> form
                        .permitAll()
                )

                .logout(logout -> logout
                        .permitAll()
                );

        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
