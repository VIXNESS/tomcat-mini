package com.turing.tomcat.httpInterface;

public abstract class AbstractResponse implements Response {
    protected String makeHttpResponse(final String content){
        final StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(defaultHTTPHeader)
                .append(defaultContentType)
                .append(delimiter)
                .append(content);
        return stringBuilder.toString();
    }
}
