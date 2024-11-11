import { TestBed } from '@angular/core/testing';

import { sampleWithNewData, sampleWithRequiredData } from '../inspection-photo.test-samples';

import { InspectionPhotoFormService } from './inspection-photo-form.service';

describe('InspectionPhoto Form Service', () => {
  let service: InspectionPhotoFormService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(InspectionPhotoFormService);
  });

  describe('Service methods', () => {
    describe('createInspectionPhotoFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createInspectionPhotoFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            photo: expect.any(Object),
            inspection: expect.any(Object),
          }),
        );
      });

      it('passing IInspectionPhoto should create a new form with FormGroup', () => {
        const formGroup = service.createInspectionPhotoFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            photo: expect.any(Object),
            inspection: expect.any(Object),
          }),
        );
      });
    });

    describe('getInspectionPhoto', () => {
      it('should return NewInspectionPhoto for default InspectionPhoto initial value', () => {
        const formGroup = service.createInspectionPhotoFormGroup(sampleWithNewData);

        const inspectionPhoto = service.getInspectionPhoto(formGroup) as any;

        expect(inspectionPhoto).toMatchObject(sampleWithNewData);
      });

      it('should return NewInspectionPhoto for empty InspectionPhoto initial value', () => {
        const formGroup = service.createInspectionPhotoFormGroup();

        const inspectionPhoto = service.getInspectionPhoto(formGroup) as any;

        expect(inspectionPhoto).toMatchObject({});
      });

      it('should return IInspectionPhoto', () => {
        const formGroup = service.createInspectionPhotoFormGroup(sampleWithRequiredData);

        const inspectionPhoto = service.getInspectionPhoto(formGroup) as any;

        expect(inspectionPhoto).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing IInspectionPhoto should not enable id FormControl', () => {
        const formGroup = service.createInspectionPhotoFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewInspectionPhoto should disable id FormControl', () => {
        const formGroup = service.createInspectionPhotoFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
