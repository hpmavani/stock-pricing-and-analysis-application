package com.example.Alpaca.adapters;

import java.util.List;

import com.example.DTOs.AlpacaQuote;
import com.example.DTOs.AlpacaTrade;
import com.example.Domain.Quote;
import com.example.Domain.Trade;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class AlpacaTradeAdapter {
    private ObjectMapper om;

    public AlpacaTradeAdapter() {
        om = new ObjectMapper();
    }

    public Trade toTrade(String tradeJson) {

        AlpacaTrade alpacaTrade;

        try {
            alpacaTrade = this.om.readValue(tradeJson, AlpacaTrade.class);
        } catch (JsonMappingException me) {
            System.out.println("Could not map trade json to Alpaca Trade: " + me.toString());
            return null; //TODO: should raise
        } catch (JsonProcessingException je) {
            System.out.println("Could not process trade json: " + je.toString());
            return null; //TODO: should raise
        }

        
        Trade trade = new Trade(
            alpacaTrade.getSymbol(),
            alpacaTrade.getPrice(),
            alpacaTrade.getSize(),
            alpacaTrade.getTime()
        );
    

        return trade;
    }
}
