package com.gateway;

import org.springframework.context.ConfigurableApplicationContext;

public class Applications {
    private static ConfigurableApplicationContext context;

    public static ConfigurableApplicationContext getContext() {
        return context;
    }

    public static void setContext(ConfigurableApplicationContext context) {
        System.out.println("初始化");
        Applications.context = context;
    }
}
