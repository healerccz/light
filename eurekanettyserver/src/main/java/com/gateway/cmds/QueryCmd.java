package com.gateway.cmds;

public class QueryCmd extends CommonCmd {
    public String id;

    QueryCmd(){

    }

    public QueryCmd(String id){
        this.requestType = CmdString.query;
        this.id = id;
    }

}
