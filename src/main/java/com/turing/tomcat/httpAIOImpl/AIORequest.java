package com.turing.tomcat.httpAIOImpl;

import com.turing.tomcat.httpInterface.Request;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


import java.nio.ByteBuffer;

import java.nio.channels.AsynchronousSocketChannel;


public class AIORequest implements Request {
    private final String httpContent;

    private final static Logger logger = LogManager.getLogger();
    private String url;
    private String method;

    public AIORequest(final String httpContent) {
        this.method = null;
        this.httpContent = httpContent;
        process();
    }

    private void process() {
        if (httpContent != null && !httpContent.isBlank()) {
            final String line = httpContent.split("\\n")[0];
            final String[] arr = line.split("\\s");
            method = arr[0];
            url = arr[1].split("\\?")[0];
        }
    }

    @Override
    public String getUrl() {
        return this.url;
    }

    @Override
    public String getMethod() {
        return this.method;
    }
}
