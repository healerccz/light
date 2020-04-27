package com.gateway.cmds;


import java.util.ArrayList;
import java.util.Map;

public class DevBatchCtrlCmd extends CommonCmd {
    public ArrayList<String> id;
    public Map<String, String> attributes;
    public  DevBatchCtrlCmd(ArrayList<String> idArr, Map<String, String>  attributes){
        this.requestType = CmdString.singleCtrl;
        this.id = idArr;
        this.attributes = attributes;
    }
}
