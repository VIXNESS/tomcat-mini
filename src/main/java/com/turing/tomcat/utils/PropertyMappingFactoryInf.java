package com.turing.tomcat.utils;

import com.turing.tomcat.httpInterface.Servlet;

import java.util.Map;

public interface PropertyMappingFactoryInf {
    enum MapType{
        Concurrency,
        Enum
    }
    Map<String, Servlet> getPropertyMapping();
}
