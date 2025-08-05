package com.example;

import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.ServerConnector;
import org.eclipse.jetty.server.Connector;

import java.net.URI;
import org.eclipse.jetty.client.HttpClient;
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
import org.eclipse.jetty.websocket.api.Session;
import org.eclipse.jetty.websocket.client.WebSocketClient;
import java.util.concurrent.CompletableFuture;

import jakarta.websocket.server.ServerEndpointConfig;

public class SimplestServer
{
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

        return server;
    }
    
    public static void main(String[] args) {
        HttpClient httpClient = new HttpClient(); 
        WebSocketClient websocketClient = new WebSocketClient(httpClient); 
        
        try {
            websocketClient.start();
            AlpacaEndpoint alpacaEndpoint = new AlpacaEndpoint(); 
            URI serverURI = new URI("wss://stream.data.alpaca.markets/v2/test");

            CompletableFuture<Session> clientSessionPromise = websocketClient.connect(alpacaEndpoint, serverURI);

        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } 

        
        /*Server server = newServer(8080);
        try {
            server.start();
        } catch (Exception e) {
            e.printStackTrace();
        }*/
    }
    
}