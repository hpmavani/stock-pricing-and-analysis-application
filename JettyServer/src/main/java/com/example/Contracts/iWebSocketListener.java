package com.example.Contracts;

/**
 * Listener interface for receiving WebSocket messages from a third-party stream.
 */
public interface iWebSocketListener {
    /**
     * Called when a message is received from the WebSocket.
     *
     * @param message the raw JSON message
     */
    void onMessage(String message);
}
