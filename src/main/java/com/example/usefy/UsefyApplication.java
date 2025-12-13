package com.example.usefy;

import com.example.usefy.model.User;
import com.example.usefy.service.UserService;
import com.example.usefy.service.chat.ChatService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class UsefyApplication {
    public static void main(String[] args) {
        SpringApplication.run(UsefyApplication.class, args);
    }
//    @Bean
//    public CommandLineRunner testChat(
//            UserService userService,
//            ChatService chatService
//    ) {
//        return args -> {
//
//            // 1. Создаём пользователя (если ещё нет)
//            User user = userService.findByUsername("testuser");
//            if (user == null) {
//                user = userService.registerUser(
//                        "testuser",
//                        "password",
//                        "test@test.com"
//                );
//            }
//
//            // 2. Создаём чат
//            var chat = chatService.createChat(user, "First chat");
//            System.out.println("Chat created: " + chat.getId());
//
//            // 3. Добавляем сообщение
//            chatService.addUserMessage(chat.getId(), "Hello from user!");
//
//            System.out.println("Message added");
//        };
//    }
}

