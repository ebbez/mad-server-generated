import { Injectable } from '@angular/core';
import { FormControl, FormGroup, Validators } from '@angular/forms';

import dayjs from 'dayjs/esm';
import { DATE_TIME_FORMAT } from 'app/config/input.constants';
import { IInspection, NewInspection } from '../inspection.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IInspection for edit and NewInspectionFormGroupInput for create.
 */
type InspectionFormGroupInput = IInspection | PartialWithRequiredKeyOf<NewInspection>;

/**
 * Type that converts some properties for forms.
 */
type FormValueOf<T extends IInspection | NewInspection> = Omit<T, 'completed'> & {
  completed?: string | null;
};

type InspectionFormRawValue = FormValueOf<IInspection>;

type NewInspectionFormRawValue = FormValueOf<NewInspection>;

type InspectionFormDefaults = Pick<NewInspection, 'id' | 'completed'>;

type InspectionFormGroupContent = {
  id: FormControl<InspectionFormRawValue['id'] | NewInspection['id']>;
  code: FormControl<InspectionFormRawValue['code']>;
  odometer: FormControl<InspectionFormRawValue['odometer']>;
  result: FormControl<InspectionFormRawValue['result']>;
  description: FormControl<InspectionFormRawValue['description']>;
  photo: FormControl<InspectionFormRawValue['photo']>;
  photoContentType: FormControl<InspectionFormRawValue['photoContentType']>;
  completed: FormControl<InspectionFormRawValue['completed']>;
  car: FormControl<InspectionFormRawValue['car']>;
  employee: FormControl<InspectionFormRawValue['employee']>;
  rental: FormControl<InspectionFormRawValue['rental']>;
};

export type InspectionFormGroup = FormGroup<InspectionFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class InspectionFormService {
  createInspectionFormGroup(inspection: InspectionFormGroupInput = { id: null }): InspectionFormGroup {
    const inspectionRawValue = this.convertInspectionToInspectionRawValue({
      ...this.getFormDefaults(),
      ...inspection,
    });
    return new FormGroup<InspectionFormGroupContent>({
      id: new FormControl(
        { value: inspectionRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        },
      ),
      code: new FormControl(inspectionRawValue.code),
      odometer: new FormControl(inspectionRawValue.odometer),
      result: new FormControl(inspectionRawValue.result),
      description: new FormControl(inspectionRawValue.description),
      photo: new FormControl(inspectionRawValue.photo),
      photoContentType: new FormControl(inspectionRawValue.photoContentType),
      completed: new FormControl(inspectionRawValue.completed),
      car: new FormControl(inspectionRawValue.car),
      employee: new FormControl(inspectionRawValue.employee),
      rental: new FormControl(inspectionRawValue.rental),
    });
  }

  getInspection(form: InspectionFormGroup): IInspection | NewInspection {
    return this.convertInspectionRawValueToInspection(form.getRawValue() as InspectionFormRawValue | NewInspectionFormRawValue);
  }

  resetForm(form: InspectionFormGroup, inspection: InspectionFormGroupInput): void {
    const inspectionRawValue = this.convertInspectionToInspectionRawValue({ ...this.getFormDefaults(), ...inspection });
    form.reset(
      {
        ...inspectionRawValue,
        id: { value: inspectionRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */,
    );
  }

  private getFormDefaults(): InspectionFormDefaults {
    const currentTime = dayjs();

    return {
      id: null,
      completed: currentTime,
    };
  }

  private convertInspectionRawValueToInspection(
    rawInspection: InspectionFormRawValue | NewInspectionFormRawValue,
  ): IInspection | NewInspection {
    return {
      ...rawInspection,
      completed: dayjs(rawInspection.completed, DATE_TIME_FORMAT),
    };
  }

  private convertInspectionToInspectionRawValue(
    inspection: IInspection | (Partial<NewInspection> & InspectionFormDefaults),
  ): InspectionFormRawValue | PartialWithRequiredKeyOf<NewInspectionFormRawValue> {
    return {
      ...inspection,
      completed: inspection.completed ? inspection.completed.format(DATE_TIME_FORMAT) : undefined,
    };
  }
}
