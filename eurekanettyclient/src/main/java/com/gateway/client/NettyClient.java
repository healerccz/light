package com.gateway.client;

import com.alibaba.fastjson.JSON;
import com.gateway.test.TestCmd;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.regex.Pattern;

public class NettyClient {

    private String ip;

    private int port;

    private boolean stop = false;

    private Channel channel;

    public NettyClient(String ip, int port) {
        this.ip = ip;
        this.port = port;
    }

    public static boolean isInteger(String str) {
        Pattern pattern = Pattern.compile("^[-\\+]?[\\d]*$");
        return pattern.matcher(str).matches();
    }

    public void run() throws IOException {
        //设置一个多线程循环器
        EventLoopGroup workerGroup = new NioEventLoopGroup();
        //启动附注类
        Bootstrap bootstrap = new Bootstrap();
        bootstrap.group(workerGroup);
        //指定所使用的NIO传输channel
        bootstrap.channel(NioSocketChannel.class);
        //指定客户端初始化处理
        bootstrap.handler(new ClientIniterHandler());
        try {
            //连接服务
            this.channel = bootstrap.connect(ip, port).sync().channel();
        } catch (InterruptedException e) {
            e.printStackTrace();
            System.exit(1);
        }
//        finally {
//            workerGroup.shutdownGracefully();
//        }
    }

    public boolean sendCom(String  content) {
        if(isInteger(content)){
            TestCmd cmd = new TestCmd();
            cmd.cmd = Integer.parseInt(content);
            this.channel.writeAndFlush(JSON.toJSONString(cmd));
            System.out.println(JSON.toJSONString(cmd));
            Logger logger = LoggerFactory.getLogger(getClass());
            logger.info("send command : " + JSON.toJSONString(cmd) + "\n");

            return true;
        }
        return false;
    }
}