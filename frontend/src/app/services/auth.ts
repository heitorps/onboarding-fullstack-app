import { HttpClient } from '@angular/common/http';
import { inject, Service } from '@angular/core';
import { Observable } from 'rxjs';

@Service()
export class AuthService {
    private apiUrl = 'http://localhost:8080/api/users';

    private http = inject(HttpClient);

    register(userData: any): Observable<any>{
        return this.http.post(`${this.apiUrl}/register`, userData);
    }

    login(credentials:any): Observable<any> {
        return this.http.post(`${this.apiUrl}/login`, credentials);
    }
}
