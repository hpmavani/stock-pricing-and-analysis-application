import React, { createContext, useReducer, useContext } from "react";
const MarketContext = createContext();
const initialState = { assets: [], prices: {}, selected: null };

function reducer(state, action) {
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
export function MarketProvider({ children }) {
  const [state, dispatch] = useReducer(reducer, initialState);
  return (
    <MarketContext.Provider value={{ state, dispatch }}>
      {children}
    </MarketContext.Provider>
  );
}
export function useMarket() {
  return useContext(MarketContext);
}