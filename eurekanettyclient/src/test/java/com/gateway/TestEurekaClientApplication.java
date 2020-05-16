package com.gateway;

import com.gateway.client.NettyClient;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.context.annotation.Bean;

import java.io.IOException;

@SpringBootApplication
@EnableDiscoveryClient
public class TestEurekaClientApplication {
    public static void main(String[] args) {
        SpringApplication.run(NettyClientApplication.class, args);
    }

    @Bean
    public NettyClient run() throws Exception {
        NettyClient nettyClient = new NettyClient("127.0.0.1", 8999);
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    nettyClient.run();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

        }).start();
        return nettyClient;
    }
}
