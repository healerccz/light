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
        boolean flag;
        flag = nettyClient.sendCom(cmd);

        Thread.sleep(1000);
        String result = baseRedisService.get("update");
        System.out.println("这是redis中key对应对的值：" + result);
//        baseRedisService.delete("update");

        return result;
    }
}
