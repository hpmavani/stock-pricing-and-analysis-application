import { useMarket } from "../context/MarketContext";
import { exampleAnalyzer } from "../analyzers/exampleAnalyzer";
export default function AnalysisPanel() {
  const { state } = useMarket();
  // Simulate price history for analyzer
  const priceHistory = {};
  state.assets.forEach(asset => {
    priceHistory[asset.symbol] = Array(10).fill(0).map(() => Math.random() * 1000);
  });
  const insights = exampleAnalyzer(priceHistory);
  return (
    <section className="bg-gray-900 text-gray-100 p-4 rounded mt-4">
      <h2 className="font-semibold mb-2">Analysis</h2>
      {/* To add new analysis algorithms, import and use them here */}
      <ul>
        {insights.map(i => (
          <li key={i.symbol}>{i.symbol}: {i.insight}</li>
        ))}
      </ul>
      {/* To add new backend endpoints, update marketDataService.js */}
    </section>
  );
}