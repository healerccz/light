package com.gateway.server;import com.alibaba.fastjson.JSON;import com.gateway.cmds.*;import com.gateway.test.TestCmd;import io.netty.channel.Channel;import io.netty.channel.ChannelFuture;import io.netty.channel.ChannelHandlerContext;import io.netty.channel.SimpleChannelInboundHandler;import io.netty.channel.group.ChannelGroup;import io.netty.channel.group.DefaultChannelGroup;import io.netty.util.concurrent.GlobalEventExecutor;import java.util.ArrayList;import java.util.HashMap;import java.util.Map;//import com.sun.xml.internal.xsom.impl.scd.Iterators;public class ServerHandler extends SimpleChannelInboundHandler<String> {    /**     * 所有的活动用户     */    public static final ChannelGroup group = new DefaultChannelGroup(GlobalEventExecutor.INSTANCE);    /**     * 源id => 通道映射     */    public static final Map<String, Channel> srcIdChMap = new HashMap<String, Channel>();    /**     * 读取消息通道     *     * @param context     * @param s     * @throws Exception     */    @Override    protected void channelRead0(ChannelHandlerContext context, String s)            throws Exception {        Channel channel = context.channel();//        System.out.println("[" + channel.remoteAddress() + "]: " + s + "\n");        String[] strArr = s.split("\n");        for (String str : strArr) {            HeartBeatCmd cmd = JSON.parseObject(str, HeartBeatCmd.class);            // 网关连接服务器（由心跳标识）            if( cmd != null && cmd.sourceId != null && cmd.requestType.equals(HeartBeatCmd.cmd()) && cmd.requestType.equals("heart")){                for (Channel ch : group) {                    if (ch == channel) {                        //回复心跳                        //HeartBeatCmd htbt = new HeartBeatCmd(CmdString.serverId);                        //String strHtbt = JSON.toJSONString(htbt);                        //ch.writeAndFlush( strHtbt + "\n");                        if (!srcIdChMap.containsKey(cmd.sourceId)){ // 建立网关和netty服务器的映射                            srcIdChMap.put(cmd.sourceId, ch);                            System.out.println("建立" + channel.remoteAddress() + " " + cmd.sourceId + "映射关系");                        }                        break;                    }                }            }else { // 普通设备                System.out.println("from: [" + channel.remoteAddress() + "]: " + str + "\n");                processTestCmd(channel, str);            }        }    }    /**     * 向设备发送命令     *     * @param ch     * @param content     * @return     */    private boolean processTestCmd(Channel ch, String content){        TestCmd cmd = JSON.parseObject(content, TestCmd.class);        if(cmd != null){            switch (cmd.cmd)            {                case 1: {                    sendDevCtrlCmdOn(); // 打开设备开关                }                     break;                case 2: {                    sendDevCtrlCmdOff(); // 关闭设备开关                }                    break;                case 3: {                    sendQueryCmd(); // 查询设备状态                }                    break;                case 4: {                    sendBatchDevCtrlCmdOn(); // 批量打开设备开关                }                    break;                case 5: {                    sendBatchDevCtrlCmdOff(); // 批量关闭设备开关                }                    break;                default:                    break;            }        }        return cmd != null;    }    private void sendDevCtrlCmdOn(){        DevCtrlCmd cmd = new DevCtrlCmd(CmdString.lightId, (Map<String,String>) JSON.toJSON(SWI.on()));        System.out.println(JSON.toJSON(SWI.on()));        sendToChannel(cmd);    }    private void sendDevCtrlCmdOff(){        DevCtrlCmd cmd = new DevCtrlCmd(CmdString.lightId, (Map<String,String>) JSON.toJSON(SWI.off()));        System.out.println(JSON.toJSON(SWI.off()));        sendToChannel(cmd);    }    private void sendBatchDevCtrlCmdOn(){        sendBatchDevCtrl(true);    }    private void sendBatchDevCtrlCmdOff(){        sendBatchDevCtrl(false);    }    private void sendBatchDevCtrl(Boolean on){        ArrayList<String> arr = new ArrayList<>();        arr.add(CmdString.lightId1);        arr.add(CmdString.lightId);        DevBatchCtrlCmd cmd = new DevBatchCtrlCmd(arr, (Map<String,String>) JSON.toJSON(on ? SWI.on() : SWI.off()));        sendToChannel(cmd);    }    private void sendQueryCmd(){        QueryCmd cmd = new QueryCmd(CmdString.batchQueryId);        sendToChannel(cmd);    }    private void sendToChannel(CommonCmd cmd){        if(srcIdChMap.containsKey(CmdString.gwId)){            String str = JSON.toJSONString(cmd) + "\n";            ChannelFuture future = srcIdChMap.get(CmdString.gwId).writeAndFlush(str);            System.out.println("send to : [" + srcIdChMap.get(CmdString.gwId).remoteAddress() + "]:  " + str + "\n");            future.addListener(future1 -> {                System.out.println("sended" + "\n");            });        }else{            System.out.println("没有找到命令中指定的设备通道");        }    }    /**     * 处理新加的消息通道     *     * @param ctx     * @throws Exception     */    @Override    public void handlerAdded(ChannelHandlerContext ctx) throws Exception {        Channel channel = ctx.channel();        for (Channel ch : group) {            if (ch == channel) {                ch.writeAndFlush("[" + channel.remoteAddress() + "] coming");            }        }        group.add(channel);    }    /**     * 处理退出消息通道     *     * @param ctx     * @throws Exception     */    @Override    public void handlerRemoved(ChannelHandlerContext ctx) throws Exception {        Channel channel = ctx.channel();        for (Channel ch : group) {            if (ch == channel) {                ch.writeAndFlush("[" + channel.remoteAddress() + "] leaving");            }        }        String keyFound = "";        for(String key:srcIdChMap.keySet()){            if(srcIdChMap.get(key) == channel){                keyFound = key;                break;            }        }        srcIdChMap.remove(keyFound);        group.remove(channel);    }    /**     * 在建立连接时发送消息     *     * @param ctx     * @throws Exception     */    @Override    public void channelActive(ChannelHandlerContext ctx) throws Exception {        Channel channel = ctx.channel();        boolean active = channel.isActive();        if (active) {            System.out.println("[" + channel.remoteAddress() + "] is online");        } else {            System.out.println("[" + channel.remoteAddress() + "] is offline");        }        ctx.writeAndFlush("[server]: welcome");    }    /**     * 退出时发送消息     *     * @param ctx     * @throws Exception     */    @Override    public void channelInactive(ChannelHandlerContext ctx) throws Exception {        Channel channel = ctx.channel();        if (!channel.isActive()) {            System.out.println("[" + channel.remoteAddress() + "] is offline");        } else {            System.out.println("[" + channel.remoteAddress() + "] is online");        }    }    /**     * 异常捕获     *     * @param ctx     * @param e     * @throws Exception     */    @Override    public void exceptionCaught(ChannelHandlerContext ctx, Throwable e) throws Exception {        Channel channel = ctx.channel();        System.out.println("[" + channel.remoteAddress() + "] leave the room");        ctx.close().sync();    }}