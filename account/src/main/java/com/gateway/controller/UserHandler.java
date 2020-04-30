package com.gateway.controller;

import com.fasterxml.jackson.databind.util.JSONPObject;
import com.gateway.entity.User;
import com.gateway.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("user")
public class UserHandler {
    @Autowired
    UserService userService;

    @PostMapping("login")
    public User login(@RequestParam("username") String username, @RequestParam("password") String password) {
        User user = userService.login(username, password);
        System.out.println(user);
        return user;
    }
}
