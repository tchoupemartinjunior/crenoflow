import { Injectable } from '@angular/core';
import { FormGroup, FormControl, Validators } from '@angular/forms';

import { ICourseLocation, NewCourseLocation } from '../course-location.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts ICourseLocation for edit and NewCourseLocationFormGroupInput for create.
 */
type CourseLocationFormGroupInput = ICourseLocation | PartialWithRequiredKeyOf<NewCourseLocation>;

type CourseLocationFormDefaults = Pick<NewCourseLocation, 'id'>;

type CourseLocationFormGroupContent = {
  id: FormControl<ICourseLocation['id'] | NewCourseLocation['id']>;
  address: FormControl<ICourseLocation['address']>;
  name: FormControl<ICourseLocation['name']>;
  manager: FormControl<ICourseLocation['manager']>;
};

export type CourseLocationFormGroup = FormGroup<CourseLocationFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class CourseLocationFormService {
  createCourseLocationFormGroup(courseLocation: CourseLocationFormGroupInput = { id: null }): CourseLocationFormGroup {
    const courseLocationRawValue = {
      ...this.getFormDefaults(),
      ...courseLocation,
    };
    return new FormGroup<CourseLocationFormGroupContent>({
      id: new FormControl(
        { value: courseLocationRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        },
      ),
      address: new FormControl(courseLocationRawValue.address),
      name: new FormControl(courseLocationRawValue.name, {
        validators: [Validators.required, Validators.maxLength(64)],
      }),
      manager: new FormControl(courseLocationRawValue.manager),
    });
  }

  getCourseLocation(form: CourseLocationFormGroup): ICourseLocation | NewCourseLocation {
    return form.getRawValue() as ICourseLocation | NewCourseLocation;
  }

  resetForm(form: CourseLocationFormGroup, courseLocation: CourseLocationFormGroupInput): void {
    const courseLocationRawValue = { ...this.getFormDefaults(), ...courseLocation };
    form.reset(
      {
        ...courseLocationRawValue,
        id: { value: courseLocationRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */,
    );
  }

  private getFormDefaults(): CourseLocationFormDefaults {
    return {
      id: null,
    };
  }
}
