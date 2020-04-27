package com.gateway.cmds;

public  class SWI{
    public String SWI;
    public static com.gateway.cmds.SWI on(){
        com.gateway.cmds.SWI obj = new SWI();
        obj.SWI = "ON";
        return obj;
    }

    public static com.gateway.cmds.SWI off(){
        com.gateway.cmds.SWI obj = new SWI();
        obj.SWI = "OFF";
        return obj;
    }
}

