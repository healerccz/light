package com.gateway.gwremain;

import io.netty.channel.group.DefaultChannelGroup;
import io.netty.util.concurrent.EventExecutor;

public class GWChannelGroup extends DefaultChannelGroup {
    public String sourceId;
    public GWChannelGroup(EventExecutor executor) {
        super(executor, false);
    }
}
