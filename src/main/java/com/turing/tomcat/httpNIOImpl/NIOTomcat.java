package com.turing.tomcat.httpNIOImpl;

import com.turing.tomcat.httpInterface.Request;
import com.turing.tomcat.httpInterface.Response;
import com.turing.tomcat.httpInterface.Servlet;
import com.turing.tomcat.httpInterface.Tomcat;

import com.turing.tomcat.utils.EventLoopGroupAutoCloseable;
import com.turing.tomcat.utils.NioEventLoopGroupAutoCloseable;
import com.turing.tomcat.utils.PropertyMappingFactoryImpl;
import com.turing.tomcat.utils.PropertyMappingFactoryInf;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.*;

import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.http.HttpRequest;
import io.netty.handler.codec.http.HttpRequestDecoder;


import io.netty.handler.codec.http.HttpResponseEncoder;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.FileInputStream;

import java.util.Map;
import java.util.Properties;



public class NIOTomcat implements Tomcat  {
    private final int port;
    private final Map<String, Servlet> servletMapping;
    private Properties webProperties;
    private final Logger logger = LogManager.getLogger();

    public NIOTomcat(){
        this(new PropertyMappingFactoryImpl(PropertyMappingFactoryInf.MapType.Concurrency), defaultPort);
    }

    public NIOTomcat(PropertyMappingFactoryInf mappingFactory, int port){
        webProperties = new Properties();
        this.port = port;
        this.servletMapping = mappingFactory.getPropertyMapping();
    }

    private void init(){
        final String WEB_INF = this.getClass().getResource("/").getPath();
        try(FileInputStream fileInputStream = new FileInputStream(WEB_INF + "web.properties")){
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
    @Override
    public void start() {
        init();
        try(EventLoopGroupAutoCloseable bossGroup =  new NioEventLoopGroupAutoCloseable();
        EventLoopGroupAutoCloseable workerGroup = new NioEventLoopGroupAutoCloseable()){
           ServerBootstrap server = new ServerBootstrap();
           server.group(bossGroup, workerGroup)
                   .channel(NioServerSocketChannel.class)
                   .childHandler(new ChannelInitializer<>() {
                       @Override
                       protected void initChannel(Channel client) throws Exception {
                           client.pipeline()
                                   .addLast(new HttpResponseEncoder())
                                   .addLast(new HttpRequestDecoder())
                                   .addLast(new TomcatHandler());
                       }
                   }).option(ChannelOption.SO_BACKLOG,128)
                   .childOption(ChannelOption.SO_KEEPALIVE, true);
           ChannelFuture future = server.bind(port).sync();
           logger.info("NIO Tomcat is started, post: " + port);
           future.channel().closeFuture().sync();
        }catch (Exception e){
            logger.error(e.getMessage());
            e.printStackTrace();
        }
    }
   public class TomcatHandler extends ChannelInboundHandlerAdapter{

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        if(msg instanceof HttpRequest){
            final HttpRequest req = (HttpRequest) msg;
            final String url = req.uri();
            if(servletMapping.containsKey(url)){
                servletMapping.get(url).service(new NIORequest(ctx, req), new NIOResponse(ctx, req));
            }else {
                new NIOResponse(ctx, req).write("404 - not Found");
            }
        }
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
//        super.exceptionCaught(ctx, cause);
    }

   }
}
