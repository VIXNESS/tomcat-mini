package com.turing.tomcat.httpAIOImpl;

import com.turing.tomcat.httpInterface.AbstractTomcat;
import com.turing.tomcat.httpInterface.Request;
import com.turing.tomcat.httpInterface.Response;
import com.turing.tomcat.httpInterface.Tomcat;
import com.turing.tomcat.utils.PropertyMappingFactoryInf;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.net.InetSocketAddress;

import java.nio.ByteBuffer;
import java.nio.channels.*;
import java.nio.charset.Charset;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


public class AIOTomcat extends AbstractTomcat implements AutoCloseable {
    private final Logger logger = LogManager.getLogger();

    final ExecutorService executors = Executors.newCachedThreadPool();
    final AsynchronousChannelGroup channelGroup = AsynchronousChannelGroup.withCachedThreadPool(executors, 1);
    final AsynchronousServerSocketChannel socket = AsynchronousServerSocketChannel.open(channelGroup);

    public AIOTomcat(PropertyMappingFactoryInf propertyMappingFactory, int port) throws IOException {
        this.servletMapping = propertyMappingFactory.getPropertyMapping();
        this.port = port;
    }

    @Override
    public void start() throws Exception {
        init();


        socket.bind(new InetSocketAddress(this.port));
        logger.info("AIO Tomcat is running, port: " + this.port);
//        StringBuilder requestContentBuilder = new StringBuilder();
        socket.accept(null, new CompletionHandler<>() {
            private final ByteBuffer byteBuffer = ByteBuffer.allocate(512);

            @Override
            public void completed(AsynchronousSocketChannel channel, Object attachment) {
                StringBuilder requestBuilder = new StringBuilder();
                byteBuffer.clear();
                try {
                    while (channel.read(byteBuffer).get() > 0){
                        byteBuffer.flip();
                        requestBuilder.append(Charset.defaultCharset().decode(byteBuffer));
                        byteBuffer.clear();
                        if(requestBuilder.toString().endsWith("")) break;
                    }
//                    logger.debug(requestBuilder.toString());
                    final Request request = new AIORequest(requestBuilder.toString());
                    final Response response = new AIOResponse(channel);
                    final String url = request.getUrl();
                    if (url != null && servletMapping.containsKey(url)) {
                        servletMapping.get(url).service(request, response);
                    } else {
                        response.write("404 - not found.");
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    try {
                        channel.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    } finally {
                        socket.accept(null, this);
                    }
                }

            }

            @Override
            public void failed(Throwable exc, Object attachment) {

            }
        });


    }

    @Override
    public void close() throws Exception {
        socket.close();
        executors.shutdown();
    }
//    public class TomcatHandler implements CompletionHandler<AsynchronousSocketChannel, StringBuilder>{
//        private final ByteBuffer byteBuffer = ByteBuffer.allocate(128);
//        private StringBuilder requestContentBuilder;
//
//        @Override
//        public void completed(AsynchronousSocketChannel channel, StringBuilder obj) {
//            try {
//                byteBuffer.clear();
//                channel.read(byteBuffer, requestContentBuilder, new CompletionHandler<>() {
//                    @Override
//                    public void completed(Integer result, StringBuilder attachment) {
//                        if (result < 0) return;
//                        byteBuffer.flip();
//                        requestContentBuilder.append(Charset.defaultCharset().decode(byteBuffer));
//                        byteBuffer.clear();
//                        channel.read(byteBuffer, attachment, this);
//                    }
//
//                    @Override
//                    public void failed(Throwable exc, StringBuilder object) {
//
//                    }
//                });
//
//                logger.debug(requestContentBuilder.toString());
//                final Request request = new AIORequest(requestContentBuilder.toString());
//                final Response response = new AIOResponse(channel);
//                final String url = request.getUrl();
//                requestContentBuilder = new StringBuilder();
//                if(url != null && servletMapping.containsKey(url)){
//                    servletMapping.get(url).service(request, response);
//                }else {
//                    response.write("404 - not found.");
//                }
//
//            } catch (Exception e) {
//                e.printStackTrace();
//            } finally {
//                try {
//                    channel.close();
//                } catch (IOException e) {
//                    e.printStackTrace();
//                } finally {
//                    socket.accept(requestContentBuilder, this);
//                }
//            }
//        }
//
//        @Override
//        public void failed(Throwable exc, StringBuilder attachment) {
//
//        }
//    }
}

