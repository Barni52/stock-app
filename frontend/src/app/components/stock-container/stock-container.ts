import { Component } from '@angular/core';
import { StockService } from '../../services/stock-service';
import { Stock } from '../../models/models';
import { of, Observable } from 'rxjs';
import { AsyncPipe } from '@angular/common';
import { StockItem } from '../stock-item/stock-item';

@Component({
  selector: 'app-stock-container',
  imports: [AsyncPipe, StockItem],
  templateUrl: './stock-container.html',
  styleUrl: './stock-container.scss'
})
export class StockContainer {
  stocks!: Observable<Stock[]>;

  constructor(private stockService : StockService){};

  ngOnInit() {
    this.stocks = this.stockService.getAllStocks();

    /*this.stocks = of(
      [{ticker : "AAPL", currentPrice : 201}, 
      {ticker : "RNND", currentPrice : 101}, 
      {ticker : "LOL", currentPrice : 4301.2}]
    )*/
  }


}
