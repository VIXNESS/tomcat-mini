package com.turing.tomcat.utils;

import com.turing.tomcat.httpInterface.Servlet;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public enum ServletMappingPool {
    INSTANCE;
    private Map<String, Servlet> servletMap = new ConcurrentHashMap<>();

    public Map<String, Servlet> getServletMap() {
        return servletMap;
    }

    public static ServletMappingPool getInstance(){
        return INSTANCE;
    }

}
