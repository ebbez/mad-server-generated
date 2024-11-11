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
import { RentalState } from 'app/entities/enumerations/rental-state.model';
import { RentalService } from '../service/rental.service';
import { IRental } from '../rental.model';
import { RentalFormGroup, RentalFormService } from './rental-form.service';

@Component({
  standalone: true,
  selector: 'jhi-rental-update',
  templateUrl: './rental-update.component.html',
  imports: [SharedModule, FormsModule, ReactiveFormsModule],
})
export class RentalUpdateComponent implements OnInit {
  isSaving = false;
  rental: IRental | null = null;
  rentalStateValues = Object.keys(RentalState);

  customersSharedCollection: ICustomer[] = [];
  carsSharedCollection: ICar[] = [];

  protected rentalService = inject(RentalService);
  protected rentalFormService = inject(RentalFormService);
  protected customerService = inject(CustomerService);
  protected carService = inject(CarService);
  protected activatedRoute = inject(ActivatedRoute);

  // eslint-disable-next-line @typescript-eslint/member-ordering
  editForm: RentalFormGroup = this.rentalFormService.createRentalFormGroup();

  compareCustomer = (o1: ICustomer | null, o2: ICustomer | null): boolean => this.customerService.compareCustomer(o1, o2);

  compareCar = (o1: ICar | null, o2: ICar | null): boolean => this.carService.compareCar(o1, o2);

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ rental }) => {
      this.rental = rental;
      if (rental) {
        this.updateForm(rental);
      }

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const rental = this.rentalFormService.getRental(this.editForm);
    if (rental.id !== null) {
      this.subscribeToSaveResponse(this.rentalService.update(rental));
    } else {
      this.subscribeToSaveResponse(this.rentalService.create(rental));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IRental>>): void {
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

  protected updateForm(rental: IRental): void {
    this.rental = rental;
    this.rentalFormService.resetForm(this.editForm, rental);

    this.customersSharedCollection = this.customerService.addCustomerToCollectionIfMissing<ICustomer>(
      this.customersSharedCollection,
      rental.customer,
    );
    this.carsSharedCollection = this.carService.addCarToCollectionIfMissing<ICar>(this.carsSharedCollection, rental.car);
  }

  protected loadRelationshipsOptions(): void {
    this.customerService
      .query()
      .pipe(map((res: HttpResponse<ICustomer[]>) => res.body ?? []))
      .pipe(
        map((customers: ICustomer[]) => this.customerService.addCustomerToCollectionIfMissing<ICustomer>(customers, this.rental?.customer)),
      )
      .subscribe((customers: ICustomer[]) => (this.customersSharedCollection = customers));

    this.carService
      .query()
      .pipe(map((res: HttpResponse<ICar[]>) => res.body ?? []))
      .pipe(map((cars: ICar[]) => this.carService.addCarToCollectionIfMissing<ICar>(cars, this.rental?.car)))
      .subscribe((cars: ICar[]) => (this.carsSharedCollection = cars));
  }
}
