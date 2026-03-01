package com.example.Contracts;

import com.example.Domain.Quote;

public interface iQuoteService {
    /**
     * Fetch a quote via the underlying provider.
     */
    Quote fetchQuote(String ticker);
}