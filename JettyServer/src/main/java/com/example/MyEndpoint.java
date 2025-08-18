package com.example;

import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import jakarta.websocket.CloseReason;
import jakarta.websocket.Endpoint;
import jakarta.websocket.EndpointConfig;
import jakarta.websocket.MessageHandler;
import jakarta.websocket.RemoteEndpoint;
import jakarta.websocket.server.ServerEndpoint;

import java.net.URI;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import org.eclipse.jetty.client.HttpClient;
import org.eclipse.jetty.util.Callback;
import org.eclipse.jetty.websocket.api.Session;
import org.eclipse.jetty.websocket.api.annotations.WebSocket;
import org.eclipse.jetty.websocket.client.WebSocketClient;
import org.slf4j.Logger;

import java.util.concurrent.CompletableFuture;

@ServerEndpoint("/ws")
public class MyEndpoint implements Session.Listener {
    
    private static final Logger LOG = LoggerFactory.getLogger(MyEndpoint.class);
    private Session session;
    private static final Map<Session, Set<String>> subscriptions = new ConcurrentHashMap<>();
    private ObjectMapper mapper = new ObjectMapper();
    private HttpClient httpClient = new HttpClient(); 
    private WebSocketClient websocketClient = new WebSocketClient(httpClient); 
    
    @Override
    public void onWebSocketOpen(Session session) {
        this.session = session;
        LOG.info("WebSocket Open: {}", session);
        System.out.println("connected to ws.");
    }

    @Override
    public void onWebSocketClose(int statusCode, String reason) {
        System.out.println("WebSocket closed.");
    }

    @Override
    public void onWebSocketText(String message) {
        // If message is a subscription
        //LOG.info("Echoing back text message [{}]", message);
        try {
            JsonNode root = mapper.readTree(message);
            //String tickers = root.path("tickers").asText();

            if (root.get("type").asText().equals("subscription")) {
                String tickers = root.get("tickers").asText();
                System.out.println("Subscription request for " + tickers);
                Set<String> ticker_set = new HashSet<String>();
                ticker_set.add("AMD");
                ticker_set.add("FAKEPACA");
                subscriptions.put(this.session, ticker_set); 

                String response = "{\"status\": \"success\", \"tickers\": \"" + tickers + "\"}";

            }
            //String tickers = "[AMD]";

            //alpaca_connection.addMessageHandler(new MessageHandler());
            //this.remote.sendText(alpaca_result);
            
        } catch (JsonMappingException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (JsonProcessingException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        
    }

    @Override
    public void onWebSocketError(Throwable cause) {
        LOG.warn("WebSocket Error", cause);
    }

    public static void success() {
        System.out.println("Succeeded");
    }

    public static void broadcastQuote(String ticker, String openPrice) {
        String json = "{\"ticker\":" + ticker + ", \"open\":" + openPrice + "}";
        System.out.println(json);
        for(Session session : subscriptions.keySet()) {
            if (subscriptions.get(session).contains(ticker)) {                
                //session.sendText(json, Callback.from(success()));
                //TO DO: Test with client, actually send text data
                
            }
        }
    }
}
