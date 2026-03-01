package com.example.Contracts;

/**
 * Listener interface for receiving WebSocket messages from Alpaca.
 */
public interface WebSocketMessageListener {
    /**
     * Called when a message is received from the Alpaca WebSocket.
     *
     * @param message the raw JSON message
     */
    void onMessage(String message);
}
