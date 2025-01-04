import { Injectable } from '@angular/core';
import { FormGroup, FormControl, Validators } from '@angular/forms';

import { ISchoolLevel, NewSchoolLevel } from '../school-level.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts ISchoolLevel for edit and NewSchoolLevelFormGroupInput for create.
 */
type SchoolLevelFormGroupInput = ISchoolLevel | PartialWithRequiredKeyOf<NewSchoolLevel>;

type SchoolLevelFormDefaults = Pick<NewSchoolLevel, 'id'>;

type SchoolLevelFormGroupContent = {
  id: FormControl<ISchoolLevel['id'] | NewSchoolLevel['id']>;
  label: FormControl<ISchoolLevel['label']>;
  cycle: FormControl<ISchoolLevel['cycle']>;
};

export type SchoolLevelFormGroup = FormGroup<SchoolLevelFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class SchoolLevelFormService {
  createSchoolLevelFormGroup(schoolLevel: SchoolLevelFormGroupInput = { id: null }): SchoolLevelFormGroup {
    const schoolLevelRawValue = {
      ...this.getFormDefaults(),
      ...schoolLevel,
    };
    return new FormGroup<SchoolLevelFormGroupContent>({
      id: new FormControl(
        { value: schoolLevelRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        },
      ),
      label: new FormControl(schoolLevelRawValue.label, {
        validators: [Validators.required],
      }),
      cycle: new FormControl(schoolLevelRawValue.cycle),
    });
  }

  getSchoolLevel(form: SchoolLevelFormGroup): ISchoolLevel | NewSchoolLevel {
    return form.getRawValue() as ISchoolLevel | NewSchoolLevel;
  }

  resetForm(form: SchoolLevelFormGroup, schoolLevel: SchoolLevelFormGroupInput): void {
    const schoolLevelRawValue = { ...this.getFormDefaults(), ...schoolLevel };
    form.reset(
      {
        ...schoolLevelRawValue,
        id: { value: schoolLevelRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */,
    );
  }

  private getFormDefaults(): SchoolLevelFormDefaults {
    return {
      id: null,
    };
  }
}
