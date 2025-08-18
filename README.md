# stock-pricing-and-analysis-application

## Overview
A work-in-progress Java backend that streams real-time stock market data using WebSockets and the Alpaca API, designed to practice low-latency system design and modular backend architecture.

## Features (Work in Progress)
- ✅ WebSocket integration with Alpaca API for live price streaming
- ✅ Basic Jetty 12 WebSocket server
- 🚧 Modularized backend design using OOP and Observer pattern
- 🚧 Client subscription for continuous real-time updates
- 🚧 Future: basic analysis & insights
  
## Prerequisites
* Jetty 12
* Java 8+
* Alpaca API Key & Secret Key

## Getting Started
* Clone the repository
```bash
   git clone https://github.com/yourusername/stock-pricing-and-analysis-application.git
   cd stock-pricing-and-analysis-application
```
* Set environment variables "ALPACA_API_KEY" and "ALPACA_SECRET"
```bash
  export ALPACA_API_KEY=your_api_key
  export ALPACA_SECRET=your_secret
```
* Run the Server <br>
javac MyServer.java <br>
java MyServer

  
