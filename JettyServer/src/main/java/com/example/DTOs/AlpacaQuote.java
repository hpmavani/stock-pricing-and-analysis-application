package com.example.DTOs;

import java.time.Instant;
import java.util.List;
import java.util.Objects;

import com.fasterxml.jackson.annotation.JsonProperty;

public class AlpacaQuote {

    @JsonProperty
    private String T;       // quote type

    @JsonProperty
    private String S;       // symbol

    @JsonProperty
    private String bx;      // bid exchange

    @JsonProperty
    private double bp;      // bid price

    @JsonProperty
    private int bs;         // bid size

    @JsonProperty
    private String ax;      // ask exchange

    @JsonProperty
    private double ap;      // ask price

    @JsonProperty
    private int as;         // ask size

    @JsonProperty
    private List<String> c; // conditions

    @JsonProperty
    private String z;       // tape

    @JsonProperty
    private String t;      // timestamp

    public String getSymbol() {
        return this.S;
    }

    public double getBidPrice() {
        return this.bp;
    }

    public double getAskPrice() {
        return this.ap;
    }

    @Override
    public String toString() {
        return "AlpacaQuote{" +
                "T='" + T + '\'' +
                ", S='" + S + '\'' +
                ", bx='" + bx + '\'' +
                ", bp=" + bp +
                ", bs=" + bs +
                ", ax='" + ax + '\'' +
                ", ap=" + ap +
                ", as=" + as +
                ", c=" + c +
                ", z='" + z + '\'' +
                ", t=" + t +
                '}';
    }

}
