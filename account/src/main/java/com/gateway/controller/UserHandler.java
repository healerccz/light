package com.gateway.controller;

import com.fasterxml.jackson.databind.util.JSONPObject;
import com.gateway.entity.User;
import com.gateway.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.session.data.redis.config.annotation.web.http.EnableRedisHttpSession;
import org.springframework.web.bind.annotation.*;
import rx.subjects.SerializedSubject;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.io.Serializable;

@RestController
@RequestMapping("/user")
@Configuration
@EnableRedisHttpSession
public class UserHandler {

    @Autowired
    UserService userService;

    @PostMapping("/login")
    public User login(HttpServletRequest request) {
        Logger logger = LoggerFactory.getLogger(getClass());
        String username = request.getParameter("username");
        String password = request.getParameter("password");
        User user = userService.login(username, password);
        System.out.println(user);
        if (user != null) {
            HttpSession session = request.getSession();
            session.setAttribute("userInfo", user.id + System.currentTimeMillis());
            logger.info("info:" + user.username + "login");
        }

        return user;
    }
}
