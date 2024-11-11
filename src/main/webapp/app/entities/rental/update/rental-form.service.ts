import { Injectable } from '@angular/core';
import { FormControl, FormGroup, Validators } from '@angular/forms';

import { IRental, NewRental } from '../rental.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IRental for edit and NewRentalFormGroupInput for create.
 */
type RentalFormGroupInput = IRental | PartialWithRequiredKeyOf<NewRental>;

type RentalFormDefaults = Pick<NewRental, 'id'>;

type RentalFormGroupContent = {
  id: FormControl<IRental['id'] | NewRental['id']>;
  code: FormControl<IRental['code']>;
  longitude: FormControl<IRental['longitude']>;
  latitude: FormControl<IRental['latitude']>;
  fromDate: FormControl<IRental['fromDate']>;
  toDate: FormControl<IRental['toDate']>;
  state: FormControl<IRental['state']>;
  customer: FormControl<IRental['customer']>;
  car: FormControl<IRental['car']>;
};

export type RentalFormGroup = FormGroup<RentalFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class RentalFormService {
  createRentalFormGroup(rental: RentalFormGroupInput = { id: null }): RentalFormGroup {
    const rentalRawValue = {
      ...this.getFormDefaults(),
      ...rental,
    };
    return new FormGroup<RentalFormGroupContent>({
      id: new FormControl(
        { value: rentalRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        },
      ),
      code: new FormControl(rentalRawValue.code),
      longitude: new FormControl(rentalRawValue.longitude),
      latitude: new FormControl(rentalRawValue.latitude),
      fromDate: new FormControl(rentalRawValue.fromDate),
      toDate: new FormControl(rentalRawValue.toDate),
      state: new FormControl(rentalRawValue.state),
      customer: new FormControl(rentalRawValue.customer),
      car: new FormControl(rentalRawValue.car),
    });
  }

  getRental(form: RentalFormGroup): IRental | NewRental {
    return form.getRawValue() as IRental | NewRental;
  }

  resetForm(form: RentalFormGroup, rental: RentalFormGroupInput): void {
    const rentalRawValue = { ...this.getFormDefaults(), ...rental };
    form.reset(
      {
        ...rentalRawValue,
        id: { value: rentalRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */,
    );
  }

  private getFormDefaults(): RentalFormDefaults {
    return {
      id: null,
    };
  }
}
