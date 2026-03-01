package com.example.Alpaca.adapters;

import java.util.List;

import com.example.DTOs.AlpacaQuote;
import com.example.Domain.Quote;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class AlpacaQuoteAdapter {

    private ObjectMapper om;

    public AlpacaQuoteAdapter() {
        om = new ObjectMapper();
    }

    public Quote toQuote(String quoteJson) {
        AlpacaQuote alpacaQuote;

        try {
            alpacaQuote = this.om.readValue(quoteJson, AlpacaQuote.class);
        } catch (JsonMappingException me) {
            System.out.println("Could not map quote json to Alpaca Quote: " + me.toString());
            return null; //TODO: should raise
        } catch (JsonProcessingException je) {
            System.out.println("Could not process quote json: " + je.toString());
            return null; //TODO: should raise
        }

        
        Quote quote = new Quote(
            alpacaQuote.getSymbol(),
            alpacaQuote.getAskPrice(),
            alpacaQuote.getBidPrice()
        );
    

        return quote;
    }
}
