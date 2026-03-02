import { Injectable } from '@angular/core';
import { HttpClient, HttpErrorResponse } from '@angular/common/http';
import { Observable, throwError } from 'rxjs';
import { catchError } from 'rxjs/operators';

export interface Film {
  id?: string;
  title: string;
  description: string;
  releaseDate: string;
  duration: number;
  genre: string;
}

export interface Series {
  id?: string;
  title: string;
  description: string;
  releaseDate: string;
  numberOfSeasons: number;
  genre: string;
}

export interface Documentary {
  id?: string;
  title: string;
  description: string;
  releaseDate: string;
  duration: number;
  genre: string;
}

export interface Category {
  id?: string;
  name: string;
  description: string;
}

export interface Notification {
  id?: string;
  title: string;
  message: string;
  read: boolean;
}

@Injectable({
  providedIn: 'root'
})
export class ContentService {
  private baseUrl = 'http://localhost:8080/api/contents';

  constructor(private http: HttpClient) { }

  // Films CRUD
  createFilm(film: Film): Observable<Film> {
    return this.http.post<Film>(`${this.baseUrl}/films`, film).pipe(catchError(this.handleError));
  }

  getFilmById(id: string): Observable<Film> {
    return this.http.get<Film>(`${this.baseUrl}/${id}`).pipe(catchError(this.handleError));
  }

  getAllContent(): Observable<any[]> {
    return this.http.get<any[]>(this.baseUrl).pipe(catchError(this.handleError));
  }

  updateFilm(id: string, film: Film): Observable<Film> {
    return this.http.put<Film>(`${this.baseUrl}/films/${id}`, film).pipe(catchError(this.handleError));
  }

  deleteContent(id: string): Observable<void> {
    return this.http.delete<void>(`${this.baseUrl}/${id}`).pipe(catchError(this.handleError));
  }

  // Series CRUD
  createSeries(series: Series): Observable<Series> {
    return this.http.post<Series>(`${this.baseUrl}/series`, series).pipe(catchError(this.handleError));
  }

  updateSeries(id: string, series: Series): Observable<Series> {
    return this.http.put<Series>(`${this.baseUrl}/series/${id}`, series).pipe(catchError(this.handleError));
  }

  // Documentary CRUD
  createDocumentary(doc: Documentary): Observable<Documentary> {
    return this.http.post<Documentary>(`${this.baseUrl}/documentaries`, doc).pipe(catchError(this.handleError));
  }

  updateDocumentary(id: string, doc: Documentary): Observable<Documentary> {
    return this.http.put<Documentary>(`${this.baseUrl}/documentaries/${id}`, doc).pipe(catchError(this.handleError));
  }

  private handleError(error: HttpErrorResponse) {
    let message = 'An error occurred';
    if (error.error instanceof ErrorEvent) {
      message = `Error: ${error.error.message}`;
    } else {
      message = `Error Code: ${error.status}\nMessage: ${error.message}`;
    }
    return throwError(() => new Error(message));
  }
}

@Injectable({
  providedIn: 'root'
})
export class CategoryService {
  private baseUrl = 'http://localhost:8080/api/categories';

  constructor(private http: HttpClient) { }

  createCategory(category: Category): Observable<Category> {
    return this.http.post<Category>(this.baseUrl, category).pipe(catchError(this.handleError));
  }

  getCategoryById(id: string): Observable<Category> {
    return this.http.get<Category>(`${this.baseUrl}/${id}`).pipe(catchError(this.handleError));
  }

  getAllCategories(): Observable<Category[]> {
    return this.http.get<Category[]>(this.baseUrl).pipe(catchError(this.handleError));
  }

  updateCategory(id: string, category: Category): Observable<Category> {
    return this.http.put<Category>(`${this.baseUrl}/${id}`, category).pipe(catchError(this.handleError));
  }

  deleteCategory(id: string): Observable<void> {
    return this.http.delete<void>(`${this.baseUrl}/${id}`).pipe(catchError(this.handleError));
  }

  private handleError(error: HttpErrorResponse) {
    let message = 'An error occurred';
    if (error.error instanceof ErrorEvent) {
      message = `Error: ${error.error.message}`;
    } else {
      message = `Error Code: ${error.status}\nMessage: ${error.message}`;
    }
    return throwError(() => new Error(message));
  }
}

@Injectable({
  providedIn: 'root'
})
export class NotificationService {
  private baseUrl = 'http://localhost:8080/api/notifications';

  constructor(private http: HttpClient) { }

  createNotification(notification: Notification): Observable<Notification> {
    return this.http.post<Notification>(this.baseUrl, notification).pipe(catchError(this.handleError));
  }

  getNotificationsByUserId(userId: string): Observable<Notification[]> {
    return this.http.get<Notification[]>(`${this.baseUrl}/user/${userId}`).pipe(catchError(this.handleError));
  }

  markAsRead(id: string): Observable<void> {
    return this.http.patch<void>(`${this.baseUrl}/${id}/read`, {}).pipe(catchError(this.handleError));
  }

  deleteNotification(id: string): Observable<void> {
    return this.http.delete<void>(`${this.baseUrl}/${id}`).pipe(catchError(this.handleError));
  }

  private handleError(error: HttpErrorResponse) {
    let message = 'An error occurred';
    if (error.error instanceof ErrorEvent) {
      message = `Error: ${error.error.message}`;
    } else {
      message = `Error Code: ${error.status}\nMessage: ${error.message}`;
    }
    return throwError(() => new Error(message));
  }
}
