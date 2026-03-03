package com.example;

import org.eclipse.jetty.ee11.servlet.ServletContextHandler;
import org.eclipse.jetty.ee11.websocket.jakarta.server.config.JakartaWebSocketServletContainerInitializer;
import org.eclipse.jetty.server.Server;

import com.example.Contracts.iQuoteListener;
import com.example.Contracts.iTradeListener;
import com.example.Domain.Quote;
import com.example.Domain.Trade;

/* Singleton */

public class MarketDataServer implements iQuoteListener, iTradeListener{

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

        System.out.println("Configuring market data server...");
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
        System.out.println("Started Market Data Server Successfully!");
    }

    public void onQuote(Quote quote) {
        MarketDataServerEndpoint.broadcast(quote);
    }

    public void onTrade(Trade trade) {
        MarketDataServerEndpoint.broadcast(trade);
    }
}
