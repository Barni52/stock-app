import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { map, Observable } from 'rxjs';
import { Stock } from '../models/models';



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

}
