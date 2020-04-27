package com.gateway.server;

import com.gateway.cmds.CommonCmd;

public interface ICmdSender {
    void send(CommonCmd cmd);
}