package com.turing.tomcat.httpBIOImpl;

import com.turing.tomcat.httpInterface.Request;
import com.turing.tomcat.httpInterface.Response;
import com.turing.tomcat.httpInterface.Servlet;

public abstract class AbstractServlet implements Servlet {
    public void service(Request request, Response response) throws Exception{
        if(typeGET.equalsIgnoreCase(request.getMethod())) doGet(request, response);
        else doPost(request, response);
    }
}
