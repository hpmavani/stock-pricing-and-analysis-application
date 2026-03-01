package com.example;

import java.util.ArrayList;

import com.example.Alpaca.AlpacaClient;
import com.example.Alpaca.AlpacaMarketDataService;
import com.example.Alpaca.adapters.AlpacaQuoteAdapter;
import com.example.Alpaca.adapters.AlpacaTradeAdapter;
import com.example.Contracts.WebSocketMessageListener;

public class Main {
    
    public static void main(String[] args) {

        AlpacaClient ac;
        WebSocketMessageListener mdService;
        AlpacaQuoteAdapter qAdapter;
        AlpacaTradeAdapter tAdapter;
        
        try {
            
            ac = new AlpacaClient();
            
        } catch (Exception e) {
            System.out.println("Could not instantiate client" + e.toString());
            ac = null;
        }
        
        try {
            ac.connectWebSocket();
            ac.authenticate();
            
            ArrayList<String> tickers = new ArrayList<String>();
            tickers.add("FAKEPACA");
            ac.subscribe(tickers); 

            System.out.println("Connected: " + ac.isConnected());

            qAdapter = new AlpacaQuoteAdapter();
            tAdapter = new AlpacaTradeAdapter();
            mdService = new AlpacaMarketDataService(ac, qAdapter, tAdapter);
            ac.addListener(mdService);

        }
        catch(Exception e) {
            System.out.println("Could not connect to websocket: " + e.toString());
        }
    }
}
