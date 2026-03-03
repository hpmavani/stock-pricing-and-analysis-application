package com.example;

import java.util.ArrayList;
import java.util.List;

import com.example.Contracts.iQuoteListener;
import com.example.Contracts.iQuoteService;
import com.example.Contracts.iTradeListener;
import com.example.Contracts.iTradeService;
import com.example.Contracts.iWebSocketListener;

public abstract class MarketDataService implements iQuoteService, iTradeService, iWebSocketListener {

    private List<iQuoteListener> quoteListeners = new ArrayList<iQuoteListener>();
    private List<iTradeListener> tradeListeners = new ArrayList<iTradeListener>();
    
    public void addQuoteListener(iQuoteListener listener) {
        quoteListeners.add(listener);
    }

    public void addTradeListener(iTradeListener listener) {
        tradeListeners.add(listener);
    }

    protected List<iQuoteListener> getQuoteListeners() {
        return quoteListeners;
    }

    protected List<iTradeListener> getTradeListeners() {
        return tradeListeners;
    }

    public abstract void onQuote(String quote);

    public abstract void onTrade(String trade);

}
