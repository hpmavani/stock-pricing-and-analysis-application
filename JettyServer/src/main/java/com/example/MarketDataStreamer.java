package com.example;

import java.net.URI;
import java.util.LinkedList;
import java.util.Queue;

import org.eclipse.jetty.client.HttpClient;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.websocket.api.Session;
import org.eclipse.jetty.websocket.client.WebSocketClient;

/* Listens to Alpaca Websocket endpoint for quote price updates and streams to MyEndpoint for broadcasting */
public class MarketDataStreamer {

    //URI to alpaca websocket endpoint
    private static URI alpacaURI = URI.create("wss://stream.data.alpaca.markets/v2/test");

    /* Connects to Alpaca */
    public static void connectToAlpaca() {
        HttpClient httpClient = new HttpClient();
        WebSocketClient websocketClient = new WebSocketClient(httpClient);

        Queue<String> quotes = new LinkedList<String>();
    
        // TO DO: Look into message queueing
        try {
            websocketClient.start();

            //Create an AlpacaEndpoint instance with a callback that broadcasts price updates to MyEndpoint

            AlpacaEndpoint alpacaEndpoint = new AlpacaEndpoint(message -> {
                MyEndpoint.broadcastQuote("FAKEPACA", message);
            }); 
            
            //Initialize a Session for blocking connection to Alpaca Websocket
            Session alpacaSession = websocketClient.connect(alpacaEndpoint, alpacaURI).get(); 
            
            //.connect() establishes the connection with a promise of returning a session in the future
            //.get() adds an additional non-blocking feature that blocks until session is received
        }
        catch(Exception e) {
            System.out.println("Couldn't establish connection to Alpaca: " + e); 
        } 
    }
}
