package com.turing.tomcat.httpInterface;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.FileInputStream;
import java.util.Map;
import java.util.Properties;

public abstract class AbstractTomcat implements Tomcat {

    private final Logger logger = LogManager.getLogger();
    protected Map<String, Servlet> servletMapping;
    protected final Properties webProperties = new Properties();
    protected int port = defaultPort;

    protected void init(){
        final String WEB_INF = this.getClass().getResource("/").getPath();

        try(final FileInputStream fileInputStream = new FileInputStream( WEB_INF + "web.properties")){
            if(webProperties == null) throw  new NullPointerException("webProperties is not initialized.");
            webProperties.load(fileInputStream);
            webProperties.keySet().stream()
                    .map(Object::toString)
                    .filter(i -> i.endsWith(".url"))
                    .forEach(key -> {
                        final String servletName = key.replaceAll("\\.url$", "");
                        final String url = webProperties.getProperty(key);
                        final String className = webProperties.getProperty(servletName + ".className");
                        try {
                            Servlet servlet = (Servlet) Class.forName(className).getDeclaredConstructor().newInstance();
                            servletMapping.put(url, servlet);
                        }catch (Exception e){
                            logger.error(e.getMessage());
                            e.printStackTrace();
                        }
                    });
        }catch (Exception e){
            logger.error(e.getMessage());
            e.printStackTrace();
        }
    }
}
