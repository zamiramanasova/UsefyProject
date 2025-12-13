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
                // 1) CSRF
                .csrf(csrf -> csrf
                        // REST и H2 не требуют CSRF
                        .ignoringRequestMatchers("/api/auth/**", "/h2-console/**")
                )

                // 2) H2 console
                .headers(headers -> headers
                        .frameOptions(frame -> frame.sameOrigin())
                )

                // 3) Авторизация
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers(
                                "/",                 // главная
                                "/hello",
                                "/register",         // регистрация
                                "/login",            // логин
                                "/h2-console/**",    // H2
                                "/css/**",           // статика
                                "/js/**",
                                "/images/**",
                                "/api/auth/**"       // REST auth
                        ).permitAll()
                        .anyRequest().authenticated()
                )

                // 4) Form Login
                .formLogin(form -> form
                        .loginPage("/login")
                        .defaultSuccessUrl("/profile", true)
                        .permitAll()
                )

                // 5) Logout
                .logout(logout -> logout
                        .logoutSuccessUrl("/")
                        .permitAll()
                );

        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
