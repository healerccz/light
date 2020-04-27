package com.gateway.cmds;

import java.util.Map;

public class DevCtrlCmd extends CommonCmd {
    public String id;
    public Map<String, String> attributes;

    public DevCtrlCmd(){

    }

    public  DevCtrlCmd(String id, Map<String, String> attributes){
        this.requestType = CmdString.singleCtrl;
        this.id = id;
        this.attributes = attributes;
    }
}


