package com.gateway.service;

import com.gateway.entity.User;

public interface UserService {
    public User login(String name, String password);
}
