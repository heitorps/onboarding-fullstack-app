import { HttpClient, HttpHeaders } from '@angular/common/http';
import { inject, Service } from '@angular/core';
import { Observable } from 'rxjs';

@Service()
export class ReviewService {
    private apiUrl = 'http://localhost:8080/api/reviews';
    private http = inject(HttpClient);

    getTimeline(): Observable<any[]> {
        const userId = localStorage.getItem('userId') || '';

        const headers = new HttpHeaders().set('X-User-Id',userId);

        return this.http.get<any[]>(`${this.apiUrl}/timeline`, {headers});
    }

    getReviewById(reviewId: number): Observable<any>{
        return this.http.get<any>(`${this.apiUrl}/${reviewId}`);
    }
}
