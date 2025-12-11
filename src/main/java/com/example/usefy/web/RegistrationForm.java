package com.example.usefy.web;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class RegistrationForm {

    @NotBlank(message = "Поле логин обязательно")
    @Size(min = 3, max = 50, message = "Логин должен быть от 3 до 50 символов")
    private String username;

    @NotBlank(message = "Поле пароль обязательно")
    @Size(min = 4, message = "Пароль должен быть не короче 4 символов")
    private String password;

    @NotBlank(message = "Email обязателен")
    @Email(message = "Неверный формат email")
    private String email;
}
