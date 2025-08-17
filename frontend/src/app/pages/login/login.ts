import { Component } from '@angular/core';
import { CommonModule } from '@angular/common'; // Required for *ngIf
import { FormsModule } from '@angular/forms'; // Required for [(ngModel)]
import { RouterModule } from '@angular/router'; // Required for routerLink
import { AuthService } from '../../services/auth-service';
import { Router } from '@angular/router';
import { AuthRequest } from '../../models/auth';

@Component({
  selector: 'app-login',
  standalone: true, // Mark as standalone
  imports: [CommonModule, FormsModule, RouterModule], // Import necessary modules
  templateUrl: './login.html',
  styleUrl: './login.scss',

})
export class LoginComponent {
  authRequest: AuthRequest = { username: '', password: '' };
  errorMessage: string | null = null;

  constructor(private authService: AuthService, private router: Router) { }

  onLogin(): void {
    this.errorMessage = null; // Clear previous errors
    this.authService.login(this.authRequest).subscribe({
      next: () => {
        this.router.navigate(['']);
      },
      error: (err) => {
        console.error('Login failed:', err);
        this.errorMessage = 'Invalid username or password. Please try again.';
      }
    });
  }
}
