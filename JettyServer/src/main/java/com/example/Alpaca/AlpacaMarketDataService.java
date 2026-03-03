package com.example.Alpaca;

import java.util.List;

import com.example.MarketDataServer;
import com.example.Alpaca.adapters.AlpacaQuoteAdapter;
import com.example.Alpaca.adapters.AlpacaTradeAdapter;
import com.example.Contracts.WebSocketMessageListener;
import com.example.Contracts.iQuoteService;
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
public class AlpacaMarketDataService implements WebSocketMessageListener, iQuoteService, iTradeService {

    private final AlpacaClient client;
    private final AlpacaQuoteAdapter quoteAdapter;
    private final ObjectMapper objectMapper;
    private final AlpacaTradeAdapter tradeAdapter;

    private MarketDataServer server;

    public AlpacaMarketDataService(AlpacaClient client, AlpacaQuoteAdapter quoteAdapter, 
                                    AlpacaTradeAdapter tradeAdapter, MarketDataServer server) {
        this.client = client;
        this.quoteAdapter = quoteAdapter;
        this.tradeAdapter = tradeAdapter;
        this.server = server;
        this.objectMapper = new ObjectMapper();
    }

    @Override
    public void onMessage(String message) {
        try {
            List<JsonNode> events = objectMapper.readValue(message, new TypeReference<List<JsonNode>>() {});
            for (JsonNode event : events) {
                String type = event.get("T").asText();
                switch (type) {
                    case "t" -> processTrade(event.toString());
                    case "q" -> processQuote(event.toString());
                }
            }
        } catch (JsonProcessingException e) {
            System.out.println("Could not process json: " + e);
        }
        
    }

    @Override
    public Quote processQuote(String quote) {
        Quote q = this.quoteAdapter.toQuote(quote);
        // converts to quote using adapter
        server.broadcast(q);
        return q;
    }
    @Override
    public Trade processTrade(String trade) {
        Trade t = this.tradeAdapter.toTrade(trade);
        // converts to quote using adapter
        
        server.broadcast(t);
        return t;
    }

}