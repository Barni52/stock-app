import { Injectable } from '@angular/core';
import { HttpClient, HttpParams } from '@angular/common/http';
import { map, Observable } from 'rxjs';
import { OwnedStock, Stock, StockOrder } from '../models/models';



@Injectable({
  providedIn: 'root'
})
export class StockService {
  private url = 'http://localhost:8080/';

  constructor(private http : HttpClient){}

  getAllStocks() : Observable<Stock[]> {
    return this.http.get<any[]>(`${this.url}stock`).pipe(
      map(data => data.map(item => ({
        ticker : item.ticker,
        currentPrice : item.currentPrice
      })))
    );
  }

  getAllOwnedStocks (username : string | null) : Observable<OwnedStock[]> {
    return this.http.get<any[]>(`${this.url}stock/owned/${username}`).pipe(
      map(data => data.map(item => ({
        id : item.id,
        stock : {
          ticker : item.stock.ticker,
          currentPrice : item.stock.currentPrice
        },
        quantity : item.quantity
      })))
    );
  }

  getAllOrders (username : string | null) : Observable<StockOrder[]> {
    return this.http.get<any[]>(`${this.url}stock/order/${username}`).pipe(
      map(response =>
        response.map(o => ({
          id: o.id,
          stock: {
            ticker: o.stock.ticker,
            currentPrice: o.stock.currentPrice
          },
          quantity: o.quantity,
          hitPrice: o.hitPrice,
          type: o.type
        }))
      )
    );
  }

  makeBuyOrder(username : string | null, ticker : string, quantity : number, hitPrice : number) {
    const body = {
        username: username,
        ticker: ticker,
        quantity: quantity,
        hitPrice: hitPrice
    }

    console.log("hello")
    return this.http.post<void>(`${this.url}order/buy`, body).subscribe({
      next: () => console.log("Order placed"),
      error: err => console.error(err)
    });
  }

  makeSellOrder(username : string | null, ticker : string, quantity : number, hitPrice : number) {
    const body = {
        username: username,
        ticker: ticker,
        quantity: quantity,
        hitPrice: hitPrice
    }

    console.log("hello")
    return this.http.post<void>(`${this.url}order/sell`, body).subscribe({
      next: () => console.log("Order placed"),
      error: err => console.error(err)
    });
  }

  cancelOrder(username : string | null, id : number){
    return this.http.delete<void>(`${this.url}order/cancel/${username}/${id}`).subscribe({
      next: () => {console.log("Order deleted")},
      error: err => console.error(err)
    });
  }
}
