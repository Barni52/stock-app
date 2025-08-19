import { Component, EventEmitter, input, Output } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { AuthService } from '../../services/auth-service';
import { CommonModule } from '@angular/common';
import { Observable } from 'rxjs';
import { StockService } from '../../services/stock-service';


@Component({
  selector: 'app-stock-item',
  imports: [FormsModule, CommonModule],
  templateUrl: './stock-item.html',
  styleUrl: './stock-item.scss'
})
export class StockItem {
  isLoggedIn! : boolean;
  ticker = input("non");
  currentPrice = input(0);
  amount : number = 1;
  hitPrice : number = 1.0;
  
  constructor(private authService : AuthService, private stockService : StockService){}

  ngOnInit() : void {

    this.authService.isAuthenticated$.subscribe(val => this.isLoggedIn = val);

  }

  buy(amount:number, hitPrice:number){
    this.stockService.makeBuyOrder(this.authService.getAuthenticatedUsername(), this.ticker(), amount, hitPrice)
  }

  totalAmount(){
    return this.hitPrice * this.amount;
  }
}
