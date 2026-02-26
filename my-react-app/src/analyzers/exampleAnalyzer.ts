import type { Price } from "../context/MarketContext";

/**
 * Example analyzer stub. Accepts price history, returns insights.
 * To add new algorithms, create new files and plug into AnalysisPanel.
 */
export function exampleAnalyzer(prices: Record<string, number[]>): { symbol: string; insight: string }[] {
  // Simple moving average stub
  const symbols = Object.keys(prices);
  return symbols.map((symbol) => ({
    symbol,
    insight: `Moving Avg: ${(
      prices[symbol].reduce((a, b) => a + b, 0) / prices[symbol].length
    ).toFixed(2)}`,
  }));
}
