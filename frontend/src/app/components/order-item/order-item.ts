import { CommonModule } from '@angular/common';
import { Component, input } from '@angular/core';
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

}
