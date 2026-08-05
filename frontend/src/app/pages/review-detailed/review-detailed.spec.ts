import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ReviewDetailed } from './review-detailed';

describe('ReviewDetailed', () => {
  let component: ReviewDetailed;
  let fixture: ComponentFixture<ReviewDetailed>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [ReviewDetailed],
    }).compileComponents();

    fixture = TestBed.createComponent(ReviewDetailed);
    component = fixture.componentInstance;
    await fixture.whenStable();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
