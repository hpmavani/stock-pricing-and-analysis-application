package com.example;

import java.util.ArrayList;

import com.example.Alpaca.AlpacaClient;
import com.example.Alpaca.AlpacaMarketDataService;
import com.example.Alpaca.adapters.AlpacaQuoteAdapter;
import com.example.Alpaca.adapters.AlpacaTradeAdapter;

public class Main {
    
    public static void main(String[] args) {

        AlpacaClient ac;
        MarketDataService mdService;
        AlpacaQuoteAdapter qAdapter;
        AlpacaTradeAdapter tAdapter;
        
        try {
            ac = new AlpacaClient();
            
        } catch (Exception e) {
            System.out.println("Could not instantiate client" + e.toString());
            return;
        }
        
        try {

            // Configure Alpaca Client
            ac.connectWebSocket();
            ac.authenticate();

            // Instantiate + Start Server
            MarketDataServer server = MarketDataServer.getInstance();
            server.start();

            // Add Test Tickers
            ArrayList<String> tickers = new ArrayList<String>();
            tickers.add("FAKEPACA");
            ac.subscribe(tickers); 

            System.out.println("Connected: " + ac.isConnected());

            // Instantiate Market Data Service
            qAdapter = new AlpacaQuoteAdapter();
            tAdapter = new AlpacaTradeAdapter();
            mdService = new AlpacaMarketDataService(qAdapter, tAdapter);

            // Add Listeners to Market Data Service
            mdService.addTradeListener(server);
            mdService.addQuoteListener(server);

            // Add Listener to Alpaca Client
            ac.addListener(mdService);

        }
        catch(Exception e) {
            System.out.println("Could not connect to websocket: " + e.toString());
        }
    }
}
