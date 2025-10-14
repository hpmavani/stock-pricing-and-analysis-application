package com.example;

import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.ServerConnector;


import org.eclipse.jetty.ee10.servlet.ServletContextHandler;
import org.eclipse.jetty.ee10.websocket.jakarta.server.config.JakartaWebSocketServletContainerInitializer;


public class MyServer
{
    public static void main(String[] args) {
        
        try {

            //Connect to Alpaca Websocket for price updates
            MarketDataStreamer.connectToAlpaca();
            Server server = new Server(8080);

            //Initialize ContextHandler and set it to /ctx
            ServletContextHandler handler = new ServletContextHandler("/ctx");
            server.setHandler(handler);

            //Configure Annotated Server Endpoint
            JakartaWebSocketServletContainerInitializer.configure(handler, (servletContext, container) ->
            {
                // Configure the ServerContainer.
                container.setDefaultMaxTextMessageBufferSize(128 * 1024);
                try {
                    container.addEndpoint(MyEndpoint.class);
                } catch (Exception e) {
                    System.out.println("Could not add endpoint: " + e.toString());
                    e.printStackTrace();
                }
            });

            //Start Server
            server.start();
            
        } catch (Exception e) {
            System.out.println("Could not start server: " + e.toString());
            e.printStackTrace();
        } 

    }
    
}