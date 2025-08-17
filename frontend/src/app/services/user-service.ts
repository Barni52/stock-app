import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { User } from '../models/models';

@Injectable({
  providedIn: 'root'
})
export class UserService {
  private url = 'http://localhost:8080/';

  constructor(private http: HttpClient) { }


  getUserBalance(username : string) : Observable<number>{
    return this.http.get<number>(`${this.url}balance/${username}`);
  }
}
