package com.example.usefy.service;

import com.example.usefy.model.User;
import com.example.usefy.repository.UserRepository;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.sql.DataSource;
import java.util.Optional;

@Slf4j
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
        log.info("Регистрация нового пользователя: {}", username);

        Optional<User> existing = userRepository.findByUsername(username);
        if (existing.isPresent()) {
            log.warn("Попытка регистрации с существующим username: {}", username);
            throw new IllegalArgumentException("Username already exists");
        }

        String hashedPassword = passwordEncoder.encode(password);

        User user = User.builder()
                .username(username)
                .email(email)
                .passwordHash(hashedPassword)
                .build();

        User saved = userRepository.save(user);
        log.info("Пользователь {} успешно зарегистрирован с ID {}", username, saved.getId());

        return saved;
    }

    @Override
    public User findByUsername(String username) {
        return userRepository.findByUsername(username)
                .orElseGet(() -> {
                    log.debug("Пользователь {} не найден", username);
                    return null;
                });
    }
}
