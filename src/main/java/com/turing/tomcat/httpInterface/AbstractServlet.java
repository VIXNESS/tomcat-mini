package com.turing.tomcat.httpInterface;


public abstract class AbstractServlet implements Servlet {
    public void service(Request request, Response response) throws Exception{
        if(typeGET.equalsIgnoreCase(request.getMethod())) doGet(request, response);
        else doPost(request, response);
    }
}
