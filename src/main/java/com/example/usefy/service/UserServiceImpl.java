package com.example.usefy.service;

import com.example.usefy.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl {

    @Autowired
    private UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = repository.getUserByUsername(username);
        if(user == null){
            throw new UsernameNotFoundException("could not find user");
        }
        return new MyUser(user);
    }

}
