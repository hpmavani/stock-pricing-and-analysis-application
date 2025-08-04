package com.example;

import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.ServerConnector;
import org.eclipse.jetty.server.Connector;
import org.eclipse.jetty.ee10.servlet.ServletContextHandler;
import org.eclipse.jetty.ee10.webapp.WebAppContext;
import org.eclipse.jetty.ee10.websocket.jakarta.server.config.JakartaWebSocketServletContainerInitializer;
import org.eclipse.jetty.http.HttpHeader;
import org.eclipse.jetty.io.Content;
import org.eclipse.jetty.server.Handler;
import org.eclipse.jetty.server.Request;
import org.eclipse.jetty.server.Response;
import org.eclipse.jetty.util.Callback;
import org.eclipse.jetty.util.thread.QueuedThreadPool;

import jakarta.websocket.server.ServerEndpointConfig;

public class SimplestServer
{
    /*@Override
    public boolean handle(Request request, Response response, Callback callback)
    {
        if(request.getHttpURI().asString().endsWith("/stocks")) {
            System.out.println("Stocks requested");
            response.setStatus(200);
            response.getHeaders().put(HttpHeader.CONTENT_TYPE, "application/json; charset=UTF-8");
            Content.Sink.write(response, true, "{stocks: nvidia}", callback);
        } else {
            System.out.println("Home Screen Requested");
            response.setStatus(200);
            response.getHeaders().put(HttpHeader.CONTENT_TYPE, "text/html; charset=UTF-8");
            Content.Sink.write(response, true, "<!DOCTYPEHTML><html><body><h1>Dashboard</h1></body></html>", callback);
        }

        return true;
    }*/

        //QueuedThreadPool threadPool = new QueuedThreadPool();
    public static Server newServer(int port) 
    {
        Server server = new Server(port);
        //WebAppContext handler
        ServletContextHandler handler = new ServletContextHandler("/ctx");

        server.setHandler(handler);

        JakartaWebSocketServletContainerInitializer.configure(handler, (servletContext, container) -> 
        {
           
            container.setDefaultMaxTextMessageBufferSize(128 * 1024);
            container.addEndpoint(MyEndpoint.class);

        });

        // connectors = component that accepts client connections
        //Connector connector = new ServerConnector(server);
        //server.addConnector(connector);
        return server;
    }
    
    public static void main(String[] args) {
        Server server = newServer(8080);
        try {
            server.start();
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        
    }
    
}