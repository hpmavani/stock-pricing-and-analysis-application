package com.example.Domain;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

public class MarketDataSubscriber {

    private Map<String, List<String>> subscriptions;
    private ObjectMapper om = new ObjectMapper();

    /* default constructor */
    public MarketDataSubscriber() {
        subscriptions.put("quotes", new ArrayList<String>());
        subscriptions.put("trades", new ArrayList<String>());
    }

    public MarketDataSubscriber(List<String> quotes, List<String> trades) {
        subscriptions.put("quotes", quotes);
        subscriptions.put("trades", trades);
    }

    public Map<String, List<String>> getSubscriptions() {
        return subscriptions;
    }

    public void setSubscriptions(Map<String, List<String>> subscriptions) {
        this.subscriptions = subscriptions;
    }

    public void processSubscription(JsonNode message) {

        TypeReference<Map<String, List<String>>> typeReferenceMap = new TypeReference<Map<String, List<String>>>() {};
        Map<String, List<String>> subscriptions = om.convertValue(message, typeReferenceMap);

        // Adds quotes & trades to existing subscriptions
        this.subscriptions.get("quotes").addAll(subscriptions.get("quotes"));
        this.subscriptions.get("trades").addAll(subscriptions.get("trades"));
    
    }

    public void processUnsubscription(JsonNode message) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'processUnsubscription'");
    }

}
