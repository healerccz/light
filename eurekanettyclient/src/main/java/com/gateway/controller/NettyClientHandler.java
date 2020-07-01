package com.gateway.controller;

import com.gateway.client.NettyClient;
import com.gateway.redis.BaseRedisService;
import com.gateway.response.ClientReturnData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Timer;
import java.util.TimerTask;

@RestController
@RequestMapping("light")
public class NettyClientHandler {
    @Autowired
    private NettyClient nettyClient;

    @Autowired
    private BaseRedisService baseRedisService;

    @GetMapping("command")
    public String sendCom(@RequestParam String cmd) throws InterruptedException {
        String key;
        nettyClient.sendCom(cmd);
        key = cmd.equals("3") ? "query" : "update";
        Thread.sleep(4000);
        String result = "";
        try {
            result = baseRedisService.get(key);
        } catch (Exception e) {
            e.printStackTrace();
        }
        System.out.println("cmd的值：" + cmd);
        System.out.println("key的值:"+ key);
        System.out.println("value的值：" + result);
        baseRedisService.delete(key);
        if (result != null) {
            result = result.replace("00124B0012994BA3", "B0012994BA3");
            result = result.replace("\n", "");
        }

        return result;
    }
}
