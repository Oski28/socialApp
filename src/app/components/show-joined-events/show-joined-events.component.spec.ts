import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ShowJoinedEventsComponent } from './show-joined-events.component';

describe('ShowJoinedEventsComponent', () => {
  let component: ShowJoinedEventsComponent;
  let fixture: ComponentFixture<ShowJoinedEventsComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ ShowJoinedEventsComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(ShowJoinedEventsComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
