package com.turing.tomcat.utils;

import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;

public class NioEventLoopGroupAutoCloseable extends NioEventLoopGroup implements EventLoopGroupAutoCloseable {
    @Override
    public void close() throws Exception {
        this.shutdownGracefully();
    }
}
