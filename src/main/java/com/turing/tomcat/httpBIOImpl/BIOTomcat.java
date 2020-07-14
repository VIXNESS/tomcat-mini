package com.turing.tomcat.httpBIOImpl;


import com.turing.tomcat.httpInterface.Request;
import com.turing.tomcat.httpInterface.Response;
import com.turing.tomcat.httpInterface.Tomcat;
import com.turing.tomcat.utils.ServletMappingPool;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.FileInputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ServerSocket;

import java.net.Socket;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.ConcurrentHashMap;

public class BIOTomcat implements Tomcat {
    protected ServerSocket serverSocket;
    protected Map<String, AbstractServlet> servletMap;
    protected Properties webProperties ;
    protected final Logger logger = LogManager.getLogger();
    protected int port = defaultPort;

    public BIOTomcat(){
//        servletMappingPool = ServletMappingPool.getInstance();
//        servletMap = new ConcurrentHashMap<>();
        servletMap = ServletMappingPool.getInstance().getServletMap();
        webProperties = new Properties();
    }

    public BIOTomcat(int port){
        this();
        this.port = port;
    }
    private void init() {
       String WEB_INF = this.getClass().getResource("/").getPath();
       logger.info(WEB_INF);
       try(FileInputStream fileInputStream = new FileInputStream(WEB_INF + "web.properties")){
          webProperties.load(fileInputStream);
          webProperties.keySet().stream().map(i -> i.toString()).forEach(key -> {
             if(key.endsWith(".url")){
                 String servletName = key.replaceAll("\\.url$", "");
                 String url = webProperties.getProperty(key);
                 String className = webProperties.getProperty(servletName + ".className");

                 try {
                     AbstractServlet servletInstance = (AbstractServlet) Class.forName(className).getDeclaredConstructor().newInstance();
                     servletMap.put(url, servletInstance);
                 }catch (Exception e){
                    logger.fatal(e.getMessage());
                    e.printStackTrace();
                 }

             }
          });
       }catch (Exception e){
           logger.error(e);
           e.printStackTrace();
       }
    }

    @Override
    public void start() {
        init();
        try{
           serverSocket = new ServerSocket(this.port);
           logger.info("Port of Tomcat is started, port is " + this.port);
           while(true){
               Socket client = serverSocket.accept();
               process(client);

           }
        }catch (Exception e){
            e.printStackTrace();
            logger.error(e);
        }
    }

    private void process(Socket client)throws Exception{
       try(InputStream in = client.getInputStream();
           OutputStream out = client.getOutputStream()) {
           Request request = new BIORequest(in);
           Response response = new BIOResponse(out);

           String url = request.getUrl();
           if(url != null && servletMap.containsKey(url)) servletMap.get(url).service(request, response);
           else response.write("404 - Not Found");
       }
        client.close();
    }
}
