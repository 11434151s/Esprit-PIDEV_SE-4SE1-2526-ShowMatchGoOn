import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';
import { FidelityService } from './fidelity.service';
import { Fidelity, FidelityRequest } from '../models/fidelity.model';

describe('FidelityService', () => {
  let service: FidelityService;
  let httpMock: HttpTestingController;
  const API_URL = 'http://localhost:8091/api/fidelities';

  const mockFidelity: Fidelity = {
    id: 'fid123',
    points: 500,
    level: 'GOLD',
    description: 'Niveau GOLD avec avantages exclusifs'
  };

  const mockFidelity2: Fidelity = {
    id: 'fid456',
    points: 100,
    level: 'BRONZE',
    description: 'Niveau BRONZE pour débutants'
  };

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
      providers: [FidelityService]
    });
    service = TestBed.inject(FidelityService);
    httpMock = TestBed.inject(HttpTestingController);
  });

  afterEach(() => {
    httpMock.verify();
  });

  // =============================================
  // ✅ TEST getAll
  // =============================================
  it('devrait récupérer tous les niveaux de fidélité', () => {
    service.getAll().subscribe(fidelities => {
      expect(fidelities).toBeTruthy();
      expect(fidelities.length).toBe(2);
      expect(fidelities[0].level).toBe('GOLD');
      expect(fidelities[1].level).toBe('BRONZE');
    });

    const req = httpMock.expectOne(API_URL);
    expect(req.request.method).toBe('GET');
    req.flush([mockFidelity, mockFidelity2]);
  });

  it('devrait retourner une liste vide si aucune fidélité', () => {
    service.getAll().subscribe(fidelities => {
      expect(fidelities.length).toBe(0);
    });

    const req = httpMock.expectOne(API_URL);
    expect(req.request.method).toBe('GET');
    req.flush([]);
  });

  // =============================================
  // ✅ TEST getById
  // =============================================
  it('devrait récupérer une fidélité par ID', () => {
    service.getById('fid123').subscribe(fidelity => {
      expect(fidelity).toBeTruthy();
      expect(fidelity.id).toBe('fid123');
      expect(fidelity.level).toBe('GOLD');
      expect(fidelity.points).toBe(500);
    });

    const req = httpMock.expectOne(`${API_URL}/fid123`);
    expect(req.request.method).toBe('GET');
    req.flush(mockFidelity);
  });

  // =============================================
  // ✅ TEST create
  // =============================================
  it('devrait créer un nouveau niveau de fidélité', () => {
    const newFidelity: FidelityRequest = {
      points: 500,
      level: 'GOLD',
      description: 'Niveau GOLD avec avantages exclusifs'
    };

    service.create(newFidelity).subscribe(fidelity => {
      expect(fidelity).toBeTruthy();
      expect(fidelity.id).toBe('fid123');
      expect(fidelity.level).toBe('GOLD');
      expect(fidelity.points).toBe(500);
    });

    const req = httpMock.expectOne(API_URL);
    expect(req.request.method).toBe('POST');
    expect(req.request.body).toEqual(newFidelity);
    req.flush(mockFidelity);
  });

  it('devrait créer un niveau BRONZE avec 0 points', () => {
    const bronzeRequest: FidelityRequest = {
      points: 0,
      level: 'BRONZE',
      description: 'Niveau débutant'
    };

    const bronzeResponse: Fidelity = {
      id: 'fid789',
      points: 0,
      level: 'BRONZE',
      description: 'Niveau débutant'
    };

    service.create(bronzeRequest).subscribe(fidelity => {
      expect(fidelity.points).toBe(0);
      expect(fidelity.level).toBe('BRONZE');
    });

    const req = httpMock.expectOne(API_URL);
    expect(req.request.method).toBe('POST');
    req.flush(bronzeResponse);
  });

  // =============================================
  // ✅ TEST update
  // =============================================
  it('devrait mettre à jour un niveau de fidélité', () => {
    const updatedData: FidelityRequest = {
      points: 1000,
      level: 'PLATINUM',
      description: 'Niveau PLATINUM mis à jour'
    };

    const updatedFidelity: Fidelity = {
      id: 'fid123',
      points: 1000,
      level: 'PLATINUM',
      description: 'Niveau PLATINUM mis à jour'
    };

    service.update('fid123', updatedData).subscribe(fidelity => {
      expect(fidelity).toBeTruthy();
      expect(fidelity.level).toBe('PLATINUM');
      expect(fidelity.points).toBe(1000);
    });

    const req = httpMock.expectOne(`${API_URL}/fid123`);
    expect(req.request.method).toBe('PUT');
    expect(req.request.body).toEqual(updatedData);
    req.flush(updatedFidelity);
  });

  // =============================================
  // ✅ TEST delete
  // =============================================
  it('devrait supprimer un niveau de fidélité', () => {
    service.delete('fid123').subscribe(response => {
      expect(response).toBe('Fidelity supprimée avec succès');
    });

    const req = httpMock.expectOne(`${API_URL}/fid123`);
    expect(req.request.method).toBe('DELETE');
    req.flush('Fidelity supprimée avec succès');
  });

  // =============================================
  // ✅ TEST erreurs HTTP
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

  it('devrait gérer une erreur 500 sur create', () => {
    const badRequest: FidelityRequest = {
      points: -1,
      level: 'BRONZE',
      description: 'Test'
    };

    service.create(badRequest).subscribe({
      next: () => fail('Devrait échouer'),
      error: (error) => {
        expect(error.status).toBe(500);
      }
    });

    const req = httpMock.expectOne(API_URL);
    req.flush('Server Error', { status: 500, statusText: 'Internal Server Error' });
  });
});