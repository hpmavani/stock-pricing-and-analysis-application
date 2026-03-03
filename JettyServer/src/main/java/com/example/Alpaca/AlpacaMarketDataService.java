package com.example.Alpaca;

import java.util.ArrayList;
import java.util.List;

import com.example.MarketDataServer;
import com.example.Alpaca.adapters.AlpacaQuoteAdapter;
import com.example.Alpaca.adapters.AlpacaTradeAdapter;
import com.example.Contracts.iWebSocketListener;
import com.example.Contracts.iQuoteListener;
import com.example.Contracts.iQuoteService;
import com.example.Contracts.iTradeListener;
import com.example.Contracts.iTradeService;
import com.example.Domain.MarketDataSubscriber;
import com.example.Domain.Quote;
import com.example.Domain.Trade;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * Service that fetches quotes from the Alpaca API via the AlpacaClient.
 */
public class AlpacaMarketDataService extends MarketDataService {

    private final AlpacaQuoteAdapter quoteAdapter;
    private final ObjectMapper objectMapper;
    private final AlpacaTradeAdapter tradeAdapter;

    public AlpacaMarketDataService(AlpacaQuoteAdapter quoteAdapter, AlpacaTradeAdapter tradeAdapter) {
        this.quoteAdapter = quoteAdapter;
        this.tradeAdapter = tradeAdapter;
        this.objectMapper = new ObjectMapper();
    }

    @Override
    public void onMessage(String message) {
        try {
            List<JsonNode> events = objectMapper.readValue(message, new TypeReference<List<JsonNode>>() {});
            for (JsonNode event : events) {
                String type = event.get("T").asText();
                switch (type) {
                    case "t" -> onTrade(event.toString());
                    case "q" -> onQuote(event.toString());
                }
            }
        } catch (JsonProcessingException e) {
            System.out.println("Could not process json: " + e);
        }
        
    }

    public void onQuote(String quote) {
        Quote q = this.quoteAdapter.toQuote(quote);
        super.getQuoteListeners().forEach((ql) -> {
            ql.onQuote(q);
        });
    }

    public void onTrade(String trade) {
        Trade t = this.tradeAdapter.toTrade(trade);
        super.getTradeListeners().forEach((tl) -> {
            tl.onTrade(t);
        });
    }

    public void addQuoteListener(iQuoteListener ql) {
        super.addQuoteListener(ql);
        System.out.println("Alpaca Service added quote listener");
    }

    public void addTradeListener(iTradeListener tl) {
        super.addTradeListener(tl);
        System.out.println("Alpaca Service added trade listener");
    }
}