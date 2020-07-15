package com.turing.tomcat;

import com.turing.tomcat.httpInterface.Tomcat;
import com.turing.tomcat.utils.PropertyMappingFactoryInf;

import java.util.Map;

public class TomcatFactory {
    public final static Tomcat getTomcat(Class<? extends Tomcat> tomcatClass, PropertyMappingFactoryInf factoryInf, int port) throws Exception{
       return tomcatClass.getDeclaredConstructor(PropertyMappingFactoryInf.class, int.class).newInstance(factoryInf, port);
    }
}
