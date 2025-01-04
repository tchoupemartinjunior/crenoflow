import { Injectable } from '@angular/core';
import { FormGroup, FormControl, Validators } from '@angular/forms';

import { IEducationLevel, NewEducationLevel } from '../education-level.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IEducationLevel for edit and NewEducationLevelFormGroupInput for create.
 */
type EducationLevelFormGroupInput = IEducationLevel | PartialWithRequiredKeyOf<NewEducationLevel>;

type EducationLevelFormDefaults = Pick<NewEducationLevel, 'id'>;

type EducationLevelFormGroupContent = {
  id: FormControl<IEducationLevel['id'] | NewEducationLevel['id']>;
  label: FormControl<IEducationLevel['label']>;
};

export type EducationLevelFormGroup = FormGroup<EducationLevelFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class EducationLevelFormService {
  createEducationLevelFormGroup(educationLevel: EducationLevelFormGroupInput = { id: null }): EducationLevelFormGroup {
    const educationLevelRawValue = {
      ...this.getFormDefaults(),
      ...educationLevel,
    };
    return new FormGroup<EducationLevelFormGroupContent>({
      id: new FormControl(
        { value: educationLevelRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        },
      ),
      label: new FormControl(educationLevelRawValue.label),
    });
  }

  getEducationLevel(form: EducationLevelFormGroup): IEducationLevel | NewEducationLevel {
    return form.getRawValue() as IEducationLevel | NewEducationLevel;
  }

  resetForm(form: EducationLevelFormGroup, educationLevel: EducationLevelFormGroupInput): void {
    const educationLevelRawValue = { ...this.getFormDefaults(), ...educationLevel };
    form.reset(
      {
        ...educationLevelRawValue,
        id: { value: educationLevelRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */,
    );
  }

  private getFormDefaults(): EducationLevelFormDefaults {
    return {
      id: null,
    };
  }
}
