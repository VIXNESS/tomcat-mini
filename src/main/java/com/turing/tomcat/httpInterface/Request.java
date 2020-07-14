package com.turing.tomcat.httpInterface;

import java.io.InputStream;

public interface Request {
    String getUrl();
    String getMethod();
    int defaultBufferLength = 1024;
}
