package com.example.Alpaca;

import com.example.Contracts.WebSocketMessageListener;
import com.example.Domain.Quote;
import com.example.Providers.iQuoteProvider;

/**
 * Service that fetches quotes from the Alpaca API via the AlpacaClient.
 */
public class AlpacaMarketDataService implements WebSocketMessageListener {

    private final AlpacaClient client;
    private final AlpacaAdapter adapter;

    public AlpacaMarketDataService(AlpacaClient client, AlpacaAdapter adapter) {
        this.client = client;
        this.adapter = adapter;
    }

    @Override
    public void onMessage(String message) {
        Quote q = this.adapter.toQuote(message);
        // converts to quote using adapter
        System.out.println("Received: " + q);
    }
}