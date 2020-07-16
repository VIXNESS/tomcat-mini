package com.turing.tomcat.httpBIOImpl;

import com.turing.tomcat.httpInterface.AbstractResponse;


import java.io.OutputStream;

public class BIOResponse extends AbstractResponse {
    protected OutputStream out;
    public BIOResponse(OutputStream out){
        this.out = out;
    }

    @Override
    public void write(String content) throws Exception {
        try {
            out.write(this.makeHttpResponse(content).getBytes());
        }finally {
            out.flush();
        }
    }
}
