import { TestBed } from '@angular/core/testing';

import { sampleWithNewData, sampleWithRequiredData } from '../inspection.test-samples';

import { InspectionFormService } from './inspection-form.service';

describe('Inspection Form Service', () => {
  let service: InspectionFormService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(InspectionFormService);
  });

  describe('Service methods', () => {
    describe('createInspectionFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createInspectionFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            code: expect.any(Object),
            odometer: expect.any(Object),
            result: expect.any(Object),
            description: expect.any(Object),
            photo: expect.any(Object),
            completed: expect.any(Object),
            car: expect.any(Object),
            employee: expect.any(Object),
            rental: expect.any(Object),
          }),
        );
      });

      it('passing IInspection should create a new form with FormGroup', () => {
        const formGroup = service.createInspectionFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            code: expect.any(Object),
            odometer: expect.any(Object),
            result: expect.any(Object),
            description: expect.any(Object),
            photo: expect.any(Object),
            completed: expect.any(Object),
            car: expect.any(Object),
            employee: expect.any(Object),
            rental: expect.any(Object),
          }),
        );
      });
    });

    describe('getInspection', () => {
      it('should return NewInspection for default Inspection initial value', () => {
        const formGroup = service.createInspectionFormGroup(sampleWithNewData);

        const inspection = service.getInspection(formGroup) as any;

        expect(inspection).toMatchObject(sampleWithNewData);
      });

      it('should return NewInspection for empty Inspection initial value', () => {
        const formGroup = service.createInspectionFormGroup();

        const inspection = service.getInspection(formGroup) as any;

        expect(inspection).toMatchObject({});
      });

      it('should return IInspection', () => {
        const formGroup = service.createInspectionFormGroup(sampleWithRequiredData);

        const inspection = service.getInspection(formGroup) as any;

        expect(inspection).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing IInspection should not enable id FormControl', () => {
        const formGroup = service.createInspectionFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewInspection should disable id FormControl', () => {
        const formGroup = service.createInspectionFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
