import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';
import { AbonnementService } from './abonnement.service';
import { Abonnement, AbonnementRequest } from '../models/abonnement.model';

describe('AbonnementService', () => {
  let service: AbonnementService;
  let httpMock: HttpTestingController;
  const API_URL = 'http://localhost:8091/api/abonnements';

  const mockAbonnement: Abonnement = {
    id: 'abc123',
    type: 'PREMIUM',
    prix: 14.99,
    description: 'Accès illimité à tous les contenus premium'
  };

  const mockAbonnement2: Abonnement = {
    id: 'def456',
    type: 'BASIC',
    prix: 4.99,
    description: 'Accès basique aux contenus'
  };

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
      providers: [AbonnementService]
    });
    service = TestBed.inject(AbonnementService);
    httpMock = TestBed.inject(HttpTestingController);
  });

  afterEach(() => {
    httpMock.verify();
  });

  // =============================================
  // ✅ TEST getAll
  // =============================================
  it('devrait récupérer tous les abonnements', () => {
    service.getAll().subscribe(abonnements => {
      expect(abonnements).toBeTruthy();
      expect(abonnements.length).toBe(2);
      expect(abonnements[0].type).toBe('PREMIUM');
      expect(abonnements[1].type).toBe('BASIC');
    });

    const req = httpMock.expectOne(API_URL);
    expect(req.request.method).toBe('GET');
    req.flush([mockAbonnement, mockAbonnement2]);
  });

  it('devrait retourner une liste vide si aucun abonnement', () => {
    service.getAll().subscribe(abonnements => {
      expect(abonnements.length).toBe(0);
    });

    const req = httpMock.expectOne(API_URL);
    expect(req.request.method).toBe('GET');
    req.flush([]);
  });

  // =============================================
  // ✅ TEST getById
  // =============================================
  it('devrait récupérer un abonnement par ID', () => {
    service.getById('abc123').subscribe(abonnement => {
      expect(abonnement).toBeTruthy();
      expect(abonnement.id).toBe('abc123');
      expect(abonnement.type).toBe('PREMIUM');
      expect(abonnement.prix).toBe(14.99);
    });

    const req = httpMock.expectOne(`${API_URL}/abc123`);
    expect(req.request.method).toBe('GET');
    req.flush(mockAbonnement);
  });

  // =============================================
  // ✅ TEST create
  // =============================================
  it('devrait créer un nouvel abonnement', () => {
    const newAbonnement: AbonnementRequest = {
      type: 'PREMIUM',
      prix: 14.99,
      description: 'Accès illimité à tous les contenus premium'
    };

    service.create(newAbonnement).subscribe(abonnement => {
      expect(abonnement).toBeTruthy();
      expect(abonnement.id).toBe('abc123');
      expect(abonnement.type).toBe('PREMIUM');
      expect(abonnement.prix).toBe(14.99);
    });

    const req = httpMock.expectOne(API_URL);
    expect(req.request.method).toBe('POST');
    expect(req.request.body).toEqual(newAbonnement);
    req.flush(mockAbonnement);
  });

  // =============================================
  // ✅ TEST update
  // =============================================
  it('devrait mettre à jour un abonnement existant', () => {
    const updatedData: AbonnementRequest = {
      type: 'BASIC',
      prix: 9.99,
      description: 'Description mise à jour'
    };

    const updatedAbonnement: Abonnement = {
      id: 'abc123',
      type: 'BASIC',
      prix: 9.99,
      description: 'Description mise à jour'
    };

    service.update('abc123', updatedData).subscribe(abonnement => {
      expect(abonnement).toBeTruthy();
      expect(abonnement.type).toBe('BASIC');
      expect(abonnement.prix).toBe(9.99);
    });

    const req = httpMock.expectOne(`${API_URL}/abc123`);
    expect(req.request.method).toBe('PUT');
    expect(req.request.body).toEqual(updatedData);
    req.flush(updatedAbonnement);
  });

  // =============================================
  // ✅ TEST delete
  // =============================================
  it('devrait supprimer un abonnement', () => {
    service.delete('abc123').subscribe(response => {
      expect(response).toBe('Abonnement supprimé avec succès');
    });

    const req = httpMock.expectOne(`${API_URL}/abc123`);
    expect(req.request.method).toBe('DELETE');
    req.flush('Abonnement supprimé avec succès');
  });

  // =============================================
  // ✅ TEST erreur HTTP
  // =============================================
  it('devrait gérer une erreur 404 sur getById', () => {
    service.getById('notfound').subscribe({
      next: () => fail('Devrait échouer'),
      error: (error) => {
        expect(error.status).toBe(404);
      }
    });

    const req = httpMock.expectOne(`${API_URL}/notfound`);
    req.flush('Not Found', { status: 404, statusText: 'Not Found' });
  });

  it('devrait gérer une erreur 500 sur getAll', () => {
    service.getAll().subscribe({
      next: () => fail('Devrait échouer'),
      error: (error) => {
        expect(error.status).toBe(500);
      }
    });

    const req = httpMock.expectOne(API_URL);
    req.flush('Server Error', { status: 500, statusText: 'Internal Server Error' });
  });
});