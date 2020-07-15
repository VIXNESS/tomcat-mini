package com.turing.tomcat.httpNIOImpl;

import com.turing.tomcat.httpInterface.Request;

import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.http.HttpRequest;
import io.netty.handler.codec.http.QueryStringDecoder;

import java.util.List;
import java.util.Map;


public class NIORequest implements Request {
    private ChannelHandlerContext context;
    private HttpRequest httpRequest;

    public NIORequest(ChannelHandlerContext context, HttpRequest httpRequest){
        this.context = context;
        this.httpRequest = httpRequest;
    }



    @Override
    public String getUrl() {
        return httpRequest.uri();
    }

    @Override
    public String getMethod() {
        return httpRequest.method().name();
    }

    public Map<String, List<String>> getParameters(String name){
        QueryStringDecoder decoder = new QueryStringDecoder(httpRequest.uri());
        return decoder.parameters();
    }

    public String getParameter(String name){
       List<String> params = getParameters(name).get(name);
       if(params != null) return params.get(0);
       else return null;
    }
}
