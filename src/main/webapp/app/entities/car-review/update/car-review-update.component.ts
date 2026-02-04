import { Component, OnInit, inject } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import SharedModule from 'app/shared/shared.module';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';

import { ICustomer } from 'app/entities/customer/customer.model';
import { CustomerService } from 'app/entities/customer/service/customer.service';
import { ICar } from 'app/entities/car/car.model';
import { CarService } from 'app/entities/car/service/car.service';
import { CarReviewService } from '../service/car-review.service';
import { ICarReview } from '../car-review.model';
import { CarReviewFormGroup, CarReviewFormService } from './car-review-form.service';

@Component({
  selector: 'jhi-car-review-update',
  templateUrl: './car-review-update.component.html',
  imports: [SharedModule, FormsModule, ReactiveFormsModule],
})
export class CarReviewUpdateComponent implements OnInit {
  isSaving = false;
  carReview: ICarReview | null = null;

  customersSharedCollection: ICustomer[] = [];
  carsSharedCollection: ICar[] = [];

  protected carReviewService = inject(CarReviewService);
  protected carReviewFormService = inject(CarReviewFormService);
  protected customerService = inject(CustomerService);
  protected carService = inject(CarService);
  protected activatedRoute = inject(ActivatedRoute);

  // eslint-disable-next-line @typescript-eslint/member-ordering
  editForm: CarReviewFormGroup = this.carReviewFormService.createCarReviewFormGroup();

  compareCustomer = (o1: ICustomer | null, o2: ICustomer | null): boolean => this.customerService.compareCustomer(o1, o2);

  compareCar = (o1: ICar | null, o2: ICar | null): boolean => this.carService.compareCar(o1, o2);

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ carReview }) => {
      this.carReview = carReview;
      if (carReview) {
        this.updateForm(carReview);
      }

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const carReview = this.carReviewFormService.getCarReview(this.editForm);
    if (carReview.id !== null) {
      this.subscribeToSaveResponse(this.carReviewService.update(carReview));
    } else {
      this.subscribeToSaveResponse(this.carReviewService.create(carReview));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<ICarReview>>): void {
    result.pipe(finalize(() => this.onSaveFinalize())).subscribe({
      next: () => this.onSaveSuccess(),
      error: () => this.onSaveError(),
    });
  }

  protected onSaveSuccess(): void {
    this.previousState();
  }

  protected onSaveError(): void {
    // Api for inheritance.
  }

  protected onSaveFinalize(): void {
    this.isSaving = false;
  }

  protected updateForm(carReview: ICarReview): void {
    this.carReview = carReview;
    this.carReviewFormService.resetForm(this.editForm, carReview);

    this.customersSharedCollection = this.customerService.addCustomerToCollectionIfMissing<ICustomer>(
      this.customersSharedCollection,
      carReview.customer,
    );
    this.carsSharedCollection = this.carService.addCarToCollectionIfMissing<ICar>(this.carsSharedCollection, carReview.car);
  }

  protected loadRelationshipsOptions(): void {
    this.customerService
      .query()
      .pipe(map((res: HttpResponse<ICustomer[]>) => res.body ?? []))
      .pipe(
        map((customers: ICustomer[]) =>
          this.customerService.addCustomerToCollectionIfMissing<ICustomer>(customers, this.carReview?.customer),
        ),
      )
      .subscribe((customers: ICustomer[]) => (this.customersSharedCollection = customers));

    this.carService
      .query()
      .pipe(map((res: HttpResponse<ICar[]>) => res.body ?? []))
      .pipe(map((cars: ICar[]) => this.carService.addCarToCollectionIfMissing<ICar>(cars, this.carReview?.car)))
      .subscribe((cars: ICar[]) => (this.carsSharedCollection = cars));
  }
}
