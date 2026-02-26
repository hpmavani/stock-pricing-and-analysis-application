import { useMarket } from "../context/MarketContext";
import { useEffect, useState } from "react";
export default function PriceTable() {
  const { state } = useMarket();
  const [highlight, setHighlight] = useState({});
  useEffect(() => {
    // Animate price changes
    const timer = setTimeout(() => setHighlight({}), 500);
    return () => clearTimeout(timer);
  }, [state.prices]);
  return (
    <table className="w-full bg-gray-800 text-white rounded shadow">
      <thead>
        <tr>
          <th>Symbol</th>
          <th>Name</th>
          <th>Last Price</th>
          <th>Change %</th>
          <th>Volume</th>
        </tr>
      </thead>
      <tbody>
        {state.assets.map(asset => {
          const price = state.prices[asset.symbol] || {};
          const changeColor = price.change > 0 ? "text-green-400" : price.change < 0 ? "text-red-400" : "";
          return (
            <tr key={asset.symbol} className={highlight[asset.symbol] ? "bg-yellow-900" : ""}>
              <td>{asset.symbol}</td>
              <td>{asset.name}</td>
              <td>{price.price || "--"}</td>
              <td className={changeColor}>{price.change || "--"}</td>
              <td>{price.volume || "--"}</td>
            </tr>
          );
        })}
      </tbody>
    </table>
  );
}