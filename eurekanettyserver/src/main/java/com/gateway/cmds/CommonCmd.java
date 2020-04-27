package com.gateway.cmds;


public class CommonCmd {

    public static int serialIndex = 100000;

    public String sourceId;
    public int serialNum;
    public String requestType;

    public CommonCmd(){
        this.sourceId = CmdString.serverId;
        this.serialNum = CommonCmd.serialIndex++; // 序列号自增
    }
}

