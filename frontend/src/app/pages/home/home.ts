import { Component } from '@angular/core';
import { StockContainer } from '../../components/stock-container/stock-container';

@Component({
  selector: 'app-home',
  imports: [StockContainer],
  templateUrl: './home.html',
  styleUrl: './home.scss'
})
export class Home {
  
}
