package com.turing.tomcat.httpBIOImpl;


import com.turing.tomcat.httpInterface.*;
import com.turing.tomcat.utils.PropertyMappingFactoryImpl;
import com.turing.tomcat.utils.PropertyMappingFactoryInf;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


import java.io.InputStream;
import java.io.OutputStream;
import java.net.ServerSocket;

import java.net.Socket;
import java.util.Map;
import java.util.Properties;


public class BIOTomcat extends AbstractTomcat {
    protected ServerSocket serverSocket;
    protected Map<String, Servlet> servletMap;
    protected Properties webProperties;
    protected final Logger logger = LogManager.getLogger();

    public BIOTomcat(PropertyMappingFactoryInf propertyMappingFactory, int port) {
        servletMap = propertyMappingFactory.getPropertyMapping();
        this.port = port;
    }

    @Override
    public void start() throws Exception {
        init();
        serverSocket = new ServerSocket(this.port);
        logger.info("Port of Tomcat is started, port is " + this.port);
        while (true) {
            Socket client = serverSocket.accept();
            process(client);

        }

    }

    private void process(Socket client) throws Exception {
        try (InputStream in = client.getInputStream();
             OutputStream out = client.getOutputStream()) {
            Request request = new BIORequest(in);
            Response response = new BIOResponse(out);

            String url = request.getUrl();
            if (url != null && servletMap.containsKey(url)) servletMap.get(url).service(request, response);
            else response.write("404 - Not Found");
        }
        client.close();
    }
}
