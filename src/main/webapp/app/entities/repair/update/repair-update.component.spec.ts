import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse, provideHttpClient } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Subject, from, of } from 'rxjs';

import { ICar } from 'app/entities/car/car.model';
import { CarService } from 'app/entities/car/service/car.service';
import { IEmployee } from 'app/entities/employee/employee.model';
import { EmployeeService } from 'app/entities/employee/service/employee.service';
import { IInspection } from 'app/entities/inspection/inspection.model';
import { InspectionService } from 'app/entities/inspection/service/inspection.service';
import { IRepair } from '../repair.model';
import { RepairService } from '../service/repair.service';
import { RepairFormService } from './repair-form.service';

import { RepairUpdateComponent } from './repair-update.component';

describe('Repair Management Update Component', () => {
  let comp: RepairUpdateComponent;
  let fixture: ComponentFixture<RepairUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let repairFormService: RepairFormService;
  let repairService: RepairService;
  let carService: CarService;
  let employeeService: EmployeeService;
  let inspectionService: InspectionService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [RepairUpdateComponent],
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
      .overrideTemplate(RepairUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(RepairUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    repairFormService = TestBed.inject(RepairFormService);
    repairService = TestBed.inject(RepairService);
    carService = TestBed.inject(CarService);
    employeeService = TestBed.inject(EmployeeService);
    inspectionService = TestBed.inject(InspectionService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should call Car query and add missing value', () => {
      const repair: IRepair = { id: 456 };
      const car: ICar = { id: 20428 };
      repair.car = car;

      const carCollection: ICar[] = [{ id: 7855 }];
      jest.spyOn(carService, 'query').mockReturnValue(of(new HttpResponse({ body: carCollection })));
      const additionalCars = [car];
      const expectedCollection: ICar[] = [...additionalCars, ...carCollection];
      jest.spyOn(carService, 'addCarToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ repair });
      comp.ngOnInit();

      expect(carService.query).toHaveBeenCalled();
      expect(carService.addCarToCollectionIfMissing).toHaveBeenCalledWith(carCollection, ...additionalCars.map(expect.objectContaining));
      expect(comp.carsSharedCollection).toEqual(expectedCollection);
    });

    it('Should call Employee query and add missing value', () => {
      const repair: IRepair = { id: 456 };
      const employee: IEmployee = { id: 30185 };
      repair.employee = employee;

      const employeeCollection: IEmployee[] = [{ id: 13226 }];
      jest.spyOn(employeeService, 'query').mockReturnValue(of(new HttpResponse({ body: employeeCollection })));
      const additionalEmployees = [employee];
      const expectedCollection: IEmployee[] = [...additionalEmployees, ...employeeCollection];
      jest.spyOn(employeeService, 'addEmployeeToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ repair });
      comp.ngOnInit();

      expect(employeeService.query).toHaveBeenCalled();
      expect(employeeService.addEmployeeToCollectionIfMissing).toHaveBeenCalledWith(
        employeeCollection,
        ...additionalEmployees.map(expect.objectContaining),
      );
      expect(comp.employeesSharedCollection).toEqual(expectedCollection);
    });

    it('Should call Inspection query and add missing value', () => {
      const repair: IRepair = { id: 456 };
      const inspection: IInspection = { id: 6983 };
      repair.inspection = inspection;

      const inspectionCollection: IInspection[] = [{ id: 3472 }];
      jest.spyOn(inspectionService, 'query').mockReturnValue(of(new HttpResponse({ body: inspectionCollection })));
      const additionalInspections = [inspection];
      const expectedCollection: IInspection[] = [...additionalInspections, ...inspectionCollection];
      jest.spyOn(inspectionService, 'addInspectionToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ repair });
      comp.ngOnInit();

      expect(inspectionService.query).toHaveBeenCalled();
      expect(inspectionService.addInspectionToCollectionIfMissing).toHaveBeenCalledWith(
        inspectionCollection,
        ...additionalInspections.map(expect.objectContaining),
      );
      expect(comp.inspectionsSharedCollection).toEqual(expectedCollection);
    });

    it('Should update editForm', () => {
      const repair: IRepair = { id: 456 };
      const car: ICar = { id: 29398 };
      repair.car = car;
      const employee: IEmployee = { id: 19114 };
      repair.employee = employee;
      const inspection: IInspection = { id: 19330 };
      repair.inspection = inspection;

      activatedRoute.data = of({ repair });
      comp.ngOnInit();

      expect(comp.carsSharedCollection).toContain(car);
      expect(comp.employeesSharedCollection).toContain(employee);
      expect(comp.inspectionsSharedCollection).toContain(inspection);
      expect(comp.repair).toEqual(repair);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IRepair>>();
      const repair = { id: 123 };
      jest.spyOn(repairFormService, 'getRepair').mockReturnValue(repair);
      jest.spyOn(repairService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ repair });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: repair }));
      saveSubject.complete();

      // THEN
      expect(repairFormService.getRepair).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(repairService.update).toHaveBeenCalledWith(expect.objectContaining(repair));
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IRepair>>();
      const repair = { id: 123 };
      jest.spyOn(repairFormService, 'getRepair').mockReturnValue({ id: null });
      jest.spyOn(repairService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ repair: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: repair }));
      saveSubject.complete();

      // THEN
      expect(repairFormService.getRepair).toHaveBeenCalled();
      expect(repairService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IRepair>>();
      const repair = { id: 123 };
      jest.spyOn(repairService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ repair });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(repairService.update).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });

  describe('Compare relationships', () => {
    describe('compareCar', () => {
      it('Should forward to carService', () => {
        const entity = { id: 123 };
        const entity2 = { id: 456 };
        jest.spyOn(carService, 'compareCar');
        comp.compareCar(entity, entity2);
        expect(carService.compareCar).toHaveBeenCalledWith(entity, entity2);
      });
    });

    describe('compareEmployee', () => {
      it('Should forward to employeeService', () => {
        const entity = { id: 123 };
        const entity2 = { id: 456 };
        jest.spyOn(employeeService, 'compareEmployee');
        comp.compareEmployee(entity, entity2);
        expect(employeeService.compareEmployee).toHaveBeenCalledWith(entity, entity2);
      });
    });

    describe('compareInspection', () => {
      it('Should forward to inspectionService', () => {
        const entity = { id: 123 };
        const entity2 = { id: 456 };
        jest.spyOn(inspectionService, 'compareInspection');
        comp.compareInspection(entity, entity2);
        expect(inspectionService.compareInspection).toHaveBeenCalledWith(entity, entity2);
      });
    });
  });
});
