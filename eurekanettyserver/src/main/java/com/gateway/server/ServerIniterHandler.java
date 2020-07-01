package com.gateway.server;

import com.gateway.Applications;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;

import javax.annotation.Resource;

public class ServerIniterHandler extends ChannelInitializer<SocketChannel> {
    @Override
    protected void initChannel(SocketChannel socketChannel) {
        //管道注册handler
        ChannelPipeline pipeline = socketChannel.pipeline();
        //编码通道处理
        pipeline.addLast("decode", new StringDecoder());
        //转码通道处理
        pipeline.addLast("encode", new StringEncoder());
        //聊天服务通道处理
        pipeline.addLast("chat", new ServerHandler((StringRedisTemplate)Applications.getContext().getBean("stringRedisTemplate")));
    }
}