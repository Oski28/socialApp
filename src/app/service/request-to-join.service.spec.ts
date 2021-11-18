import { TestBed } from '@angular/core/testing';

import { RequestToJoinService } from './request-to-join.service';

describe('RequestToJoinService', () => {
  let service: RequestToJoinService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(RequestToJoinService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
