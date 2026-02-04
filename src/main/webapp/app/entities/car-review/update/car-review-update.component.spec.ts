import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse, provideHttpClient } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Subject, from, of } from 'rxjs';

import { ICustomer } from 'app/entities/customer/customer.model';
import { CustomerService } from 'app/entities/customer/service/customer.service';
import { ICar } from 'app/entities/car/car.model';
import { CarService } from 'app/entities/car/service/car.service';
import { ICarReview } from '../car-review.model';
import { CarReviewService } from '../service/car-review.service';
import { CarReviewFormService } from './car-review-form.service';

import { CarReviewUpdateComponent } from './car-review-update.component';

describe('CarReview Management Update Component', () => {
  let comp: CarReviewUpdateComponent;
  let fixture: ComponentFixture<CarReviewUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let carReviewFormService: CarReviewFormService;
  let carReviewService: CarReviewService;
  let customerService: CustomerService;
  let carService: CarService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [CarReviewUpdateComponent],
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
      .overrideTemplate(CarReviewUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(CarReviewUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    carReviewFormService = TestBed.inject(CarReviewFormService);
    carReviewService = TestBed.inject(CarReviewService);
    customerService = TestBed.inject(CustomerService);
    carService = TestBed.inject(CarService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('should call Customer query and add missing value', () => {
      const carReview: ICarReview = { id: 3042 };
      const customer: ICustomer = { id: 26915 };
      carReview.customer = customer;

      const customerCollection: ICustomer[] = [{ id: 26915 }];
      jest.spyOn(customerService, 'query').mockReturnValue(of(new HttpResponse({ body: customerCollection })));
      const additionalCustomers = [customer];
      const expectedCollection: ICustomer[] = [...additionalCustomers, ...customerCollection];
      jest.spyOn(customerService, 'addCustomerToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ carReview });
      comp.ngOnInit();

      expect(customerService.query).toHaveBeenCalled();
      expect(customerService.addCustomerToCollectionIfMissing).toHaveBeenCalledWith(
        customerCollection,
        ...additionalCustomers.map(expect.objectContaining),
      );
      expect(comp.customersSharedCollection).toEqual(expectedCollection);
    });

    it('should call Car query and add missing value', () => {
      const carReview: ICarReview = { id: 3042 };
      const car: ICar = { id: 30624 };
      carReview.car = car;

      const carCollection: ICar[] = [{ id: 30624 }];
      jest.spyOn(carService, 'query').mockReturnValue(of(new HttpResponse({ body: carCollection })));
      const additionalCars = [car];
      const expectedCollection: ICar[] = [...additionalCars, ...carCollection];
      jest.spyOn(carService, 'addCarToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ carReview });
      comp.ngOnInit();

      expect(carService.query).toHaveBeenCalled();
      expect(carService.addCarToCollectionIfMissing).toHaveBeenCalledWith(carCollection, ...additionalCars.map(expect.objectContaining));
      expect(comp.carsSharedCollection).toEqual(expectedCollection);
    });

    it('should update editForm', () => {
      const carReview: ICarReview = { id: 3042 };
      const customer: ICustomer = { id: 26915 };
      carReview.customer = customer;
      const car: ICar = { id: 30624 };
      carReview.car = car;

      activatedRoute.data = of({ carReview });
      comp.ngOnInit();

      expect(comp.customersSharedCollection).toContainEqual(customer);
      expect(comp.carsSharedCollection).toContainEqual(car);
      expect(comp.carReview).toEqual(carReview);
    });
  });

  describe('save', () => {
    it('should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<ICarReview>>();
      const carReview = { id: 28227 };
      jest.spyOn(carReviewFormService, 'getCarReview').mockReturnValue(carReview);
      jest.spyOn(carReviewService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ carReview });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: carReview }));
      saveSubject.complete();

      // THEN
      expect(carReviewFormService.getCarReview).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(carReviewService.update).toHaveBeenCalledWith(expect.objectContaining(carReview));
      expect(comp.isSaving).toEqual(false);
    });

    it('should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<ICarReview>>();
      const carReview = { id: 28227 };
      jest.spyOn(carReviewFormService, 'getCarReview').mockReturnValue({ id: null });
      jest.spyOn(carReviewService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ carReview: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: carReview }));
      saveSubject.complete();

      // THEN
      expect(carReviewFormService.getCarReview).toHaveBeenCalled();
      expect(carReviewService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<ICarReview>>();
      const carReview = { id: 28227 };
      jest.spyOn(carReviewService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ carReview });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(carReviewService.update).toHaveBeenCalled();
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
