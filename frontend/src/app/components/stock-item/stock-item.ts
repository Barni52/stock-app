import { Component, input } from '@angular/core';
import { FormsModule } from '@angular/forms';

@Component({
  selector: 'app-stock-item',
  imports: [FormsModule],
  templateUrl: './stock-item.html',
  styleUrl: './stock-item.scss'
})
export class StockItem {
  ticker = input("non");
  currentPrice = input(0);
  amount : number = 1;
  hitPrice : number = 1.0;

  buy(amount:number, hitPrice:number){
    console.log("Amount entered: ", amount)
  }

  totalAmount(){
    return this.hitPrice * this.currentPrice();
  }
}
