import { Injectable } from '@angular/core';
import { FormGroup, FormControl, Validators } from '@angular/forms';

import { ISubjectCycle, NewSubjectCycle } from '../subject-cycle.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts ISubjectCycle for edit and NewSubjectCycleFormGroupInput for create.
 */
type SubjectCycleFormGroupInput = ISubjectCycle | PartialWithRequiredKeyOf<NewSubjectCycle>;

type SubjectCycleFormDefaults = Pick<NewSubjectCycle, 'id' | 'teachers' | 'subjects' | 'cycles'>;

type SubjectCycleFormGroupContent = {
  id: FormControl<ISubjectCycle['id'] | NewSubjectCycle['id']>;
  teachers: FormControl<ISubjectCycle['teachers']>;
  subjects: FormControl<ISubjectCycle['subjects']>;
  cycles: FormControl<ISubjectCycle['cycles']>;
};

export type SubjectCycleFormGroup = FormGroup<SubjectCycleFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class SubjectCycleFormService {
  createSubjectCycleFormGroup(subjectCycle: SubjectCycleFormGroupInput = { id: null }): SubjectCycleFormGroup {
    const subjectCycleRawValue = {
      ...this.getFormDefaults(),
      ...subjectCycle,
    };
    return new FormGroup<SubjectCycleFormGroupContent>({
      id: new FormControl(
        { value: subjectCycleRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        },
      ),
      teachers: new FormControl(subjectCycleRawValue.teachers ?? []),
      subjects: new FormControl(subjectCycleRawValue.subjects ?? []),
      cycles: new FormControl(subjectCycleRawValue.cycles ?? []),
    });
  }

  getSubjectCycle(form: SubjectCycleFormGroup): ISubjectCycle | NewSubjectCycle {
    return form.getRawValue() as ISubjectCycle | NewSubjectCycle;
  }

  resetForm(form: SubjectCycleFormGroup, subjectCycle: SubjectCycleFormGroupInput): void {
    const subjectCycleRawValue = { ...this.getFormDefaults(), ...subjectCycle };
    form.reset(
      {
        ...subjectCycleRawValue,
        id: { value: subjectCycleRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */,
    );
  }

  private getFormDefaults(): SubjectCycleFormDefaults {
    return {
      id: null,
      teachers: [],
      subjects: [],
      cycles: [],
    };
  }
}
