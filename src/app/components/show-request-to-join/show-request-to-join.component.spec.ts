import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ShowRequestToJoinComponent } from './show-request-to-join.component';

describe('ShowRequestToJoinComponent', () => {
  let component: ShowRequestToJoinComponent;
  let fixture: ComponentFixture<ShowRequestToJoinComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ ShowRequestToJoinComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(ShowRequestToJoinComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
