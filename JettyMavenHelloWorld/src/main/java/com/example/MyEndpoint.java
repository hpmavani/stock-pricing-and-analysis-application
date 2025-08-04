package com.example;

import org.slf4j.LoggerFactory;

import jakarta.websocket.CloseReason;
import jakarta.websocket.Endpoint;
import jakarta.websocket.EndpointConfig;
import jakarta.websocket.MessageHandler;
import jakarta.websocket.RemoteEndpoint;
import jakarta.websocket.Session;
import jakarta.websocket.server.ServerEndpoint;

import org.slf4j.Logger;

@ServerEndpoint("/ws")
public class MyEndpoint extends Endpoint implements MessageHandler.Whole<String> {
    
    private static final Logger LOG = LoggerFactory.getLogger(MyEndpoint.class);
    private Session session;
    private RemoteEndpoint.Async remote;
    
    @Override
    public void onOpen(Session session, EndpointConfig config) {
        this.session = session;
        this.remote = this.session.getAsyncRemote();
        LOG.info("WebSocket Open: {}", session);

        // attach message handler
        session.addMessageHandler(this);
        this.remote.sendText("You are now connected to " + this.getClass().getName());
        System.out.println("connected to ws.");
    }

    @Override
    public void onClose(Session session, CloseReason close) {
        super.onClose(session, close);
        this.session = null;
        this.remote = null;
        LOG.info("WebSocket Close: {} - {}", close.getCloseCode(), close.getReasonPhrase()); 
    }

    @Override
    public void onMessage(String message) {
        LOG.info("Echoing back text message [{}]", message);
        this.remote.sendText(message);
    }

    @Override
    public void onError(Session session, Throwable cause) {
        super.onError(session, cause);
        LOG.warn("WebSocket Error", cause);
    }
}
