export function connectMarketData({ onPrice, onAssets }) {
  // Simulate asset list (replace with REST fetch if needed)
  const assets = [
    { symbol: "AAPL", name: "Apple Inc.", type: "stock" },
    { symbol: "BTC", name: "Bitcoin", type: "crypto" },
    { symbol: "SPY", name: "S&P 500 ETF", type: "etf" },
    // Add new tradeables here
  ];
  onAssets(assets);

  // Connect to Jetty WebSocket
  const ws = new WebSocket("ws://localhost:8080/ctx/ws");
  ws.onopen = () => {
    ws.send(JSON.stringify({ type: "subscribe", symbols: assets.map(a => a.symbol) }));
  };
  ws.onmessage = (event) => {
    // Example: { symbol, price, change, volume }
    let data;
    try {
      data = JSON.parse(event.data);
    } catch {
      return;
    }
    if (data.symbol) onPrice(data);
  };
  ws.onerror = () => {
    // Fallback to dummy data if needed
    setInterval(() => {
      assets.forEach(asset => {
        onPrice({
          symbol: asset.symbol,
          price: (Math.random() * 1000).toFixed(2),
          change: (Math.random() * 2 - 1).toFixed(2),
          volume: Math.floor(Math.random() * 10000),
        });
      });
    }, 2000);
  };
  return ws;
}