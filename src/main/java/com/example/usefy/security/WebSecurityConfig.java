package com.example.usefy.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class WebSecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                // 1. H2-консоль должна работать без CSRF и с фреймами
                .csrf(csrf -> csrf
                        .ignoringRequestMatchers("/h2-console/**")
                )
                .headers(headers -> headers
                        .frameOptions(frame -> frame.sameOrigin())
                )

                // 2. Правила доступа
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers(
                                "/",
                                "/hello",
                                "/h2-console/**",   // H2-консоль разрешаем
                                "/css/**",
                                "/js/**",
                                "/images/**"
                        ).permitAll()
                        .anyRequest().authenticated()
                )

                // 3. Форма логина по умолчанию
                .formLogin(form -> form
                        .loginPage("/login")      // если нет своего шаблона, Spring сделает дефолтный
                        .permitAll()
                )

                // 4. Logout
                .logout(logout -> logout
                        .permitAll()
                );

        return http.build();
    }
}