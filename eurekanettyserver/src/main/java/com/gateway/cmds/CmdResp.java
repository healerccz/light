package com.gateway.cmds;

import com.alibaba.fastjson.JSON;

import java.util.Map;

public class CmdResp {

    public static boolean isCmdResp(String str){
        return JSON.parseObject(str).containsKey("stateCode");
    }

    public String sourceId;
    public int serialNum;
    public String requestType;
    public String id;
    public int stateCode;
    public Map<String, String> attributes;

}
