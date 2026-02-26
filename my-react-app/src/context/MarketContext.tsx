import React, { createContext, useReducer, useContext, ReactNode } from "react";

export type Asset = {
  symbol: string;
  name: string;
  type: string;
};
export type Price = {
  symbol: string;
  price: number;
  change: number;
  volume: number;
};
export type MarketState = {
  assets: Asset[];
  prices: Record<string, Price>;
  selected: string | null;
};

type MarketAction =
  | { type: "SET_ASSETS"; assets: Asset[] }
  | { type: "UPDATE_PRICE"; symbol: string; price: Price }
  | { type: "SELECT_ASSET"; symbol: string };

const MarketContext = createContext<{
  state: MarketState;
  dispatch: React.Dispatch<MarketAction>;
} | undefined>(undefined);

const initialState: MarketState = {
  assets: [],
  prices: {},
  selected: null,
};

function reducer(state: MarketState, action: MarketAction): MarketState {
  switch (action.type) {
    case "SET_ASSETS":
      return { ...state, assets: action.assets };
    case "UPDATE_PRICE":
      return {
        ...state,
        prices: { ...state.prices, [action.symbol]: action.price },
      };
    case "SELECT_ASSET":
      return { ...state, selected: action.symbol };
    default:
      return state;
  }
}

export function MarketProvider({ children }: { children: ReactNode }) {
  const [state, dispatch] = useReducer(reducer, initialState);
  return (
    <MarketContext.Provider value={{ state, dispatch }}>
      {children}
    </MarketContext.Provider>
  );
}
export function useMarket() {
  const context = useContext(MarketContext);
  if (!context) throw new Error("useMarket must be used within MarketProvider");
  return context;
}
