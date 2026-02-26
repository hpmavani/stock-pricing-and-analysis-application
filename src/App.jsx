import React, { useEffect } from "react";
import { MarketProvider, useMarket } from "./context/MarketContext";
import Header from "./components/Header";
import Sidebar from "./components/Sidebar";
import PriceTable from "./components/PriceTable";
import AnalysisPanel from "./components/AnalysisPanel";
import { connectMarketData } from "./services/marketDataService";

function MainApp() {
  const { state, dispatch } = useMarket();
  useEffect(() => {
    const ws = connectMarketData({
      onAssets: assets => dispatch({ type: "SET_ASSETS", assets }),
      onPrice: price => dispatch({ type: "UPDATE_PRICE", symbol: price.symbol, price }),
    });
    return () => ws && ws.close();
  }, [dispatch]);
  const handleRefresh = () => window.location.reload();
  return (
    <div className="flex flex-col h-screen bg-gray-950">
      <Header onRefresh={handleRefresh} />
      <div className="flex flex-1">
        <Sidebar />
        <main className="flex-1 p-6">
          <PriceTable />
          <AnalysisPanel />
        </main>
      </div>
    </div>
  );
}

export default function App() {
  return (
    <MarketProvider>
      <MainApp />
    </MarketProvider>
  );
}