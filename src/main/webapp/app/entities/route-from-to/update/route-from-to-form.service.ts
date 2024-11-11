import { Injectable } from '@angular/core';
import { FormControl, FormGroup, Validators } from '@angular/forms';

import { IRouteFromTo, NewRouteFromTo } from '../route-from-to.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IRouteFromTo for edit and NewRouteFromToFormGroupInput for create.
 */
type RouteFromToFormGroupInput = IRouteFromTo | PartialWithRequiredKeyOf<NewRouteFromTo>;

type RouteFromToFormDefaults = Pick<NewRouteFromTo, 'id'>;

type RouteFromToFormGroupContent = {
  id: FormControl<IRouteFromTo['id'] | NewRouteFromTo['id']>;
  code: FormControl<IRouteFromTo['code']>;
  description: FormControl<IRouteFromTo['description']>;
  date: FormControl<IRouteFromTo['date']>;
  employee: FormControl<IRouteFromTo['employee']>;
};

export type RouteFromToFormGroup = FormGroup<RouteFromToFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class RouteFromToFormService {
  createRouteFromToFormGroup(routeFromTo: RouteFromToFormGroupInput = { id: null }): RouteFromToFormGroup {
    const routeFromToRawValue = {
      ...this.getFormDefaults(),
      ...routeFromTo,
    };
    return new FormGroup<RouteFromToFormGroupContent>({
      id: new FormControl(
        { value: routeFromToRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        },
      ),
      code: new FormControl(routeFromToRawValue.code),
      description: new FormControl(routeFromToRawValue.description),
      date: new FormControl(routeFromToRawValue.date),
      employee: new FormControl(routeFromToRawValue.employee),
    });
  }

  getRouteFromTo(form: RouteFromToFormGroup): IRouteFromTo | NewRouteFromTo {
    return form.getRawValue() as IRouteFromTo | NewRouteFromTo;
  }

  resetForm(form: RouteFromToFormGroup, routeFromTo: RouteFromToFormGroupInput): void {
    const routeFromToRawValue = { ...this.getFormDefaults(), ...routeFromTo };
    form.reset(
      {
        ...routeFromToRawValue,
        id: { value: routeFromToRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */,
    );
  }

  private getFormDefaults(): RouteFromToFormDefaults {
    return {
      id: null,
    };
  }
}
