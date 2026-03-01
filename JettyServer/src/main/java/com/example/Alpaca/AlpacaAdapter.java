package com.example.Alpaca;

import java.util.List;

import com.example.DTOs.AlpacaQuote;
import com.example.Domain.Quote;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class AlpacaAdapter {

    private ObjectMapper om;

    public AlpacaAdapter() {
        om = new ObjectMapper();
    }

    public Quote toQuote(String quoteJson) {
        List<AlpacaQuote> alpacaQuotes;

        try {
            alpacaQuotes = this.om.readValue(quoteJson, new TypeReference<List<AlpacaQuote>>(){});
        } catch (JsonMappingException me) {
            System.out.println("Could not map quote json to Alpaca Quote: " + me.toString());
            return null; //TODO: should raise
        } catch (JsonProcessingException je) {
            System.out.println("Could not process quote json: " + je.toString());
            return null; //TODO: should raise
        }

        
        Quote quote = new Quote(
            alpacaQuotes.get(0).getSymbol(),
            alpacaQuotes.get(0).getAskPrice(),
            alpacaQuotes.get(0).getBidPrice()
        );
    

        return quote;
    }
}
