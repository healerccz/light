package com.gateway.controller;

import com.fasterxml.jackson.databind.util.JSONPObject;
import com.gateway.repository.UserRepository;
import com.gateway.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("user")
public class UserHandler {
    @Autowired
    private UserRepository userRepository;

    @PostMapping("login")
    public User login(@RequestParam("username") String username, @RequestParam("password") String password) {
        User user = userRepository.login(username, password);
        System.out.println(user);
        return user;
    }
}
