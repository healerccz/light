package com.gateway;

import com.gateway.server.NettyServer;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.context.annotation.Bean;

import java.util.concurrent.ArrayBlockingQueue;

@SpringBootApplication
@EnableDiscoveryClient
public class NettyServerApplication {
    public static void main(String[] args) {
        SpringApplication.run(NettyServerApplication.class, args);
    }

    @Bean
    public NettyServer run() {
        NettyServer nettyServer = new NettyServer(8999);
        new Thread(new Runnable() {
            @Override
            public void run() {
                nettyServer.run();
            }
        }).start();
        return nettyServer;
    }
}
