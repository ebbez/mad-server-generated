import { TestBed } from '@angular/core/testing';

import { sampleWithNewData, sampleWithRequiredData } from '../car-review.test-samples';

import { CarReviewFormService } from './car-review-form.service';

describe('CarReview Form Service', () => {
  let service: CarReviewFormService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(CarReviewFormService);
  });

  describe('Service methods', () => {
    describe('createCarReviewFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createCarReviewFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            review: expect.any(Object),
            customer: expect.any(Object),
            car: expect.any(Object),
          }),
        );
      });

      it('passing ICarReview should create a new form with FormGroup', () => {
        const formGroup = service.createCarReviewFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            review: expect.any(Object),
            customer: expect.any(Object),
            car: expect.any(Object),
          }),
        );
      });
    });

    describe('getCarReview', () => {
      it('should return NewCarReview for default CarReview initial value', () => {
        const formGroup = service.createCarReviewFormGroup(sampleWithNewData);

        const carReview = service.getCarReview(formGroup) as any;

        expect(carReview).toMatchObject(sampleWithNewData);
      });

      it('should return NewCarReview for empty CarReview initial value', () => {
        const formGroup = service.createCarReviewFormGroup();

        const carReview = service.getCarReview(formGroup) as any;

        expect(carReview).toMatchObject({});
      });

      it('should return ICarReview', () => {
        const formGroup = service.createCarReviewFormGroup(sampleWithRequiredData);

        const carReview = service.getCarReview(formGroup) as any;

        expect(carReview).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing ICarReview should not enable id FormControl', () => {
        const formGroup = service.createCarReviewFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewCarReview should disable id FormControl', () => {
        const formGroup = service.createCarReviewFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
