package com.gateway.repository;

import com.gateway.entity.User;

public interface UserRepository {
    public User login(String username, String password);
}
