package com.example;

import java.util.List;

import org.eclipse.jetty.ee11.servlet.ServletContextHandler;
import org.eclipse.jetty.ee11.webapp.WebAppContext;
import org.eclipse.jetty.ee11.websocket.jakarta.server.config.JakartaWebSocketServletContainerInitializer;
import org.eclipse.jetty.server.Server;

import com.example.Domain.MarketEvent;

/* Singleton */

public class MarketDataServer {

    private Server server;
    private ServletContextHandler handler;
    private static MarketDataServer instance;
    private static int port = 8080;

    public MarketDataServer() {
        this.server = new Server(port);

        this.handler = new ServletContextHandler();
        server.setHandler(handler);

        // Setup the ServerContainer and the WebSocket endpoints for this web application context.
        JakartaWebSocketServletContainerInitializer.configure(handler, (servletContext, container) ->
        {
            // Configure the ServerContainer.
            container.setDefaultMaxTextMessageBufferSize(128 * 1024);

            // Simple registration of your WebSocket endpoints.
            container.addEndpoint(MarketDataServerEndpoint.class);
        });
    }

    public static MarketDataServer getInstance() throws Exception {
        if(instance == null) {
            instance = new MarketDataServer();
        }
        return instance;
    }

    public void start() {
        try {
            this.server.start();
        } catch(Exception e) {
            System.out.println("Failed to start server: " + e.toString());
        }
    }

    public void broadcast(MarketEvent event) {
        MarketDataServerEndpoint.broadcast(event);
    }
}
