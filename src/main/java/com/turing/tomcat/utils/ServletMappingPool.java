package com.turing.tomcat.utils;

import com.turing.tomcat.httpBIOImpl.AbstractServlet;
import com.turing.tomcat.httpInterface.Servlet;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public enum ServletMappingPool {
    INSTANCE;
    private Map<String, AbstractServlet> servletMap = new ConcurrentHashMap<>();

    public Map<String, AbstractServlet> getServletMap() {
        return servletMap;
    }

    public static ServletMappingPool getInstance(){
        return INSTANCE;
    }

}
