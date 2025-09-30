package com.example.usefy.controller;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@EnableJpaRepositories("com.example.usefy.repository")
public class UsefyApplication {

    public static void main(String[] args) {
        SpringApplication.run(UsefyApplication.class, args);
    }

}
