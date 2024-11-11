import { Injectable } from '@angular/core';
import { FormControl, FormGroup, Validators } from '@angular/forms';

import { ICar, NewCar } from '../car.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts ICar for edit and NewCarFormGroupInput for create.
 */
type CarFormGroupInput = ICar | PartialWithRequiredKeyOf<NewCar>;

type CarFormDefaults = Pick<NewCar, 'id'>;

type CarFormGroupContent = {
  id: FormControl<ICar['id'] | NewCar['id']>;
  brand: FormControl<ICar['brand']>;
  model: FormControl<ICar['model']>;
  picture: FormControl<ICar['picture']>;
  pictureContentType: FormControl<ICar['pictureContentType']>;
  fuel: FormControl<ICar['fuel']>;
  options: FormControl<ICar['options']>;
  licensePlate: FormControl<ICar['licensePlate']>;
  engineSize: FormControl<ICar['engineSize']>;
  modelYear: FormControl<ICar['modelYear']>;
  since: FormControl<ICar['since']>;
  price: FormControl<ICar['price']>;
  nrOfSeats: FormControl<ICar['nrOfSeats']>;
  body: FormControl<ICar['body']>;
  longitude: FormControl<ICar['longitude']>;
  latitude: FormControl<ICar['latitude']>;
};

export type CarFormGroup = FormGroup<CarFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class CarFormService {
  createCarFormGroup(car: CarFormGroupInput = { id: null }): CarFormGroup {
    const carRawValue = {
      ...this.getFormDefaults(),
      ...car,
    };
    return new FormGroup<CarFormGroupContent>({
      id: new FormControl(
        { value: carRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        },
      ),
      brand: new FormControl(carRawValue.brand),
      model: new FormControl(carRawValue.model),
      picture: new FormControl(carRawValue.picture),
      pictureContentType: new FormControl(carRawValue.pictureContentType),
      fuel: new FormControl(carRawValue.fuel),
      options: new FormControl(carRawValue.options),
      licensePlate: new FormControl(carRawValue.licensePlate),
      engineSize: new FormControl(carRawValue.engineSize),
      modelYear: new FormControl(carRawValue.modelYear),
      since: new FormControl(carRawValue.since),
      price: new FormControl(carRawValue.price),
      nrOfSeats: new FormControl(carRawValue.nrOfSeats),
      body: new FormControl(carRawValue.body),
      longitude: new FormControl(carRawValue.longitude),
      latitude: new FormControl(carRawValue.latitude),
    });
  }

  getCar(form: CarFormGroup): ICar | NewCar {
    return form.getRawValue() as ICar | NewCar;
  }

  resetForm(form: CarFormGroup, car: CarFormGroupInput): void {
    const carRawValue = { ...this.getFormDefaults(), ...car };
    form.reset(
      {
        ...carRawValue,
        id: { value: carRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */,
    );
  }

  private getFormDefaults(): CarFormDefaults {
    return {
      id: null,
    };
  }
}
