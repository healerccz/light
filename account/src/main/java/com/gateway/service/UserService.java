package com.gateway.service;

import com.gateway.entity.User;
import org.apache.ibatis.annotations.Select;

import java.util.List;

public interface UserService {
    User login(String name, String password);


}
