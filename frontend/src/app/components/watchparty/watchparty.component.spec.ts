import { TestBed } from '@angular/core/testing';
import { provideHttpClient } from '@angular/common/http';
import { provideHttpClientTesting } from '@angular/common/http/testing';
import { WatchpartyService } from '../../services/watchparty.service';

describe('WatchpartyService', () => {
  let service: WatchpartyService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [
        provideHttpClient(),
        provideHttpClientTesting(),
        WatchpartyService
      ]
    });

    service = TestBed.inject(WatchpartyService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});