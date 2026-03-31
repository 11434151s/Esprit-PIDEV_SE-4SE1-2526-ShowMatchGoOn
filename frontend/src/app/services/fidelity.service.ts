import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { Fidelity, FidelityRequest } from '../models/fidelity.model';

@Injectable({ providedIn: 'root' })
export class FidelityService {
  private readonly API_URL = 'http://localhost:8091/api/fidelities';

  constructor(private http: HttpClient) {}

  getAll(): Observable<Fidelity[]> {
    return this.http.get<Fidelity[]>(this.API_URL);
  }

  getById(id: string): Observable<Fidelity> {
    return this.http.get<Fidelity>(`${this.API_URL}/${id}`);
  }

  create(data: FidelityRequest): Observable<Fidelity> {
    return this.http.post<Fidelity>(this.API_URL, data);
  }

  update(id: string, data: FidelityRequest): Observable<Fidelity> {
    return this.http.put<Fidelity>(`${this.API_URL}/${id}`, data);
  }

  delete(id: string): Observable<string> {
    return this.http.delete(`${this.API_URL}/${id}`, { responseType: 'text' });
  }
}