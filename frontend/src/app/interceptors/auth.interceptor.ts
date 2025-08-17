import { HttpRequest, HttpHandlerFn, HttpEvent, HttpInterceptorFn } from '@angular/common/http';
import { Observable } from 'rxjs';
import { AuthService } from '../services/auth-service';
import { environment } from '../enviroment/enviroment';
import { inject } from '@angular/core';

export const authInterceptor: HttpInterceptorFn = (
  request: HttpRequest<unknown>,
  next: HttpHandlerFn
): Observable<HttpEvent<unknown>> => {
  const authService = inject(AuthService);
  const token = authService.getToken(); // Call getToken() to retrieve the token
  const isApiUrl = request.url.startsWith(environment.apiUrl);

  console.log('AuthInterceptor: Intercepting request to URL:', request.url);
  console.log('AuthInterceptor: Token retrieved:', token ? token.substring(0, 20) + '...' : 'No token');

  if (token && isApiUrl && !request.url.includes('/auth/login') && !request.url.includes('/auth/register')) {
    request = request.clone({
      setHeaders: {
        Authorization: `Bearer ${token}`
      }
    });
    console.log('AuthInterceptor: Added Authorization header to request.');
  } else {
    console.log('AuthInterceptor: Did not add Authorization header (no token, not API URL, or login/register endpoint).');
  }

  return next(request);
};