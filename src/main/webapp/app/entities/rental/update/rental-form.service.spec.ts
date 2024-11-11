import { TestBed } from '@angular/core/testing';

import { sampleWithNewData, sampleWithRequiredData } from '../rental.test-samples';

import { RentalFormService } from './rental-form.service';

describe('Rental Form Service', () => {
  let service: RentalFormService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(RentalFormService);
  });

  describe('Service methods', () => {
    describe('createRentalFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createRentalFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            code: expect.any(Object),
            longitude: expect.any(Object),
            latitude: expect.any(Object),
            fromDate: expect.any(Object),
            toDate: expect.any(Object),
            state: expect.any(Object),
            customer: expect.any(Object),
            car: expect.any(Object),
          }),
        );
      });

      it('passing IRental should create a new form with FormGroup', () => {
        const formGroup = service.createRentalFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            code: expect.any(Object),
            longitude: expect.any(Object),
            latitude: expect.any(Object),
            fromDate: expect.any(Object),
            toDate: expect.any(Object),
            state: expect.any(Object),
            customer: expect.any(Object),
            car: expect.any(Object),
          }),
        );
      });
    });

    describe('getRental', () => {
      it('should return NewRental for default Rental initial value', () => {
        const formGroup = service.createRentalFormGroup(sampleWithNewData);

        const rental = service.getRental(formGroup) as any;

        expect(rental).toMatchObject(sampleWithNewData);
      });

      it('should return NewRental for empty Rental initial value', () => {
        const formGroup = service.createRentalFormGroup();

        const rental = service.getRental(formGroup) as any;

        expect(rental).toMatchObject({});
      });

      it('should return IRental', () => {
        const formGroup = service.createRentalFormGroup(sampleWithRequiredData);

        const rental = service.getRental(formGroup) as any;

        expect(rental).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing IRental should not enable id FormControl', () => {
        const formGroup = service.createRentalFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewRental should disable id FormControl', () => {
        const formGroup = service.createRentalFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
