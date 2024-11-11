import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse, provideHttpClient } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Subject, from, of } from 'rxjs';

import { ICar } from 'app/entities/car/car.model';
import { CarService } from 'app/entities/car/service/car.service';
import { IEmployee } from 'app/entities/employee/employee.model';
import { EmployeeService } from 'app/entities/employee/service/employee.service';
import { IRental } from 'app/entities/rental/rental.model';
import { RentalService } from 'app/entities/rental/service/rental.service';
import { IInspection } from '../inspection.model';
import { InspectionService } from '../service/inspection.service';
import { InspectionFormService } from './inspection-form.service';

import { InspectionUpdateComponent } from './inspection-update.component';

describe('Inspection Management Update Component', () => {
  let comp: InspectionUpdateComponent;
  let fixture: ComponentFixture<InspectionUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let inspectionFormService: InspectionFormService;
  let inspectionService: InspectionService;
  let carService: CarService;
  let employeeService: EmployeeService;
  let rentalService: RentalService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [InspectionUpdateComponent],
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
      .overrideTemplate(InspectionUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(InspectionUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    inspectionFormService = TestBed.inject(InspectionFormService);
    inspectionService = TestBed.inject(InspectionService);
    carService = TestBed.inject(CarService);
    employeeService = TestBed.inject(EmployeeService);
    rentalService = TestBed.inject(RentalService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should call Car query and add missing value', () => {
      const inspection: IInspection = { id: 456 };
      const car: ICar = { id: 11413 };
      inspection.car = car;

      const carCollection: ICar[] = [{ id: 25128 }];
      jest.spyOn(carService, 'query').mockReturnValue(of(new HttpResponse({ body: carCollection })));
      const additionalCars = [car];
      const expectedCollection: ICar[] = [...additionalCars, ...carCollection];
      jest.spyOn(carService, 'addCarToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ inspection });
      comp.ngOnInit();

      expect(carService.query).toHaveBeenCalled();
      expect(carService.addCarToCollectionIfMissing).toHaveBeenCalledWith(carCollection, ...additionalCars.map(expect.objectContaining));
      expect(comp.carsSharedCollection).toEqual(expectedCollection);
    });

    it('Should call Employee query and add missing value', () => {
      const inspection: IInspection = { id: 456 };
      const employee: IEmployee = { id: 11322 };
      inspection.employee = employee;

      const employeeCollection: IEmployee[] = [{ id: 10091 }];
      jest.spyOn(employeeService, 'query').mockReturnValue(of(new HttpResponse({ body: employeeCollection })));
      const additionalEmployees = [employee];
      const expectedCollection: IEmployee[] = [...additionalEmployees, ...employeeCollection];
      jest.spyOn(employeeService, 'addEmployeeToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ inspection });
      comp.ngOnInit();

      expect(employeeService.query).toHaveBeenCalled();
      expect(employeeService.addEmployeeToCollectionIfMissing).toHaveBeenCalledWith(
        employeeCollection,
        ...additionalEmployees.map(expect.objectContaining),
      );
      expect(comp.employeesSharedCollection).toEqual(expectedCollection);
    });

    it('Should call Rental query and add missing value', () => {
      const inspection: IInspection = { id: 456 };
      const rental: IRental = { id: 7229 };
      inspection.rental = rental;

      const rentalCollection: IRental[] = [{ id: 14149 }];
      jest.spyOn(rentalService, 'query').mockReturnValue(of(new HttpResponse({ body: rentalCollection })));
      const additionalRentals = [rental];
      const expectedCollection: IRental[] = [...additionalRentals, ...rentalCollection];
      jest.spyOn(rentalService, 'addRentalToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ inspection });
      comp.ngOnInit();

      expect(rentalService.query).toHaveBeenCalled();
      expect(rentalService.addRentalToCollectionIfMissing).toHaveBeenCalledWith(
        rentalCollection,
        ...additionalRentals.map(expect.objectContaining),
      );
      expect(comp.rentalsSharedCollection).toEqual(expectedCollection);
    });

    it('Should update editForm', () => {
      const inspection: IInspection = { id: 456 };
      const car: ICar = { id: 31092 };
      inspection.car = car;
      const employee: IEmployee = { id: 2551 };
      inspection.employee = employee;
      const rental: IRental = { id: 1355 };
      inspection.rental = rental;

      activatedRoute.data = of({ inspection });
      comp.ngOnInit();

      expect(comp.carsSharedCollection).toContain(car);
      expect(comp.employeesSharedCollection).toContain(employee);
      expect(comp.rentalsSharedCollection).toContain(rental);
      expect(comp.inspection).toEqual(inspection);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IInspection>>();
      const inspection = { id: 123 };
      jest.spyOn(inspectionFormService, 'getInspection').mockReturnValue(inspection);
      jest.spyOn(inspectionService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ inspection });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: inspection }));
      saveSubject.complete();

      // THEN
      expect(inspectionFormService.getInspection).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(inspectionService.update).toHaveBeenCalledWith(expect.objectContaining(inspection));
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IInspection>>();
      const inspection = { id: 123 };
      jest.spyOn(inspectionFormService, 'getInspection').mockReturnValue({ id: null });
      jest.spyOn(inspectionService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ inspection: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: inspection }));
      saveSubject.complete();

      // THEN
      expect(inspectionFormService.getInspection).toHaveBeenCalled();
      expect(inspectionService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IInspection>>();
      const inspection = { id: 123 };
      jest.spyOn(inspectionService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ inspection });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(inspectionService.update).toHaveBeenCalled();
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

    describe('compareRental', () => {
      it('Should forward to rentalService', () => {
        const entity = { id: 123 };
        const entity2 = { id: 456 };
        jest.spyOn(rentalService, 'compareRental');
        comp.compareRental(entity, entity2);
        expect(rentalService.compareRental).toHaveBeenCalledWith(entity, entity2);
      });
    });
  });
});
