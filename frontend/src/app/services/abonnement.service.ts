import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { Abonnement, AbonnementRequest } from '../models/abonnement.model';

@Injectable({ providedIn: 'root' })
export class AbonnementService {
  private readonly API_URL = 'http://localhost:8091/api/abonnements';

  constructor(private http: HttpClient) {}

  getAll(): Observable<Abonnement[]> {
    return this.http.get<Abonnement[]>(this.API_URL);
  }

  getById(id: string): Observable<Abonnement> {
    return this.http.get<Abonnement>(`${this.API_URL}/${id}`);
  }

  create(data: AbonnementRequest): Observable<Abonnement> {
    return this.http.post<Abonnement>(this.API_URL, data);
  }

  update(id: string, data: AbonnementRequest): Observable<Abonnement> {
    return this.http.put<Abonnement>(`${this.API_URL}/${id}`, data);
  }

  delete(id: string): Observable<string> {
    return this.http.delete(`${this.API_URL}/${id}`, { responseType: 'text' });
  }
}