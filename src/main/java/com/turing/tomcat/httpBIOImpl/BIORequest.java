package com.turing.tomcat.httpBIOImpl;

import com.turing.tomcat.httpInterface.Request;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.InputStream;

public class BIORequest implements Request {
   protected String method;
   protected String url;

   private final Logger logger = LogManager.getLogger();
    public BIORequest(InputStream in) {
        String content = "";
        byte[] buffer = new byte[defaultBufferLength];
        int len = 0;
        try{
            if((len = in.read(buffer)) > 0) {
                content = new String(buffer, 0, len);
                String line = content.split("\\n")[0];
                String[] arr = line.split("\\s");
                this.method = arr[0];
                this.url = arr[1].split("\\?")[0];
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    @Override
    public String getUrl() {
        return url;
    }

    @Override
    public String getMethod() {
        return method;
    }
}
