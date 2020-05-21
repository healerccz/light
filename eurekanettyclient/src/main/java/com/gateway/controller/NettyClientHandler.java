package com.gateway.controller;

import com.gateway.client.NettyClient;
import com.gateway.redis.BaseRedisService;
import com.gateway.response.ClientReturnData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("light")
public class NettyClientHandler {
    @Autowired
    private NettyClient nettyClient;

    @Autowired
    private BaseRedisService baseRedisService;

    @GetMapping("command")
    public ClientReturnData sendCom(@RequestParam String cmd) {
        ClientReturnData returnData = new ClientReturnData();
        boolean flag;
        flag = nettyClient.sendCom(cmd);
        returnData.code = flag ? 200+"" : 500+"";
        returnData.msg = flag ? "操作成功" : "操作失败";
        String value = baseRedisService.get("key");
        System.out.println("这是redis中key对应对的值："+value);
        return returnData;
    }
}
