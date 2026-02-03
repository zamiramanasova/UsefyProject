package com.example.usefy.service;

import com.example.usefy.model.User;

public interface UserService {

    User registerUser(String username, String password, String email);

    User findByUsername(String username);
}
