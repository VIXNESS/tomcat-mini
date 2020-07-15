package com.turing.tomcat.utils;

import io.netty.channel.EventLoopGroup;

public interface EventLoopGroupAutoCloseable extends EventLoopGroup, AutoCloseable {
}
