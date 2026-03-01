package com.example.Providers;

import com.example.Domain.Quote;

public class QuoteProvider implements iQuoteProvider {

    @Override
    public Quote getQuote(String ticker) {
        // simple placeholder implementation
        return new Quote(ticker, 0.0, 0.0);
    }
}