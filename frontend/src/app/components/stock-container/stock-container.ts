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

  apiCall(){
    this.stocks = this.stockService.getAllStocks();
  }

  ngOnInit() {
    this.stocks = this.stockService.getAllStocks();
  }


}
