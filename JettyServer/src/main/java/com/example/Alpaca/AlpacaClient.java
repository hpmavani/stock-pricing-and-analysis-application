package com.example.Alpaca;

import com.example.Contracts.iWebSocketListener;
import com.example.Domain.Quote;
import java.net.URI;
import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CountDownLatch;

import org.eclipse.jetty.client.HttpClient;
import org.eclipse.jetty.websocket.api.Callback;
import org.eclipse.jetty.websocket.api.Session;
import org.eclipse.jetty.websocket.api.StatusCode;
import org.eclipse.jetty.websocket.client.WebSocketClient;

/**
 * Alpaca WebSocket and REST client for market data streaming.
 * Handles authentication, subscriptions to trades, quotes, and bars.
 */
public class AlpacaClient {

    private static final String ALPACA_WS_URL = "wss://stream.data.alpaca.markets/v2/test";

    private final String apiKey;
    private final String apiSecret;
    private HttpClient httpClient;
    private WebSocketClient websocketClient;
    private Session session;
    private AlpacaClientEndpoint alpacaEndpoint;

    /**
     * Creates a client with explicit credentials.
     *
     * @param apiKey    Alpaca API key (must not be null/empty)
     * @param apiSecret Alpaca API secret (must not be null/empty)
     */
    public AlpacaClient(String apiKey, String apiSecret) {
        this.apiKey = requireNonNullOrEmpty(apiKey, "apiKey");
        this.apiSecret = requireNonNullOrEmpty(apiSecret, "apiSecret");
    }

    /**
     * Convenience constructor that reads credentials from environment variables
     * ALPACA_API_KEY and ALPACA_API_SECRET. Throws IllegalArgumentException if unset.
     */
    public AlpacaClient() {
        this(
            System.getenv("ALPACA_API_KEY"),
            System.getenv("ALPACA_SECRET")
        );
    }

    private static String requireNonNullOrEmpty(String value, String name) {
        if (value == null || value.isEmpty()) {
            throw new IllegalArgumentException(name + " must be provided");
        }
        return value;
    }

    public String getApiKey() {
        return apiKey;
    }

    public String getApiSecret() {
        return apiSecret;
    }

    /**
     * Establishes a WebSocket connection to Alpaca's market data stream.
     *
     * @throws Exception if connection fails
     */
    public void connectWebSocket() throws Exception {

        //Configure websocket client
        httpClient = new HttpClient();
        websocketClient = new WebSocketClient(httpClient);
        websocketClient.start();


        //Instantiate Alpaca Endpoint
        alpacaEndpoint = new AlpacaClientEndpoint();
        URI serverURI = URI.create(ALPACA_WS_URL);

        //Retrieve and set Websocket session
        CompletableFuture<Session> future = websocketClient.connect(alpacaEndpoint, serverURI);
        this.session = future.get(); //blocking
    }
    
    /**
     * Sends authentication message to Alpaca WebSocket.
     * Must be called after connectWebSocket().
     *
     * @throws Exception if sending fails
     */

    public void sendSync(String message) throws Exception {
        CountDownLatch latch = new CountDownLatch(1);

        session.sendText(message, Callback.from(
            () -> latch.countDown(),
            cause -> latch.countDown()
        )); //TODO: Update Callback

        latch.await(); // waits 1 second for auth

    }

    
    public void authenticate() throws Exception {
        // {"action": "auth", "key": "{KEY_ID}", "secret": "{SECRET}"}
        String authText = "{\"action\": \"auth\", \"key\": \"" + this.apiKey + "\", \"secret\": \"" + this.apiSecret + "\"}";
        System.out.println("Authenticating...");
        try {
            sendSync(authText);
        } catch (Exception e) {
            System.out.println("Failed to authenticate: " + e.toString());
        }
        System.out.println("Authentication to Alpaca successful!");

    }

    /**
     * Subscribes to trades, quotes, and bars for specified tickers.
     *
     * @param tickers     list of trade tickers (e.g., ["AAPL"])
     * @param quoteTickers list of quote tickers (e.g., ["AMD", "CLDR"])
     * @param barTickers   list of bar tickers (e.g., ["*"] for all)
     * @throws Exception if sending subscription fails
     */
    public void subscribe(List<String> tickers) throws Exception {

        System.out.println("Subscribing...");
        StringBuilder sb = new StringBuilder();

        String subscriptionText = "{\"action\": \"subscribe\", \"trades\":[";
        sb.append(subscriptionText);

        for(String ticker: tickers) {
            sb.append("\"" + ticker + "\",");
        }
        sb.deleteCharAt(sb.length() - 1);
        
        sb.append("], \"quotes\":[");

        for(String ticker: tickers) {
            sb.append("\"" + ticker + "\",");
        }
        sb.deleteCharAt(sb.length() - 1);
        sb.append("]}");

        subscriptionText = sb.toString();
        System.out.println("Subscribing with: " + subscriptionText);

        try {
            sendSync(subscriptionText);
        } catch (Exception e) {
            System.out.println("Failed to authenticate: " + e.toString());
        }

        System.out.println("Subscription to Alpaca successful!");

    }

    /**
     * Closes the WebSocket connection.
     *
     * @throws Exception if closing fails
     */
    
    public void closeWebSocket() throws Exception {
        System.out.println("Closing connection to Alpaca...");
        session.close(StatusCode.NORMAL, "End Websocket connection", Callback.NOOP);
    }

    public boolean isConnected() {
        return session.isOpen();
    }

    public void addListener(iWebSocketListener listener) {
        this.alpacaEndpoint.registerListener(listener);
        System.out.println("Alpaca Client added a message listener"); 
    }

    public void removeListener(iWebSocketListener listener) {
        this.alpacaEndpoint.removeListener(listener);
    }
}