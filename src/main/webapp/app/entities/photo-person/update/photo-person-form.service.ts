import { Injectable } from '@angular/core';
import { FormGroup, FormControl, Validators } from '@angular/forms';

import { IPhotoPerson, NewPhotoPerson } from '../photo-person.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IPhotoPerson for edit and NewPhotoPersonFormGroupInput for create.
 */
type PhotoPersonFormGroupInput = IPhotoPerson | PartialWithRequiredKeyOf<NewPhotoPerson>;

type PhotoPersonFormDefaults = Pick<NewPhotoPerson, 'id'>;

type PhotoPersonFormGroupContent = {
  id: FormControl<IPhotoPerson['id'] | NewPhotoPerson['id']>;
  photo: FormControl<IPhotoPerson['photo']>;
  photoContentType: FormControl<IPhotoPerson['photoContentType']>;
  person: FormControl<IPhotoPerson['person']>;
};

export type PhotoPersonFormGroup = FormGroup<PhotoPersonFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class PhotoPersonFormService {
  createPhotoPersonFormGroup(photoPerson: PhotoPersonFormGroupInput = { id: null }): PhotoPersonFormGroup {
    const photoPersonRawValue = {
      ...this.getFormDefaults(),
      ...photoPerson,
    };
    return new FormGroup<PhotoPersonFormGroupContent>({
      id: new FormControl(
        { value: photoPersonRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        },
      ),
      photo: new FormControl(photoPersonRawValue.photo),
      photoContentType: new FormControl(photoPersonRawValue.photoContentType),
      person: new FormControl(photoPersonRawValue.person),
    });
  }

  getPhotoPerson(form: PhotoPersonFormGroup): IPhotoPerson | NewPhotoPerson {
    return form.getRawValue() as IPhotoPerson | NewPhotoPerson;
  }

  resetForm(form: PhotoPersonFormGroup, photoPerson: PhotoPersonFormGroupInput): void {
    const photoPersonRawValue = { ...this.getFormDefaults(), ...photoPerson };
    form.reset(
      {
        ...photoPersonRawValue,
        id: { value: photoPersonRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */,
    );
  }

  private getFormDefaults(): PhotoPersonFormDefaults {
    return {
      id: null,
    };
  }
}
