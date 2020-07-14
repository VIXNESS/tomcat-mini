package com.turing.tomcat.httpInterface;

public interface Servlet {
    void doGet(Request request, Response response) throws Exception;
    void doPost(Request request, Response response) throws Exception;
    String typeGET = "GET";
    String typePOST = "POST";
}
