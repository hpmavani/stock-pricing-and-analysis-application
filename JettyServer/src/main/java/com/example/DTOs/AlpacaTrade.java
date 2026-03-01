package com.example.DTOs;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

public class AlpacaTrade {
    @JsonProperty
    private String T;       // type

    @JsonProperty
    private String S;       // symbol

    @JsonProperty
    private int i;          // trade index

    @JsonProperty
    private String x;      // exchange code

    @JsonProperty
    private double p;      // price

    @JsonProperty
    private int s;         // trade size

    @JsonProperty
    private String t;      // timestamp

    @JsonProperty
    private List<String> c;      // acondition

    @JsonProperty
    private String z;       // tape

    public String getSymbol() {
        return this.S;
    }

    public double getPrice() {
        return this.p;
    }

    public int getSize() {
        return this.s;
    }

    public String getTime() {
        return this.t;
    }
    
}
