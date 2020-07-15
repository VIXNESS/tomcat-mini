package com.turing.tomcat.httpNIOImpl;

import com.turing.tomcat.httpInterface.Response;
import io.netty.buffer.Unpooled;

import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.http.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


public class NIOResponse implements Response {
    private ChannelHandlerContext context;
    private HttpRequest httpRequest;
    private final Logger logger = LogManager.getLogger();

    public NIOResponse(ChannelHandlerContext context, HttpRequest httpRequest) {
        this.context = context;
        this.httpRequest = httpRequest;
    }

    @Override
    public void write(String out) throws Exception {
       if(out == null || out.isEmpty()) return;
       try {
           FullHttpResponse response = new DefaultFullHttpResponse(HttpVersion.HTTP_1_1,
                   HttpResponseStatus.OK,
                   Unpooled.wrappedBuffer(out.getBytes()));
           response.headers().set("Content-Type", "text/html;");

           context.write(response);
       }finally {
           context.flush();
           context.close();
       }
    }
}
