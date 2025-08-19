import { Component } from '@angular/core';
import { RouterLink, RouterModule } from '@angular/router';
import { AuthService } from '../../services/auth-service';
import { CommonModule } from '@angular/common';
import { UserService } from '../../services/user-service';

@Component({
  selector: 'app-header',
  imports: [RouterLink, RouterModule, CommonModule],
  templateUrl: './header.html',
  styleUrl: './header.scss'
})
export class Header {
  isLoggedIn! : boolean;
  balance: number = 0;
  username! : string | null;


  constructor(private authService : AuthService, private userService : UserService){};

  ngOnInit() : void {
    this.authService.isAuthenticated$.subscribe(val => this.isLoggedIn = val);
    this.userService.getUserBalance(this.authService.getAuthenticatedUsername()).subscribe(val => this.balance = val);
  }

}
