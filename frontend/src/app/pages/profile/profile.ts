import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { UserService } from '../../services/user-service';
import { AuthService } from '../../services/auth-service';
import { Router } from '@angular/router';
import { HttpErrorResponse } from '@angular/common/http';
import { take } from 'rxjs';

@Component({
  selector: 'app-profile',
  standalone: true,
  imports: [CommonModule],
  templateUrl: './profile.html',
  styleUrl: './profile.scss'
})
export class Profile implements OnInit {
  username: string | null = null;
  balance: number | null = null;
  errorMessage: string | null = null;

  constructor(
    private userService: UserService,
    private authService: AuthService,
    private router: Router
  ) { }

  ngOnInit() {
    // Subscribe to the currentUsername$ observable from AuthService
    // Use 'take(1)' to automatically unsubscribe after the first value is emitted
    this.authService.currentUsername$.pipe(
      take(1)
    ).subscribe(authenticatedUsername => {
      if (authenticatedUsername) {
        this.username = authenticatedUsername;
        this.fetchUserBalance(this.username);
      } else {
        // If no authenticated username, user is not logged in or token is invalid
        this.errorMessage = 'You are not logged in or your session has expired. Please log in again.';
        this.authService.logout();
        this.router.navigate(['/login']);
      }
    });
  }

  // New method to encapsulate fetching balance
  fetchUserBalance(username: string): void {
    this.errorMessage = null;
    this.userService.getUserBalance(username).subscribe({
      next: (data) => {
        this.balance = data;
      },
      error: (err: HttpErrorResponse) => { // Type the error as HttpErrorResponse
        console.error('Failed to fetch user balance:', err);
        if (err.status === 401 || err.status === 403) {
          this.errorMessage = 'Access denied or session expired. Please log in again.';
          this.authService.logout();
          this.router.navigate(['/login']);
        } else {
          this.errorMessage = 'An error occurred while fetching balance. Please try again.';
        }
      }
    });
  }
}
