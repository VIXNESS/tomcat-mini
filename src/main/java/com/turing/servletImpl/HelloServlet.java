package com.turing.servletImpl;

import com.turing.tomcat.httpInterface.AbstractServlet;
import com.turing.tomcat.httpInterface.Request;
import com.turing.tomcat.httpInterface.Response;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class HelloServlet extends AbstractServlet {
    private final Logger logger = LogManager.getLogger();
    @Override
    public void doGet(Request request, Response response) throws Exception {
        doPost(request, response);
    }

    @Override
    public void doPost(Request request, Response response) throws Exception {
        StringBuilder stringBuilder = new StringBuilder(this.getClass().getName());
        stringBuilder.append(" ").append(request.getMethod()).append(" ").append(request.getUrl());
        logger.info(stringBuilder.toString());
        response.write("Hello World");
    }
}
