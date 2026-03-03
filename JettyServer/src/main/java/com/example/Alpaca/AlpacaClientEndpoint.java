package com.example.Alpaca;

import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.List;

import org.eclipse.jetty.websocket.api.Callback;
import org.eclipse.jetty.websocket.api.Session;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketClose;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketError;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketMessage;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketOpen;
import org.eclipse.jetty.websocket.api.annotations.WebSocket;

import com.example.Contracts.iWebSocketListener;


/**
 * WebSocket client endpoint for handling Alpaca market data stream events.
 * Receives and processes real-time trades, quotes, and bars data.
 */
@WebSocket(autoDemand = true)
public class AlpacaClientEndpoint {

    private List<iWebSocketListener> listeners = new ArrayList<>();

    @OnWebSocketOpen
    public void onOpen(Session session) {
        System.out.println("Connected to Alpaca WebSocket");
    }

    @OnWebSocketMessage
    public void onTextMessage(Session session, String message) {
        // Notify all registered listeners
        for (iWebSocketListener listener : listeners) {
            listener.onMessage(message);
        }
    }

    @OnWebSocketMessage
    public void onBinaryMessage(Session session, ByteBuffer payload, Callback callback) {
        
    }

    @OnWebSocketClose
    public void onClose(Session session) {
        System.out.println("Disconnected from Alpaca WebSocket");
    }

    @OnWebSocketError
    public void onError(Throwable throwable) {
        System.err.println("WebSocket error: " + throwable.getMessage());
        throwable.printStackTrace();
    }

    public void registerListener(iWebSocketListener listener) {
        listeners.add(listener);
    }

    public void removeListener(iWebSocketListener listener) {
        listeners.remove(listener);
    }

}
