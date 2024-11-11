import { TestBed } from '@angular/core/testing';

import { sampleWithNewData, sampleWithRequiredData } from '../route-stop.test-samples';

import { RouteStopFormService } from './route-stop-form.service';

describe('RouteStop Form Service', () => {
  let service: RouteStopFormService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(RouteStopFormService);
  });

  describe('Service methods', () => {
    describe('createRouteStopFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createRouteStopFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            nr: expect.any(Object),
            routeFromTo: expect.any(Object),
            location: expect.any(Object),
          }),
        );
      });

      it('passing IRouteStop should create a new form with FormGroup', () => {
        const formGroup = service.createRouteStopFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            nr: expect.any(Object),
            routeFromTo: expect.any(Object),
            location: expect.any(Object),
          }),
        );
      });
    });

    describe('getRouteStop', () => {
      it('should return NewRouteStop for default RouteStop initial value', () => {
        const formGroup = service.createRouteStopFormGroup(sampleWithNewData);

        const routeStop = service.getRouteStop(formGroup) as any;

        expect(routeStop).toMatchObject(sampleWithNewData);
      });

      it('should return NewRouteStop for empty RouteStop initial value', () => {
        const formGroup = service.createRouteStopFormGroup();

        const routeStop = service.getRouteStop(formGroup) as any;

        expect(routeStop).toMatchObject({});
      });

      it('should return IRouteStop', () => {
        const formGroup = service.createRouteStopFormGroup(sampleWithRequiredData);

        const routeStop = service.getRouteStop(formGroup) as any;

        expect(routeStop).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing IRouteStop should not enable id FormControl', () => {
        const formGroup = service.createRouteStopFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewRouteStop should disable id FormControl', () => {
        const formGroup = service.createRouteStopFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
