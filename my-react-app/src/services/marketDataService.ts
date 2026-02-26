import { Asset, Price } from "../context/MarketContext";

export function connectMarketData({
  onPrice,
  onAssets,
}: {
  onPrice: (price: Price) => void;
  onAssets: (assets: Asset[]) => void;
}): WebSocket | null {
  // Simulate asset list (replace with REST fetch if needed)
  const assets: Asset[] = [
    { symbol: "AAPL", name: "Apple Inc.", type: "stock" },
    { symbol: "BTC", name: "Bitcoin", type: "crypto" },
    { symbol: "SPY", name: "S&P 500 ETF", type: "etf" },
    // Add new tradeables here
  ];
  onAssets(assets);

  let ws: WebSocket | null = null;
  try {
    ws = new WebSocket("ws://localhost:8080/market");
    ws.onopen = () => {
      ws?.send(
        JSON.stringify({ type: "subscribe", symbols: assets.map((a) => a.symbol) })
      );
    };
    ws.onmessage = (event) => {
      let data: any;
      try {
        data = JSON.parse(event.data);
      } catch {
        return;
      }
      if (data.symbol) onPrice(data as Price);
    };
    ws.onerror = () => {
      // Fallback to dummy data if needed
      setInterval(() => {
        assets.forEach((asset) => {
          onPrice({
            symbol: asset.symbol,
            price: Number((Math.random() * 1000).toFixed(2)),
            change: Number((Math.random() * 2 - 1).toFixed(2)),
            volume: Math.floor(Math.random() * 10000),
          });
        });
      }, 2000);
    };
  } catch {
    ws = null;
  }
  return ws;
}
