import { TestBed } from '@angular/core/testing';

import { sampleWithNewData, sampleWithRequiredData } from '../repair.test-samples';

import { RepairFormService } from './repair-form.service';

describe('Repair Form Service', () => {
  let service: RepairFormService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(RepairFormService);
  });

  describe('Service methods', () => {
    describe('createRepairFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createRepairFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            description: expect.any(Object),
            repairStatus: expect.any(Object),
            dateCompleted: expect.any(Object),
            car: expect.any(Object),
            employee: expect.any(Object),
            inspection: expect.any(Object),
          }),
        );
      });

      it('passing IRepair should create a new form with FormGroup', () => {
        const formGroup = service.createRepairFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            description: expect.any(Object),
            repairStatus: expect.any(Object),
            dateCompleted: expect.any(Object),
            car: expect.any(Object),
            employee: expect.any(Object),
            inspection: expect.any(Object),
          }),
        );
      });
    });

    describe('getRepair', () => {
      it('should return NewRepair for default Repair initial value', () => {
        const formGroup = service.createRepairFormGroup(sampleWithNewData);

        const repair = service.getRepair(formGroup) as any;

        expect(repair).toMatchObject(sampleWithNewData);
      });

      it('should return NewRepair for empty Repair initial value', () => {
        const formGroup = service.createRepairFormGroup();

        const repair = service.getRepair(formGroup) as any;

        expect(repair).toMatchObject({});
      });

      it('should return IRepair', () => {
        const formGroup = service.createRepairFormGroup(sampleWithRequiredData);

        const repair = service.getRepair(formGroup) as any;

        expect(repair).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing IRepair should not enable id FormControl', () => {
        const formGroup = service.createRepairFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewRepair should disable id FormControl', () => {
        const formGroup = service.createRepairFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
