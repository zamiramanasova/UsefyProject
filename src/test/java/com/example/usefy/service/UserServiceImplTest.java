package com.example.usefy.service;

import com.example.usefy.model.User;
import com.example.usefy.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class UserServiceImplTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private UserServiceImpl userService;

    public UserServiceImplTest() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void registerUser_success() {

        // given
        when(userRepository.findByUsername("test"))
                .thenReturn(Optional.empty());

        when(passwordEncoder.encode("1234"))
                .thenReturn("hashed-password");

        // ВАЖНО: говорим mock'у, что он должен вернуть User при save(...)
        when(userRepository.save(any(User.class)))
                .thenAnswer(invocation -> {
                    User u = invocation.getArgument(0);
                    u.setId(1L); // имитируем, как будто БД проставила ID
                    return u;
                });

        // when
        User user = userService.registerUser("test", "1234", "test@mail.com");

        // then
        assertNotNull(user);
        assertEquals("test", user.getUsername());
        assertEquals("hashed-password", user.getPasswordHash());
        assertEquals(1L, user.getId());

        verify(userRepository, times(1)).save(any(User.class));
    }

    @Test
    void registerUser_usernameAlreadyExists() {

        // given
        when(userRepository.findByUsername("test"))
                .thenReturn(Optional.of(new User()));

        // then
        Exception exception = assertThrows(IllegalArgumentException.class, () ->
                userService.registerUser("test", "1234", "test@mail.com")
        );

        assertEquals("Username already exists", exception.getMessage());
    }

    @Test
    void findByUsername_found() {

        User user = new User();
        user.setUsername("test");

        when(userRepository.findByUsername("test"))
                .thenReturn(Optional.of(user));

        User result = userService.findByUsername("test");

        assertNotNull(result);
        assertEquals("test", result.getUsername());
    }

}
