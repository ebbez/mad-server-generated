import { Injectable } from '@angular/core';
import { FormControl, FormGroup, Validators } from '@angular/forms';

import { IInspectionPhoto, NewInspectionPhoto } from '../inspection-photo.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IInspectionPhoto for edit and NewInspectionPhotoFormGroupInput for create.
 */
type InspectionPhotoFormGroupInput = IInspectionPhoto | PartialWithRequiredKeyOf<NewInspectionPhoto>;

type InspectionPhotoFormDefaults = Pick<NewInspectionPhoto, 'id'>;

type InspectionPhotoFormGroupContent = {
  id: FormControl<IInspectionPhoto['id'] | NewInspectionPhoto['id']>;
  photo: FormControl<IInspectionPhoto['photo']>;
  photoContentType: FormControl<IInspectionPhoto['photoContentType']>;
  inspection: FormControl<IInspectionPhoto['inspection']>;
};

export type InspectionPhotoFormGroup = FormGroup<InspectionPhotoFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class InspectionPhotoFormService {
  createInspectionPhotoFormGroup(inspectionPhoto: InspectionPhotoFormGroupInput = { id: null }): InspectionPhotoFormGroup {
    const inspectionPhotoRawValue = {
      ...this.getFormDefaults(),
      ...inspectionPhoto,
    };
    return new FormGroup<InspectionPhotoFormGroupContent>({
      id: new FormControl(
        { value: inspectionPhotoRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        },
      ),
      photo: new FormControl(inspectionPhotoRawValue.photo),
      photoContentType: new FormControl(inspectionPhotoRawValue.photoContentType),
      inspection: new FormControl(inspectionPhotoRawValue.inspection),
    });
  }

  getInspectionPhoto(form: InspectionPhotoFormGroup): IInspectionPhoto | NewInspectionPhoto {
    return form.getRawValue() as IInspectionPhoto | NewInspectionPhoto;
  }

  resetForm(form: InspectionPhotoFormGroup, inspectionPhoto: InspectionPhotoFormGroupInput): void {
    const inspectionPhotoRawValue = { ...this.getFormDefaults(), ...inspectionPhoto };
    form.reset(
      {
        ...inspectionPhotoRawValue,
        id: { value: inspectionPhotoRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */,
    );
  }

  private getFormDefaults(): InspectionPhotoFormDefaults {
    return {
      id: null,
    };
  }
}
