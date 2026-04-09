import { TestBed } from '@angular/core/testing';
import { provideHttpClient } from '@angular/common/http';
import { provideHttpClientTesting } from '@angular/common/http/testing';
import { FeedbackService } from '../../services/feedback.service';

describe('FeedbackService', () => {
  let service: FeedbackService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [
        provideHttpClient(),
        provideHttpClientTesting(),
        FeedbackService
      ]
    });

    service = TestBed.inject(FeedbackService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});