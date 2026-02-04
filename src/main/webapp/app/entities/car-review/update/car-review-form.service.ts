import { Injectable } from '@angular/core';
import { FormControl, FormGroup, Validators } from '@angular/forms';

import { ICarReview, NewCarReview } from '../car-review.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts ICarReview for edit and NewCarReviewFormGroupInput for create.
 */
type CarReviewFormGroupInput = ICarReview | PartialWithRequiredKeyOf<NewCarReview>;

type CarReviewFormDefaults = Pick<NewCarReview, 'id'>;

type CarReviewFormGroupContent = {
  id: FormControl<ICarReview['id'] | NewCarReview['id']>;
  review: FormControl<ICarReview['review']>;
  customer: FormControl<ICarReview['customer']>;
  car: FormControl<ICarReview['car']>;
};

export type CarReviewFormGroup = FormGroup<CarReviewFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class CarReviewFormService {
  createCarReviewFormGroup(carReview: CarReviewFormGroupInput = { id: null }): CarReviewFormGroup {
    const carReviewRawValue = {
      ...this.getFormDefaults(),
      ...carReview,
    };
    return new FormGroup<CarReviewFormGroupContent>({
      id: new FormControl(
        { value: carReviewRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        },
      ),
      review: new FormControl(carReviewRawValue.review),
      customer: new FormControl(carReviewRawValue.customer),
      car: new FormControl(carReviewRawValue.car),
    });
  }

  getCarReview(form: CarReviewFormGroup): ICarReview | NewCarReview {
    return form.getRawValue() as ICarReview | NewCarReview;
  }

  resetForm(form: CarReviewFormGroup, carReview: CarReviewFormGroupInput): void {
    const carReviewRawValue = { ...this.getFormDefaults(), ...carReview };
    form.reset(
      {
        ...carReviewRawValue,
        id: { value: carReviewRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */,
    );
  }

  private getFormDefaults(): CarReviewFormDefaults {
    return {
      id: null,
    };
  }
}
