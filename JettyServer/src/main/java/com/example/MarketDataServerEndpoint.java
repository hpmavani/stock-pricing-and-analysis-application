package com.example;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.example.Domain.MarketDataSubscriber;
import com.example.Domain.MarketEvent;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import jakarta.websocket.OnClose;
import jakarta.websocket.OnError;
import jakarta.websocket.OnMessage;
import jakarta.websocket.OnOpen;
import jakarta.websocket.Session;
import jakarta.websocket.server.ServerEndpoint;

@ServerEndpoint("/stream")
public class MarketDataServerEndpoint{

    private static Map<Session, MarketDataSubscriber> clients = new HashMap<Session, MarketDataSubscriber>();
    private final ObjectMapper objectMapper = new ObjectMapper();

    @OnOpen
    public void onOpen(Session session) {
        
    }  

    @OnClose
    public void onClose(Session session) {
        
    }

    @OnError
    public void onError(Session session, Throwable thr) {

    }

    @OnMessage
    public void onMessage(Session session, String message) {

        JsonNode msg_json;

        try {
            msg_json = objectMapper.readTree(message);
        } catch (JsonProcessingException je) { 
            System.out.println("Could not process json: " + je.toString()); 
            msg_json = null;
        }

        if(msg_json != null) {
            String action = msg_json.get("action").asText();
            MarketDataSubscriber client = clients.get(session);

            switch(action) {
                case "subscribe":
                    client.processSubscription(msg_json);
                case "unsubscribe":
                    client.processUnsubscription(msg_json);
            }
        }
        
        
    }

    /* Takes any market event, quote, trade, etc. and broadcasts to subscribed users */

    public static void broadcast(MarketEvent event) {
        System.out.println("Received market event: " + event);

        String eventType = event.getEventType();
        String ticker = event.getTicker();

        clients.forEach((session, client) -> {
            Map<String, List<String>> subscriptions = client.getSubscriptions();
            List<String> tickers = subscriptions.get(eventType);
            if (tickers != null && tickers.size() > 0) {
                if(tickers.contains(ticker)) {
                    session.getAsyncRemote().sendText(event.toString());
                }
            }
        });
    }

}
