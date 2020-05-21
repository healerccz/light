package com.gateway;

import com.gateway.redis.BaseRedisService;
import com.gateway.server.NettyServer;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import com.gateway.Applications;

@SpringBootApplication
@EnableDiscoveryClient
public class NettyServerApplication {
    public static void main(String[] args) {
        ConfigurableApplicationContext run = SpringApplication.run(NettyServerApplication.class, args);
        Applications.setContext(run);
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


//    @Bean
//    public BaseRedisService getBaseRedisServic() {
//        return new BaseRedisService();
//    }
}
