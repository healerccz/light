package com.gateway.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.gateway.client.NettyClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("light")
public class nettyClientHandler {
    @Autowired
    private NettyClient nettyClient;

    @GetMapping("command/{id}")
    public String sendCom(@PathVariable("id") String id) {
        Map<String, String> map = new HashMap<String, String>();
        boolean flag;
        flag = nettyClient.sendCom(id);
        map.put("success", flag ? "true" : "false");
        return JSON.toJSONString(map);
    }
}
