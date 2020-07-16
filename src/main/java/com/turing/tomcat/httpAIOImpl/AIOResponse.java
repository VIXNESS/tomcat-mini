package com.turing.tomcat.httpAIOImpl;

import com.turing.tomcat.httpInterface.AbstractResponse;
import com.turing.tomcat.httpInterface.Response;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.nio.ByteBuffer;
import java.nio.channels.AsynchronousServerSocketChannel;
import java.nio.channels.AsynchronousSocketChannel;
import java.nio.channels.CompletionHandler;

public class AIOResponse extends AbstractResponse {
    private final AsynchronousSocketChannel channel;

    private static final Logger logger = LogManager.getLogger();
    public AIOResponse(final AsynchronousSocketChannel channel) {
//        this.socket = socket;
        this.channel = channel;
    }

    @Override
    public void write(final String s) throws Exception {
        final String content =  this.makeHttpResponse(s).toString();
        channel.write(ByteBuffer.wrap(content.getBytes()));
    }
}
