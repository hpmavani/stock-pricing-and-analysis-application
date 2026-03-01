package com.example.Domain;

public class Trade {

    private String ticker;
    private double price;
    private int size;
    private String timestamp;

    public Trade(String ticker, double price, int size, String timestamp) {
        this.ticker = ticker;
        this.price = price;
        this.size = size;
        this.timestamp = timestamp;
    }

    public String getTicker() {
		return ticker;
	}

	public void setTicker(String ticker) {
		this.ticker = ticker;
	}

	public double getPrice() {
		return price;
	}

	public void setPrice(double price) {
		this.price = price;
	}

	public double getSize() {
		return size;
	}

	public void setSize(int size) {
		this.size = size;
	}

    public String getTime() {
        return timestamp;
    }

    public void setTime(String timestamp) {
        this.timestamp = timestamp;
    }

    @Override
	public String toString() {
		return "Trade{" +
				"ticker='" + ticker + '\'' +
				", Price=" + price +
				", Size=" + size +
                ", Timestamp=" + timestamp +
				'}';
	}
}
