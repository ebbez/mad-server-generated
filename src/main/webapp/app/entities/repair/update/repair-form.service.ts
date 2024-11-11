import { Injectable } from '@angular/core';
import { FormControl, FormGroup, Validators } from '@angular/forms';

import { IRepair, NewRepair } from '../repair.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IRepair for edit and NewRepairFormGroupInput for create.
 */
type RepairFormGroupInput = IRepair | PartialWithRequiredKeyOf<NewRepair>;

type RepairFormDefaults = Pick<NewRepair, 'id'>;

type RepairFormGroupContent = {
  id: FormControl<IRepair['id'] | NewRepair['id']>;
  description: FormControl<IRepair['description']>;
  repairStatus: FormControl<IRepair['repairStatus']>;
  dateCompleted: FormControl<IRepair['dateCompleted']>;
  car: FormControl<IRepair['car']>;
  employee: FormControl<IRepair['employee']>;
  inspection: FormControl<IRepair['inspection']>;
};

export type RepairFormGroup = FormGroup<RepairFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class RepairFormService {
  createRepairFormGroup(repair: RepairFormGroupInput = { id: null }): RepairFormGroup {
    const repairRawValue = {
      ...this.getFormDefaults(),
      ...repair,
    };
    return new FormGroup<RepairFormGroupContent>({
      id: new FormControl(
        { value: repairRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        },
      ),
      description: new FormControl(repairRawValue.description),
      repairStatus: new FormControl(repairRawValue.repairStatus),
      dateCompleted: new FormControl(repairRawValue.dateCompleted),
      car: new FormControl(repairRawValue.car),
      employee: new FormControl(repairRawValue.employee),
      inspection: new FormControl(repairRawValue.inspection),
    });
  }

  getRepair(form: RepairFormGroup): IRepair | NewRepair {
    return form.getRawValue() as IRepair | NewRepair;
  }

  resetForm(form: RepairFormGroup, repair: RepairFormGroupInput): void {
    const repairRawValue = { ...this.getFormDefaults(), ...repair };
    form.reset(
      {
        ...repairRawValue,
        id: { value: repairRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */,
    );
  }

  private getFormDefaults(): RepairFormDefaults {
    return {
      id: null,
    };
  }
}
