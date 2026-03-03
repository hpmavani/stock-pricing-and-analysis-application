package com.example.Domain;

import java.util.Objects;

public class Quote extends MarketEvent {
	
	private String eventType;
	private String ticker;
	private double askPrice;
	private double bidPrice;

	public Quote() {
	}

	public Quote(String ticker, double askPrice, double bidPrice) {
		this.ticker = ticker;
		this.askPrice = askPrice;
		this.bidPrice = bidPrice;
	}

	public String getEventType() {
        return eventType;
    }

    public void setEventType(String eventType) {
        this.eventType = eventType;
    }
	
	public String getTicker() {
		return ticker;
	}

	public void setTicker(String ticker) {
		this.ticker = ticker;
	}

	public double getAskPrice() {
		return askPrice;
	}

	public void setAskPrice(double askPrice) {
		this.askPrice = askPrice;
	}

	public double getBidPrice() {
		return bidPrice;
	}

	public void setBidPrice(double bidPrice) {
		this.bidPrice = bidPrice;
	}

	@Override
	public String toString() {
		return "Quote{" +
				"ticker='" + ticker + '\'' +
				", askPrice=" + askPrice +
				", bidPrice=" + bidPrice +
				'}';
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		Quote quote = (Quote) o;
		return Double.compare(quote.askPrice, askPrice) == 0 &&
				Double.compare(quote.bidPrice, bidPrice) == 0 &&
				Objects.equals(ticker, quote.ticker);
	}

	@Override
	public int hashCode() {
		return Objects.hash(ticker, askPrice, bidPrice);
	}
}
