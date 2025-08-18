package com.example;

import java.util.function.Consumer;

import org.eclipse.jetty.websocket.api.Callback;
import org.eclipse.jetty.websocket.api.Session;
import org.eclipse.jetty.websocket.api.annotations.WebSocket;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import com.example.MyEndpoint;



@WebSocket(autoDemand = true)
public class AlpacaEndpoint implements Session.Listener {
    private Session session;
    private static final Logger LOG = LoggerFactory.getLogger(MyEndpoint.class);
    private ObjectMapper mapper = new ObjectMapper();
    private static String auth = "{\"action\":\"auth\"," + "\"key\":\"" + System.getenv("ALPACA_API_KEY") + "\",\"secret\":\"" + System.getenv("ALPACA_SECRET") + "\"}";
    private static String subscribe = "{\"action\":\"subscribe\",\"trades\":[\"FAKEPACA\"],\"quotes\":[\"FAKEPACA\", \"AMD\"]}";    
    private Consumer<String> messageHandler;
          
    public AlpacaEndpoint(Consumer<String> messageHandler) {
        this.messageHandler = messageHandler;
    }

    @Override
    public void onWebSocketOpen(Session session) {
        // send an authentication and then demand
        System.out.println("Trying to connect to websocket");
        this.session = session;
        session.demand();
    }

    @Override
    public void onWebSocketText(String message) {
        LOG.info("Received data", message);
        System.out.println(message);
        try {
            JsonNode root = mapper.readTree(message);
            if (root.findValue("msg") != null) {
                System.out.println("Initial msg");
                for(JsonNode node : root) {
                    String msg = node.path("msg").asText();
                    System.out.println(msg);
                    if(msg.equals("connected")) {
                        session.sendText(auth, Callback.from(session :: demand, Throwable :: printStackTrace));
                    } else if (msg.equals("authenticated")) {
                        System.out.println("Subscribing");
                        session.sendText(subscribe, Callback.from(session :: demand, Throwable :: printStackTrace));
                    } 
                }
            }
            else {
                // no message -> trades or quotes
                for (JsonNode node : root) {
                    String type = node.path("T").asText();
                    if (type.equals("q")) {
                        System.out.println("Quote received"); 
                        this.messageHandler.accept(node.path("bp").asText());
                    } else if (type.equals("t")) {
                        System.out.println("Trade received");
                    } else {
                        System.out.println(type);
                    }
                }
                session.demand();
            }
        } catch (JsonMappingException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (JsonProcessingException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    @Override
    public void onWebSocketError(Throwable cause)
    {
        // The WebSocket endpoint failed.

        // You may log the error.
        cause.printStackTrace();

    }

    @Override
    public void onWebSocketClose(int statusCode, String reason)
    {
        // The WebSocket endpoint has been closed.
        System.out.println("Websocket closed");
    }
    
}

