package com.turing.tomcat.httpBIOImpl;

import com.turing.tomcat.httpInterface.Response;

import java.io.OutputStream;

public class BIOResponse implements Response {
    protected OutputStream out;
    public BIOResponse(OutputStream out){
        this.out = out;
    }

    @Override
    public void write(String content) throws Exception {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(defaultHTTPHeader)
                .append(defaultContentType)
                .append(delimiter)
                .append(content);
        out.write(stringBuilder.toString().getBytes());
        out.flush();
    }
}
