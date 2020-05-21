package com.gateway.controller;

import com.gateway.redis.BaseRedisService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("server")
public class TestController {
    @Autowired
    private BaseRedisService baseRedisService;

    @GetMapping("test")
    public boolean test() {
        String key = "keykey";
        String value = "valuevalue";
        baseRedisService.setString(key, value, 3600L);
        return true;
    }
}
