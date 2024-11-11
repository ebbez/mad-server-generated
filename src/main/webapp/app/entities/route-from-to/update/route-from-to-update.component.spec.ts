import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse, provideHttpClient } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Subject, from, of } from 'rxjs';

import { IEmployee } from 'app/entities/employee/employee.model';
import { EmployeeService } from 'app/entities/employee/service/employee.service';
import { RouteFromToService } from '../service/route-from-to.service';
import { IRouteFromTo } from '../route-from-to.model';
import { RouteFromToFormService } from './route-from-to-form.service';

import { RouteFromToUpdateComponent } from './route-from-to-update.component';

describe('RouteFromTo Management Update Component', () => {
  let comp: RouteFromToUpdateComponent;
  let fixture: ComponentFixture<RouteFromToUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let routeFromToFormService: RouteFromToFormService;
  let routeFromToService: RouteFromToService;
  let employeeService: EmployeeService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [RouteFromToUpdateComponent],
      providers: [
        provideHttpClient(),
        FormBuilder,
        {
          provide: ActivatedRoute,
          useValue: {
            params: from([{}]),
          },
        },
      ],
    })
      .overrideTemplate(RouteFromToUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(RouteFromToUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    routeFromToFormService = TestBed.inject(RouteFromToFormService);
    routeFromToService = TestBed.inject(RouteFromToService);
    employeeService = TestBed.inject(EmployeeService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should call Employee query and add missing value', () => {
      const routeFromTo: IRouteFromTo = { id: 456 };
      const employee: IEmployee = { id: 17740 };
      routeFromTo.employee = employee;

      const employeeCollection: IEmployee[] = [{ id: 11008 }];
      jest.spyOn(employeeService, 'query').mockReturnValue(of(new HttpResponse({ body: employeeCollection })));
      const additionalEmployees = [employee];
      const expectedCollection: IEmployee[] = [...additionalEmployees, ...employeeCollection];
      jest.spyOn(employeeService, 'addEmployeeToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ routeFromTo });
      comp.ngOnInit();

      expect(employeeService.query).toHaveBeenCalled();
      expect(employeeService.addEmployeeToCollectionIfMissing).toHaveBeenCalledWith(
        employeeCollection,
        ...additionalEmployees.map(expect.objectContaining),
      );
      expect(comp.employeesSharedCollection).toEqual(expectedCollection);
    });

    it('Should update editForm', () => {
      const routeFromTo: IRouteFromTo = { id: 456 };
      const employee: IEmployee = { id: 32646 };
      routeFromTo.employee = employee;

      activatedRoute.data = of({ routeFromTo });
      comp.ngOnInit();

      expect(comp.employeesSharedCollection).toContain(employee);
      expect(comp.routeFromTo).toEqual(routeFromTo);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IRouteFromTo>>();
      const routeFromTo = { id: 123 };
      jest.spyOn(routeFromToFormService, 'getRouteFromTo').mockReturnValue(routeFromTo);
      jest.spyOn(routeFromToService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ routeFromTo });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: routeFromTo }));
      saveSubject.complete();

      // THEN
      expect(routeFromToFormService.getRouteFromTo).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(routeFromToService.update).toHaveBeenCalledWith(expect.objectContaining(routeFromTo));
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IRouteFromTo>>();
      const routeFromTo = { id: 123 };
      jest.spyOn(routeFromToFormService, 'getRouteFromTo').mockReturnValue({ id: null });
      jest.spyOn(routeFromToService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ routeFromTo: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: routeFromTo }));
      saveSubject.complete();

      // THEN
      expect(routeFromToFormService.getRouteFromTo).toHaveBeenCalled();
      expect(routeFromToService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IRouteFromTo>>();
      const routeFromTo = { id: 123 };
      jest.spyOn(routeFromToService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ routeFromTo });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(routeFromToService.update).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });

  describe('Compare relationships', () => {
    describe('compareEmployee', () => {
      it('Should forward to employeeService', () => {
        const entity = { id: 123 };
        const entity2 = { id: 456 };
        jest.spyOn(employeeService, 'compareEmployee');
        comp.compareEmployee(entity, entity2);
        expect(employeeService.compareEmployee).toHaveBeenCalledWith(entity, entity2);
      });
    });
  });
});
