import React from "react";
import { useMarket } from "../context/MarketContext";

const Sidebar: React.FC = () => {
  const { state, dispatch } = useMarket();
  return (
    <aside className="w-48 bg-gray-900 text-gray-100 p-4">
      <h2 className="font-semibold mb-2">Assets</h2>
      <ul>
        {state.assets.map((asset) => (
          <li
            key={asset.symbol}
            className={`cursor-pointer p-2 rounded ${
              state.selected === asset.symbol ? "bg-blue-700" : ""
            }`}
            onClick={() => dispatch({ type: "SELECT_ASSET", symbol: asset.symbol })}
          >
            {asset.name} <span className="text-xs text-gray-400">{asset.symbol}</span>
          </li>
        ))}
      </ul>
      {/* Add new tradeables by updating assets list in marketDataService.ts */}
    </aside>
  );
};

export default Sidebar;
