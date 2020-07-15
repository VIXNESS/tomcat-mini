package com.turing.tomcat.utils;

import com.turing.tomcat.httpInterface.Servlet;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class PropertyMappingFactoryImpl implements PropertyMappingFactoryInf {
    private MapType type;
    public PropertyMappingFactoryImpl(MapType type){
        this.type = type;
    }
    @Override
    public Map<String, Servlet> getPropertyMapping() {
        if(type == MapType.Concurrency) return new ConcurrentHashMap<>();
        else return ServletMappingPool.getInstance().getServletMap();
    }
}
