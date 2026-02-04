import { ComponentFixture, TestBed } from '@angular/core/testing';
import { provideRouter, withComponentInputBinding } from '@angular/router';
import { RouterTestingHarness } from '@angular/router/testing';
import { of } from 'rxjs';

import { CarReviewDetailComponent } from './car-review-detail.component';

describe('CarReview Management Detail Component', () => {
  let comp: CarReviewDetailComponent;
  let fixture: ComponentFixture<CarReviewDetailComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [CarReviewDetailComponent],
      providers: [
        provideRouter(
          [
            {
              path: '**',
              loadComponent: () => import('./car-review-detail.component').then(m => m.CarReviewDetailComponent),
              resolve: { carReview: () => of({ id: 28227 }) },
            },
          ],
          withComponentInputBinding(),
        ),
      ],
    })
      .overrideTemplate(CarReviewDetailComponent, '')
      .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(CarReviewDetailComponent);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('should load carReview on init', async () => {
      const harness = await RouterTestingHarness.create();
      const instance = await harness.navigateByUrl('/', CarReviewDetailComponent);

      // THEN
      expect(instance.carReview()).toEqual(expect.objectContaining({ id: 28227 }));
    });
  });

  describe('PreviousState', () => {
    it('should navigate to previous state', () => {
      jest.spyOn(window.history, 'back');
      comp.previousState();
      expect(window.history.back).toHaveBeenCalled();
    });
  });
});
