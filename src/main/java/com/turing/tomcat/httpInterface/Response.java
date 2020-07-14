package com.turing.tomcat.httpInterface;


public interface Response {
     String defaultHTTPHeader = "HTTP/1.1 200 OK\n";
     String defaultContentType = "Content-Type: text/html;\n";
     String delimiter = "\r\n";
     void write(String s) throws Exception;
}
