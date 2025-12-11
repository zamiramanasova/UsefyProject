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
                // 1) разрешаем CSRF для форм, но игнорируем REST-эндпоинты и H2
                .csrf(csrf -> csrf
                        .ignoringRequestMatchers("/api/auth/**", "/h2-console/**")
                )
                .headers(headers -> headers
                        .frameOptions(frame -> frame.sameOrigin())
                )

                // 2) авторизация запросов
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers(
                                "/", "/hello",
                                "/register", "/login",
                                "/h2-console/**",
                                "/css/**", "/js/**", "/images/**",
                                "/api/auth/**" // REST эндпоинты разрешаем (т.к. они делают свою проверку)
                        ).permitAll()
                        .anyRequest().authenticated()
                )

                // 3) форма логина (стандартная)
                .formLogin(form -> form
                        .loginPage("/login") // если нет шаблона, можно убрать эту строку — тогда Spring сделает дефолтную
                        .permitAll()
                )
                .logout(logout -> logout.permitAll());

        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
