import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { RouterModule } from '@angular/router';
import { AuthService } from '../../services/auth-service';
import { Router } from '@angular/router';
import { AuthRequest } from '../../models/auth';

@Component({
  selector: 'app-register',
  standalone: true, // Mark as standalone
  imports: [CommonModule, FormsModule, RouterModule], // Import necessary modules
  templateUrl: './register.html',
  styleUrl: './register.scss',
})
export class RegisterComponent {
  authRequest: AuthRequest = { username: '', password: '' };
  successMessage: string | null = null;
  errorMessage: string | null = null;

  constructor(private authService: AuthService, private router: Router) { }

  onRegister(): void {
    this.successMessage = null;
    this.errorMessage = null; // Clear previous messages
    this.authService.register(this.authRequest).subscribe({
      next: (response) => {
        this.successMessage = 'Registration successful! You can now log in.';
        // Optionally navigate to login page after a short delay
        setTimeout(() => {
          this.router.navigate(['/login']);
        }, 2000);
      },
      error: (err) => {
        console.error('Registration failed:', err);
        if (err.status === 409) {
          this.errorMessage = 'Username already exists. Please choose a different one.';
        } else {
          this.errorMessage = 'Registration failed. Please try again later.';
        }
      }
    });
  }
}