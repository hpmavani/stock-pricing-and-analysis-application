export function exampleAnalyzer(prices) {
  // Simple moving average stub
  const symbols = Object.keys(prices);
  return symbols.map(symbol => ({
    symbol,
    insight: `Moving Avg: ${(prices[symbol].reduce((a, b) => a + b, 0) / prices[symbol].length).toFixed(2)}`,
  }));
}