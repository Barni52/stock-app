import { Injectable } from '@angular/core';
import { HttpClient, HttpErrorResponse } from '@angular/common/http';
import { Observable, BehaviorSubject, tap, catchError, throwError } from 'rxjs';
import { environment } from '../enviroment/enviroment';
import { AuthRequest, AuthResponse } from '../models/auth';
import { jwtDecode } from 'jwt-decode'; // Import jwtDecode

@Injectable({
  providedIn: 'root'
})
export class AuthService {
  private apiUrl = environment.apiUrl;
  private isAuthenticatedSubject = new BehaviorSubject<boolean>(this.hasToken());
  // New: Subject to hold the current authenticated username
  private currentUsernameSubject = new BehaviorSubject<string | null>(this.getStoredUsernameFromToken());

  isAuthenticated$: Observable<boolean> = this.isAuthenticatedSubject.asObservable();
  currentUsername$: Observable<string | null> = this.currentUsernameSubject.asObservable(); // Expose username as Observable

  constructor(private http: HttpClient) { }

  login(authRequest: AuthRequest): Observable<AuthResponse> {
    return this.http.post<AuthResponse>(`${this.apiUrl}/auth/login`, authRequest).pipe(
      tap(response => {
        console.log('AuthService: Raw response object received by Angular:', response);

        if (response && typeof response === 'object' && 'token' in response && response.token) {
          console.log('AuthService: Extracted JWT from response:', response.token.substring(0, 20) + '...');
          localStorage.setItem('jwt_token', response.token);
          this.isAuthenticatedSubject.next(true);

          // Decode token and store username
          try {
            const decodedToken: { sub: string } = jwtDecode(response.token);
            const username = decodedToken.sub; // 'sub' (subject) is standard for username in JWTs
            localStorage.setItem('authenticated_username', username); // Store username separately for easier access
            this.currentUsernameSubject.next(username);
            console.log('AuthService: Stored authenticated username:', username);
            
          } catch (e) {
            console.error('AuthService: Failed to decode JWT or extract username:', e);
            // If decoding fails, invalidate token and authentication state
            localStorage.removeItem('jwt_token');
            localStorage.removeItem('authenticated_username');
            this.isAuthenticatedSubject.next(false);
            this.currentUsernameSubject.next(null);
          }

        } else {
          console.error('AuthService: Login response did not contain a JWT or was malformed.', response);
          this.isAuthenticatedSubject.next(false);
          this.currentUsernameSubject.next(null);
        }
      }),
      catchError((error: HttpErrorResponse) => {
        console.error('AuthService: HTTP Error during login:', error);
        let errorMessage = 'An unknown error occurred during login.';
        if (error.error instanceof ErrorEvent) {
          errorMessage = `Client-side error: ${error.error.message}`;
        } else {
          if (error.status === 0) {
            errorMessage = 'Network error or CORS issue. Please check your backend is running and CORS is configured.';
          } else if (error.status === 401) {
            errorMessage = 'Invalid credentials.';
          } else if (error.error && typeof error.error === 'string') {
            errorMessage = `Server error: ${error.error}`;
          } else if (error.error && typeof error.error === 'object' && (error.error as any).message) {
             errorMessage = `Server error: ${(error.error as any).message}`;
          } else {
            errorMessage = `Server returned code ${error.status}, body was: ${error.message}`;
          }
        }
        return throwError(() => new Error(errorMessage));
      })
    );
  }

  register(authRequest: AuthRequest): Observable<string> {
    return this.http.post(`${this.apiUrl}/auth/register`, authRequest, { responseType: 'text' }).pipe(
      tap(response => {
        console.log('AuthService: Registration successful response:', response);
      }),
      catchError((error: HttpErrorResponse) => {
        console.error('AuthService: HTTP Error during registration:', error);
        let errorMessage = 'An unknown error occurred during registration.';
        if (error.status === 409) {
          errorMessage = 'Username already exists.';
        } else if (error.error && typeof error.error === 'string') {
          errorMessage = `Server error: ${error.error}`;
        } else if (error.status === 0) {
            errorMessage = 'Network error or CORS issue. Please check your backend is running and CORS is configured.';
        }
        return throwError(() => new Error(errorMessage));
      })
    );
  }

  logout(): void {
    console.log('AuthService: Logging out, removing token and username from localStorage.');
    localStorage.removeItem('jwt_token');
    localStorage.removeItem('authenticated_username'); // Clear stored username
    this.isAuthenticatedSubject.next(false);
    this.currentUsernameSubject.next(null); // Clear username in subject
  }

  getToken(): string | null {
    const token = localStorage.getItem('jwt_token');
    console.log('AuthService: Retrieving token from localStorage:', token ? token.substring(0, 20) + '...' : 'No token in localStorage');
    return token;
  }

  // New method to get the authenticated username
  getAuthenticatedUsername(): string | null {
    return this.currentUsernameSubject.value;
  }

  // Helper to initialize username from stored token on app load
  private getStoredUsernameFromToken(): string | null {
    const token = this.getToken(); // This will also log token retrieval
    if (token) {
      try {
        const decodedToken: { sub: string } = jwtDecode(token);
        return decodedToken.sub;
      } catch (e) {
        console.error('AuthService: Error decoding stored token for username on app load:', e);
        // If stored token is invalid, clear it
        localStorage.removeItem('jwt_token');
        localStorage.removeItem('authenticated_username');
        return null;
      }
    }
    return null;
  }

  private hasToken(): boolean {
    return !!localStorage.getItem('jwt_token');
  }
}