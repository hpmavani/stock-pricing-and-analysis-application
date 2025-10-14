package com.example;

import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import jakarta.websocket.CloseReason;
import jakarta.websocket.EndpointConfig;
import jakarta.websocket.OnClose;
import jakarta.websocket.OnError;
import jakarta.websocket.OnMessage;
import jakarta.websocket.OnOpen;
import jakarta.websocket.Session;
import jakarta.websocket.server.ServerEndpoint;

import java.io.IOException;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import org.slf4j.Logger;

/* Websocket server endpoint for clients to connect to and receive price updates */

@ServerEndpoint("/ws")
public class MyEndpoint {
    
    private static final Logger LOG = LoggerFactory.getLogger(MyEndpoint.class);
    private Session session;

    //Map of sessions to subscribed tickers
    private static final Map<Session, Set<String>> subscriptions = new ConcurrentHashMap<>();
    private ObjectMapper mapper = new ObjectMapper();
    
    //Handles new client connection
    @OnOpen
    public void onOpen(Session session, EndpointConfig conf) {
        //Store session
        this.session = session;
        System.out.println("connected to ws.");
        try {
            session.getBasicRemote().sendText("Connection Success");
        } catch (IOException e) {
            System.out.println("Could not send text" + e);
            e.printStackTrace();
        }
    }

    //Handles closed client connection
    @OnClose
    public void onClose(Session session, CloseReason reason) {
        System.out.println("WebSocket closed.");
    }

    //Handles messages from clients such as subscription requests to different tickers
    @OnMessage
    public void onMessage(Session session, String message) {
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
            } else {
                System.out.println(message);
            }
            //String tickers = "[AMD]";

            //alpaca_connection.addMessageHandler(new MessageHandler());
            //this.remote.sendText(alpaca_result);
            
        } catch (JsonMappingException e) {
            System.out.println("Could not map Json" + e);
            e.printStackTrace();
        } catch (JsonProcessingException e) {
            System.out.println("Could not process Json" + e);
            e.printStackTrace();
        }
        
    }

    @OnError
    public void onError(Throwable cause) {
        //LOG.warn("WebSocket Error", cause);
        System.out.println("Websocket error" + cause);
    }

    // Parse price updates into json string and send updates to all sessions listening to updated ticker
    public static void broadcastQuote(String ticker, String openPrice) {
        String json = "{\"ticker\":" + ticker + ", \"open\":" + openPrice + "}";
        //System.out.println("Broadcasting: " + json);
        for(Session session : subscriptions.keySet()) {
            try {
                if (subscriptions.get(session).contains(ticker)) {         
                    //TO DO: Test with client, actually send text data
                    session.getBasicRemote().sendText(json);
                }
            } catch (Exception e) {
                System.out.println("Could not send update to session" + e);
            }
        }
    }
}
