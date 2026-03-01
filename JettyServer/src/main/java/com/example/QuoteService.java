package com.example;

import com.example.Contracts.iQuoteService;
import com.example.Domain.Quote;
import com.example.Providers.iQuoteProvider;

public class QuoteService implements iQuoteService {

    private final iQuoteProvider provider;

    public QuoteService(iQuoteProvider provider) {
        this.provider = provider;
    }

    @Override
    public Quote fetchQuote(String ticker) {
        return provider.getQuote(ticker);
    }
}