package com.example.usefy;

import com.example.usefy.model.User;
import com.example.usefy.repository.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.password.PasswordEncoder;

@SpringBootApplication
public class UsefyApplication {
    public static void main(String[] args) {
        SpringApplication.run(UsefyApplication.class, args);
    }

    @Bean
    public CommandLineRunner createUser(UserRepository userRepository, PasswordEncoder encoder) {
        return args -> {
            if (userRepository.findByUsername("user").isEmpty()) {
                User user = User.builder()
                        .username("user")
                        .email("user@mail.com")
                        .passwordHash(encoder.encode("1234"))
                        .build();

                userRepository.save(user);
            }
        };
    }

}
