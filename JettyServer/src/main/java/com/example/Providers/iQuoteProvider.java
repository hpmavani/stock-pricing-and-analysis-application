package com.example.Providers;

import com.example.Domain.Quote;

public interface iQuoteProvider {
    /**
     * Retrieve the current quote for the given ticker symbol.
     */
    Quote getQuote(String ticker);
}