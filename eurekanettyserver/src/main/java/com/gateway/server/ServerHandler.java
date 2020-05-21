package com.gateway.server;import com.alibaba.fastjson.JSON;import com.gateway.cmds.*;import com.gateway.redis.BaseRedisService;import com.gateway.test.TestCmd;import io.netty.channel.*;import io.netty.channel.group.ChannelGroup;import io.netty.channel.group.DefaultChannelGroup;import io.netty.util.concurrent.GlobalEventExecutor;import org.slf4j.Logger;import org.slf4j.LoggerFactory;import org.springframework.beans.factory.annotation.Autowired;import org.springframework.context.annotation.Configuration;import org.springframework.data.redis.core.RedisTemplate;import org.springframework.data.redis.core.StringRedisTemplate;import org.springframework.stereotype.Component;import org.springframework.stereotype.Service;import org.springframework.web.bind.annotation.RestController;import java.util.ArrayList;import java.util.HashMap;import java.util.Map;@Servicepublic class ServerHandler extends SimpleChannelInboundHandler<String> {    /**     * 所有的活动用户     */    public static final ChannelGroup group = new DefaultChannelGroup(GlobalEventExecutor.INSTANCE);    /**     * 源id => 通道映射     */    public static final Map<String, Channel> srcIdChMap = new HashMap<String, Channel>();//    @Autowired//    private StringRedisTemplate stringRedisTemplate;    @Autowired    private RedisTemplate<String, String> redisTemplate;    /**     * 读取消息通道     *     * @param context     * @param s     * @throws Exception     */    @Override    protected void channelRead0(ChannelHandlerContext context, String s)            throws Exception {        Logger logger = LoggerFactory.getLogger(getClass());//        BaseRedisService baseRedisService = new BaseRedisService();        Channel channel = context.channel();//        System.out.println("[" + channel.remoteAddress() + "]: " + s + "\n");        // logger.info("[" + channel.remoteAddress() + "]: " + s + "\n");//        System.out.println("这是所有给服务器发的信息：" + s);        if (s.contains("update") && !s.contains("MAC")) { // 灯控响应结果            System.out.println("更新出来啦");            System.out.println(s);//            try {//                baseRedisService.setString("updateSwitchResult", s, 180L);//            } catch (Exception e) {//                e.printStackTrace();//                System.out.println("异常了");//            }            try {//                stringRedisTemplate.opsForValue().set("updateSwitchResult", s, 180L);                redisTemplate.opsForValue().set("num","123");            } catch (Exception e) {                e.printStackTrace();                System.out.println("出异常了");            }        }        if (s.contains("query")) { // 查询灯的状态响应结果            System.out.println("查询也出来啦");            System.out.println(s);//            stringRedisTemplate.opsForValue().set("queryResult", s, 180L);        }        String[] strArr = s.split("\n");        for (String str : strArr) {            HeartBeatCmd cmd = JSON.parseObject(str, HeartBeatCmd.class);            // 网关与服务器通信（由心跳标识）            if( cmd != null && cmd.sourceId != null && cmd.requestType.equals(HeartBeatCmd.cmd()) && cmd.requestType.equals("heart")){                for (Channel ch : group) {                    if (ch == channel) {                        // 回复心跳                        HeartBeatCmd htbt = new HeartBeatCmd(CmdString.serverId);                        String strHtbt = JSON.toJSONString(htbt);                        ch.writeAndFlush( strHtbt + "\n");                        if (!srcIdChMap.containsKey(cmd.sourceId)){ // 建立网关和netty服务器的映射                            srcIdChMap.put(cmd.sourceId, ch);                            System.out.println("建立" + channel.remoteAddress() + " " + cmd.sourceId + "映射关系");                            // logger.info("建立" + channel.remoteAddress() + " " + cmd.sourceId + "映射关系");                        }                        break;                    }                }            }else { // 操作普通设备//                System.out.println("from: [" + channel.remoteAddress() + "]: " + str + "\n");                // logger.info("from: [" + channel.remoteAddress() + "]: " + str + "\n");                processTestCmd(channel, str);            }        }    }    /**     * 向设备发送命令     *     * @param ch     * @param content     * @return     */    private boolean processTestCmd(Channel ch, String content){        TestCmd cmd = JSON.parseObject(content, TestCmd.class);        if(cmd != null){            switch (cmd.cmd)            {                case 1: {                    sendDevCtrlCmdOn(ch); // 打开设备开关                }                     break;                case 2: {                    sendDevCtrlCmdOff(ch); // 关闭设备开关                }                    break;                case 3: {                    sendQueryCmd(ch); // 查询设备状态                }                    break;                case 4: {                    sendBatchDevCtrlCmdOn(ch); // 批量打开设备开关                }                    break;                case 5: {                    sendBatchDevCtrlCmdOff(ch); // 批量关闭设备开关                }                    break;                default:                    break;            }        }        return cmd != null;    }    private void sendDevCtrlCmdOn(Channel ch){        DevCtrlCmd cmd = new DevCtrlCmd(CmdString.lightId, (Map<String,String>) JSON.toJSON(SWI.on()));        System.out.println(JSON.toJSON(SWI.on()));        Logger logger = LoggerFactory.getLogger(getClass());        // logger.info("send command : " + JSON.toJSON(SWI.on()) + "\n");        sendToChannel(ch, cmd);    }    private void sendDevCtrlCmdOff(Channel ch){        DevCtrlCmd cmd = new DevCtrlCmd(CmdString.lightId, (Map<String,String>) JSON.toJSON(SWI.off()));//        System.out.println(JSON.toJSON(SWI.off()));        Logger logger = LoggerFactory.getLogger(getClass());        // logger.info("send command : " + JSON.toJSON(SWI.on()) + "\n");        sendToChannel(ch, cmd);    }    private void sendBatchDevCtrlCmdOn(Channel ch){        sendBatchDevCtrl(ch,true);    }    private void sendBatchDevCtrlCmdOff(Channel ch){        sendBatchDevCtrl(ch,false);    }    private void sendBatchDevCtrl(Channel ch,Boolean on){        ArrayList<String> arr = new ArrayList<>();        arr.add(CmdString.lightId1);        arr.add(CmdString.lightId);        DevBatchCtrlCmd cmd = new DevBatchCtrlCmd(arr, (Map<String,String>) JSON.toJSON(on ? SWI.on() : SWI.off()));        sendToChannel(ch, cmd);    }    private void sendQueryCmd(Channel ch){        QueryCmd cmd = new QueryCmd(CmdString.batchQueryId);        sendToChannel(ch, cmd);    }    private void sendToChannel(Channel ch, CommonCmd cmd){        Logger logger = LoggerFactory.getLogger(getClass());        if(srcIdChMap.containsKey(CmdString.gwId)){            String str = JSON.toJSONString(cmd) + "\n";            ChannelFuture future = srcIdChMap.get(CmdString.gwId).writeAndFlush(str);//            System.out.println("send to : [" + srcIdChMap.get(CmdString.gwId).remoteAddress() + "]:  " + str + "\n");//            future.addListener(future1 -> {//                System.out.println("sended" + "\n");//                // logger.info("sent to : [" + srcIdChMap.get(CmdString.gwId).remoteAddress() + "]:  " + str + "\n");//            });            //Future监听器            future.addListener(new ChannelFutureListener() {                @Override                public void operationComplete(ChannelFuture channelFuture) throws Exception {//                    if (future.isSuccess()) {//                        ch.writeAndFlush("true");//                    }//                    else {//                        ch.writeAndFlush("false");//                    }                }            });        }else{            System.out.println("网关离线");            logger.error("网关离线" + "\n");        }    }    /**     * 处理新加的消息通道     *     * @param ctx     * @throws Exception     */    @Override    public void handlerAdded(ChannelHandlerContext ctx) throws Exception {        Channel channel = ctx.channel();        for (Channel ch : group) {            if (ch == channel) {                ch.writeAndFlush("[" + channel.remoteAddress() + "] coming");            }        }        group.add(channel);    }    /**     * 处理退出消息通道     *     * @param ctx     * @throws Exception     */    @Override    public void handlerRemoved(ChannelHandlerContext ctx) throws Exception {        Channel channel = ctx.channel();        for (Channel ch : group) {            if (ch == channel) {                ch.writeAndFlush("[" + channel.remoteAddress() + "] leaving");            }        }        String keyFound = "";        for(String key:srcIdChMap.keySet()){            if(srcIdChMap.get(key) == channel){                keyFound = key;                break;            }        }        srcIdChMap.remove(keyFound);        group.remove(channel);    }    /**     * 在建立连接时发送消息     *     * @param ctx     * @throws Exception     */    @Override    public void channelActive(ChannelHandlerContext ctx) throws Exception {        Logger logger = LoggerFactory.getLogger(getClass());        Channel channel = ctx.channel();        boolean active = channel.isActive();        if (active) {//            System.out.println("[" + channel.remoteAddress() + "] is online");            // logger.info("[" + channel.remoteAddress() + "] is online" + "\n");        } else {//            System.out.println("[" + channel.remoteAddress() + "] is offline");            // logger.info("[" + channel.remoteAddress() + "] is offline" + "\n");        }        ctx.writeAndFlush("[server]: welcome");    }    /**     * 退出时发送消息     *     * @param ctx     * @throws Exception     */    @Override    public void channelInactive(ChannelHandlerContext ctx) throws Exception {        Channel channel = ctx.channel();        Logger logger = LoggerFactory.getLogger(getClass());        if (!channel.isActive()) {//            System.out.println("[" + channel.remoteAddress() + "] is offline");            // logger.info("[" + channel.remoteAddress() + "] is offline" + "\n");        } else {//            System.out.println("[" + channel.remoteAddress() + "] is online");            // logger.info("[" + channel.remoteAddress() + "] is online" + "\n");        }    }    /**     * 异常捕获     *     * @param ctx     * @param e     * @throws Exception     */    @Override    public void exceptionCaught(ChannelHandlerContext ctx, Throwable e) throws Exception {        Channel channel = ctx.channel();        Logger logger = LoggerFactory.getLogger(getClass());//        System.out.println("[" + channel.remoteAddress() + "] leave the room");        // logger.info("[" + channel.remoteAddress() + "] leave the room" + "\n");        ctx.close().sync();    }}