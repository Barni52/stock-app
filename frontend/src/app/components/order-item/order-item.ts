import { CommonModule } from '@angular/common';
import { ChangeDetectorRef, Component, EventEmitter, input, Output } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { AuthService } from '../../services/auth-service';
import { StockService } from '../../services/stock-service';

@Component({
  selector: 'app-order-item',
  imports: [CommonModule, FormsModule],
  templateUrl: './order-item.html',
  styleUrl: './order-item.scss'
})
export class OrderItem {
  id = input(0);
  ticker = input('');
  quantity = input(0);
  sideType = input('');
  hitPrice = input(0);
  currentPrice = input();

  @Output() notifyParent = new EventEmitter<void>();

  constructor(private stockService : StockService, private authService : AuthService){}

  ngOnInit() : void {
    
  }


  cancel(){
    console.log("Attempting to cancel");
    this.stockService.cancelOrder(this.authService.getAuthenticatedUsername(), this.id());
    this.notifyParent.emit();
  }
}
