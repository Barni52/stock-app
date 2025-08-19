export interface User {
  id : number;
  username : string;
  balance : number;
}

export interface Stock {
  ticker : string;
  currentPrice : number;
}

export interface StockOrder {
  id : number;
  stock : Stock;
  quantity : number;
  hitPrice : number;
  type : string;
}

export interface OwnedStock {
  id : number;
  stock: Stock;
  quantity  : number;
}
