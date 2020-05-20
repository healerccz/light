package com.gateway.controller;

import com.gateway.redis.BaseRedisService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Configuration
public class TestController {

    @Autowired
    private BaseRedisService baseRedisService;

    @GetMapping("/test")
    public void test() {
        String key = "this is a string for testing";
        String value = "this is a balue";
        baseRedisService.setString(key, value, 3600L);
    }
}
