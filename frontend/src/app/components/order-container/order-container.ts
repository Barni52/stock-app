import { Component } from '@angular/core';
import { Observable } from 'rxjs';
import { StockOrder } from '../../models/models';
import { StockService } from '../../services/stock-service';
import { AuthService } from '../../services/auth-service';
import { OrderItem } from "../order-item/order-item";
import { FormsModule } from '@angular/forms';
import { CommonModule } from '@angular/common';

@Component({
  selector: 'app-order-container',
  imports: [OrderItem, FormsModule, CommonModule],
  templateUrl: './order-container.html',
  styleUrl: './order-container.scss'
})
export class OrderContainer {
      orders!: Observable<StockOrder[]>;
    
      constructor(private stockService : StockService, private authService : AuthService){};

      private apiCall(){
        this.orders = this.stockService.getAllOrders(this.authService.getAuthenticatedUsername());
      }
    
      ngOnInit() {
        this.apiCall();
      }

      parentFunction(){
        setTimeout(() => this.apiCall(), 100);
      }
}
