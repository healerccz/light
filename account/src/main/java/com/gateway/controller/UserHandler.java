package com.gateway.controller;

import com.alibaba.fastjson.JSON;
import com.gateway.entity.User;
import com.gateway.response.LoginReturnData;
import com.gateway.service.UserService;
import com.gateway.serviceImpl.BaseRedisService;
import com.gateway.token.TokenUtils;
import net.minidev.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
//import org.springframework.session.data.redis.config.annotation.web.http.EnableRedisHttpSession;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

@RestController
@Configuration
public class UserHandler {

    @Autowired
    UserService userService;

    @Autowired
    private BaseRedisService baseRedisService;

    @PostMapping("/login")
//    @CrossOrigin(allowCredentials="true", allowedHeaders="*", methods={RequestMethod.GET,
//            RequestMethod.POST, RequestMethod.DELETE, RequestMethod.OPTIONS,
//            RequestMethod.HEAD, RequestMethod.PUT, RequestMethod.PATCH}, origins="*")
    @ResponseBody
    public LoginReturnData login(@RequestBody Map map) {
        LoginReturnData returnData  = new LoginReturnData();
        Logger logger = LoggerFactory.getLogger(getClass());
        String username = (String)map.get("username");
        String password = (String)map.get("password");
        User user = userService.login(username, password);
        System.out.println(user);
        if (user != null) {
            //生成对应的token
            String token = TokenUtils.getToken();
            Long id = user.id;
            //存放在redis中,key为自定义token令牌,value为用户id
            baseRedisService.setString(token, id+"", 3600L);
            logger.info("info:" + user.username + "login");

            returnData.code = 200+"";
            returnData.msg = "登录成功";
            returnData.token = token;
            returnData.id = user.id+"";
            returnData.username = user.username;
        }
        System.out.println(baseRedisService);
        return returnData;
    }
}
