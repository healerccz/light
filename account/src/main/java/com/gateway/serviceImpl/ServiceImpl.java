package com.gateway.serviceImpl;

import com.gateway.dao.UserDao;
import com.gateway.entity.User;
import com.gateway.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class ServiceImpl implements UserService {

    @Autowired
    UserDao userDao;

    @Override
    public User login(String username, String password) {
        return userDao.login(username, password);
    }
}
