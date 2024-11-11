import { TestBed } from '@angular/core/testing';

import { sampleWithNewData, sampleWithRequiredData } from '../route-from-to.test-samples';

import { RouteFromToFormService } from './route-from-to-form.service';

describe('RouteFromTo Form Service', () => {
  let service: RouteFromToFormService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(RouteFromToFormService);
  });

  describe('Service methods', () => {
    describe('createRouteFromToFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createRouteFromToFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            code: expect.any(Object),
            description: expect.any(Object),
            date: expect.any(Object),
            employee: expect.any(Object),
          }),
        );
      });

      it('passing IRouteFromTo should create a new form with FormGroup', () => {
        const formGroup = service.createRouteFromToFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            code: expect.any(Object),
            description: expect.any(Object),
            date: expect.any(Object),
            employee: expect.any(Object),
          }),
        );
      });
    });

    describe('getRouteFromTo', () => {
      it('should return NewRouteFromTo for default RouteFromTo initial value', () => {
        const formGroup = service.createRouteFromToFormGroup(sampleWithNewData);

        const routeFromTo = service.getRouteFromTo(formGroup) as any;

        expect(routeFromTo).toMatchObject(sampleWithNewData);
      });

      it('should return NewRouteFromTo for empty RouteFromTo initial value', () => {
        const formGroup = service.createRouteFromToFormGroup();

        const routeFromTo = service.getRouteFromTo(formGroup) as any;

        expect(routeFromTo).toMatchObject({});
      });

      it('should return IRouteFromTo', () => {
        const formGroup = service.createRouteFromToFormGroup(sampleWithRequiredData);

        const routeFromTo = service.getRouteFromTo(formGroup) as any;

        expect(routeFromTo).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing IRouteFromTo should not enable id FormControl', () => {
        const formGroup = service.createRouteFromToFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewRouteFromTo should disable id FormControl', () => {
        const formGroup = service.createRouteFromToFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
