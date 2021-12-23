import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ShowChatsComponent } from './show-chats.component';

describe('ShowChatsComponent', () => {
  let component: ShowChatsComponent;
  let fixture: ComponentFixture<ShowChatsComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ ShowChatsComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(ShowChatsComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
