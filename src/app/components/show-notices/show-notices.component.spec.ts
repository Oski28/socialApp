import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ShowNoticesComponent } from './show-notices.component';

describe('ShowNoticesComponent', () => {
  let component: ShowNoticesComponent;
  let fixture: ComponentFixture<ShowNoticesComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ ShowNoticesComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(ShowNoticesComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
