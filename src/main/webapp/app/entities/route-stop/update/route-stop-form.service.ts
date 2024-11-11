import { Injectable } from '@angular/core';
import { FormControl, FormGroup, Validators } from '@angular/forms';

import { IRouteStop, NewRouteStop } from '../route-stop.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IRouteStop for edit and NewRouteStopFormGroupInput for create.
 */
type RouteStopFormGroupInput = IRouteStop | PartialWithRequiredKeyOf<NewRouteStop>;

type RouteStopFormDefaults = Pick<NewRouteStop, 'id'>;

type RouteStopFormGroupContent = {
  id: FormControl<IRouteStop['id'] | NewRouteStop['id']>;
  nr: FormControl<IRouteStop['nr']>;
  routeFromTo: FormControl<IRouteStop['routeFromTo']>;
  location: FormControl<IRouteStop['location']>;
};

export type RouteStopFormGroup = FormGroup<RouteStopFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class RouteStopFormService {
  createRouteStopFormGroup(routeStop: RouteStopFormGroupInput = { id: null }): RouteStopFormGroup {
    const routeStopRawValue = {
      ...this.getFormDefaults(),
      ...routeStop,
    };
    return new FormGroup<RouteStopFormGroupContent>({
      id: new FormControl(
        { value: routeStopRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        },
      ),
      nr: new FormControl(routeStopRawValue.nr),
      routeFromTo: new FormControl(routeStopRawValue.routeFromTo),
      location: new FormControl(routeStopRawValue.location),
    });
  }

  getRouteStop(form: RouteStopFormGroup): IRouteStop | NewRouteStop {
    return form.getRawValue() as IRouteStop | NewRouteStop;
  }

  resetForm(form: RouteStopFormGroup, routeStop: RouteStopFormGroupInput): void {
    const routeStopRawValue = { ...this.getFormDefaults(), ...routeStop };
    form.reset(
      {
        ...routeStopRawValue,
        id: { value: routeStopRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */,
    );
  }

  private getFormDefaults(): RouteStopFormDefaults {
    return {
      id: null,
    };
  }
}
