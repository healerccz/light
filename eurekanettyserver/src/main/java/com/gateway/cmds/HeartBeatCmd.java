package com.gateway.cmds;

public class HeartBeatCmd {
    public String sourceId;
    public int serialNum;
    public String requestType;
    public String id;
    //public int stateCode;

    public HeartBeatCmd(){

    }

    public HeartBeatCmd(String id){
        this.id = id;
        sourceId = CmdString.serverId;
        requestType = cmd();
        serialNum = -1;
        //stateCode = 1;
    }

    public static String cmd(){
        return  CmdString.heartBeat;
    }
}
