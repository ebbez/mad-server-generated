import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse, provideHttpClient } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Subject, from, of } from 'rxjs';

import { ICustomer } from 'app/entities/customer/customer.model';
import { CustomerService } from 'app/entities/customer/service/customer.service';
import { ICar } from 'app/entities/car/car.model';
import { CarService } from 'app/entities/car/service/car.service';
import { IRental } from '../rental.model';
import { RentalService } from '../service/rental.service';
import { RentalFormService } from './rental-form.service';

import { RentalUpdateComponent } from './rental-update.component';

describe('Rental Management Update Component', () => {
  let comp: RentalUpdateComponent;
  let fixture: ComponentFixture<RentalUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let rentalFormService: RentalFormService;
  let rentalService: RentalService;
  let customerService: CustomerService;
  let carService: CarService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [RentalUpdateComponent],
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
      .overrideTemplate(RentalUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(RentalUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    rentalFormService = TestBed.inject(RentalFormService);
    rentalService = TestBed.inject(RentalService);
    customerService = TestBed.inject(CustomerService);
    carService = TestBed.inject(CarService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('should call Customer query and add missing value', () => {
      const rental: IRental = { id: 17269 };
      const customer: ICustomer = { id: 26915 };
      rental.customer = customer;

      const customerCollection: ICustomer[] = [{ id: 26915 }];
      jest.spyOn(customerService, 'query').mockReturnValue(of(new HttpResponse({ body: customerCollection })));
      const additionalCustomers = [customer];
      const expectedCollection: ICustomer[] = [...additionalCustomers, ...customerCollection];
      jest.spyOn(customerService, 'addCustomerToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ rental });
      comp.ngOnInit();

      expect(customerService.query).toHaveBeenCalled();
      expect(customerService.addCustomerToCollectionIfMissing).toHaveBeenCalledWith(
        customerCollection,
        ...additionalCustomers.map(expect.objectContaining),
      );
      expect(comp.customersSharedCollection).toEqual(expectedCollection);
    });

    it('should call Car query and add missing value', () => {
      const rental: IRental = { id: 17269 };
      const car: ICar = { id: 30624 };
      rental.car = car;

      const carCollection: ICar[] = [{ id: 30624 }];
      jest.spyOn(carService, 'query').mockReturnValue(of(new HttpResponse({ body: carCollection })));
      const additionalCars = [car];
      const expectedCollection: ICar[] = [...additionalCars, ...carCollection];
      jest.spyOn(carService, 'addCarToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ rental });
      comp.ngOnInit();

      expect(carService.query).toHaveBeenCalled();
      expect(carService.addCarToCollectionIfMissing).toHaveBeenCalledWith(carCollection, ...additionalCars.map(expect.objectContaining));
      expect(comp.carsSharedCollection).toEqual(expectedCollection);
    });

    it('should update editForm', () => {
      const rental: IRental = { id: 17269 };
      const customer: ICustomer = { id: 26915 };
      rental.customer = customer;
      const car: ICar = { id: 30624 };
      rental.car = car;

      activatedRoute.data = of({ rental });
      comp.ngOnInit();

      expect(comp.customersSharedCollection).toContainEqual(customer);
      expect(comp.carsSharedCollection).toContainEqual(car);
      expect(comp.rental).toEqual(rental);
    });
  });

  describe('save', () => {
    it('should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IRental>>();
      const rental = { id: 12599 };
      jest.spyOn(rentalFormService, 'getRental').mockReturnValue(rental);
      jest.spyOn(rentalService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ rental });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: rental }));
      saveSubject.complete();

      // THEN
      expect(rentalFormService.getRental).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(rentalService.update).toHaveBeenCalledWith(expect.objectContaining(rental));
      expect(comp.isSaving).toEqual(false);
    });

    it('should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IRental>>();
      const rental = { id: 12599 };
      jest.spyOn(rentalFormService, 'getRental').mockReturnValue({ id: null });
      jest.spyOn(rentalService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ rental: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: rental }));
      saveSubject.complete();

      // THEN
      expect(rentalFormService.getRental).toHaveBeenCalled();
      expect(rentalService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IRental>>();
      const rental = { id: 12599 };
      jest.spyOn(rentalService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ rental });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(rentalService.update).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });

  describe('Compare relationships', () => {
    describe('compareCustomer', () => {
      it('should forward to customerService', () => {
        const entity = { id: 26915 };
        const entity2 = { id: 21032 };
        jest.spyOn(customerService, 'compareCustomer');
        comp.compareCustomer(entity, entity2);
        expect(customerService.compareCustomer).toHaveBeenCalledWith(entity, entity2);
      });
    });

    describe('compareCar', () => {
      it('should forward to carService', () => {
        const entity = { id: 30624 };
        const entity2 = { id: 14019 };
        jest.spyOn(carService, 'compareCar');
        comp.compareCar(entity, entity2);
        expect(carService.compareCar).toHaveBeenCalledWith(entity, entity2);
      });
    });
  });
});
