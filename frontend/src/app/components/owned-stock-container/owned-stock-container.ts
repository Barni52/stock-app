import { Component } from '@angular/core';
import { StockService } from '../../services/stock-service';
import { Stock } from '../../pages/stock/stock';
import { Observable } from 'rxjs';
import { AuthService } from '../../services/auth-service';
import { OwnedStock } from '../../models/models';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { OwnedStockItem } from "../owned-stock-item/owned-stock-item";

@Component({
  selector: 'app-owned-stock-container',
  imports: [CommonModule, FormsModule, OwnedStockItem],
  templateUrl: './owned-stock-container.html',
  styleUrl: './owned-stock-container.scss'
})
export class OwnedStockContainer {
    ownedStocks!: Observable<OwnedStock[]>;
  
    constructor(private stockService : StockService, private authService : AuthService){};

    private apiCall(){
      this.ownedStocks = this.stockService.getAllOwnedStocks(this.authService.getAuthenticatedUsername());
    }

    parentFunction(){
      setTimeout(() => this.apiCall(), 100);

    }
  
    ngOnInit() {
      this.apiCall();
    }
}
