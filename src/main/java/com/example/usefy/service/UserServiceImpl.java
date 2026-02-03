package com.example.usefy.service;

import com.example.usefy.model.User;
import com.example.usefy.repository.UserRepository;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.sql.DataSource;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    private DataSource dataSource;

    @PostConstruct
    public void logDataSource() throws Exception {
        System.out.println(">>> DATASOURCE URL = " +
                dataSource.getConnection().getMetaData().getURL());
    }

    @Override
    public User registerUser(String username, String password, String email) {

        // Проверяем, что логин не занят
        Optional<User> existing = userRepository.findByUsername(username);
        if (existing.isPresent()) {
            throw new IllegalArgumentException("Username already exists");
        }

        // Шифруем пароль
        String hashedPassword = passwordEncoder.encode(password);

        // Создаём сущность
        User user = User.builder()
                .username(username)
                .email(email)
                .passwordHash(hashedPassword)
                .build();

        // Сохраняем и ВОЗВРАЩАЕМ результат
        return userRepository.save(user);
    }

    @Override
    public User findByUsername(String username) {
        return userRepository.findByUsername(username).orElse(null);
    }

}
