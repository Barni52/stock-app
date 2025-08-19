import { CommonModule } from '@angular/common';
import { Component, EventEmitter, input, Output } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { AuthService } from '../../services/auth-service';
import { StockService } from '../../services/stock-service';

@Component({
  selector: 'app-owned-stock-item',
  imports: [CommonModule, FormsModule],
  templateUrl: './owned-stock-item.html',
  styleUrl: './owned-stock-item.scss'
})
export class OwnedStockItem {
  isLoggedIn! : boolean;
  ticker = input('');
  currentPrice = input(0);
  quantity = input(0); 
  amount : number = 0;
  hitPrice : number = 0;

  @Output() notifyParent = new EventEmitter<void>();


  constructor(private authService : AuthService, private stockService : StockService){}

  ngOnInit() : void {

    this.authService.isAuthenticated$.subscribe(val => this.isLoggedIn = val);

  }

  sell(amount:number, hitPrice:number){
    this.stockService.makeSellOrder(this.authService.getAuthenticatedUsername(), this.ticker(), amount, hitPrice);
    this.notifyParent.emit();
  }

  totalAmount(){
    return this.hitPrice * this.amount;
  }
}
