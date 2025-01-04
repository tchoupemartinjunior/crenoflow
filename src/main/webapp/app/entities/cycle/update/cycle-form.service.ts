import { Injectable } from '@angular/core';
import { FormGroup, FormControl, Validators } from '@angular/forms';

import { ICycle, NewCycle } from '../cycle.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts ICycle for edit and NewCycleFormGroupInput for create.
 */
type CycleFormGroupInput = ICycle | PartialWithRequiredKeyOf<NewCycle>;

type CycleFormDefaults = Pick<NewCycle, 'id' | 'subjectCycles'>;

type CycleFormGroupContent = {
  id: FormControl<ICycle['id'] | NewCycle['id']>;
  label: FormControl<ICycle['label']>;
  subjectCycles: FormControl<ICycle['subjectCycles']>;
};

export type CycleFormGroup = FormGroup<CycleFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class CycleFormService {
  createCycleFormGroup(cycle: CycleFormGroupInput = { id: null }): CycleFormGroup {
    const cycleRawValue = {
      ...this.getFormDefaults(),
      ...cycle,
    };
    return new FormGroup<CycleFormGroupContent>({
      id: new FormControl(
        { value: cycleRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        },
      ),
      label: new FormControl(cycleRawValue.label),
      subjectCycles: new FormControl(cycleRawValue.subjectCycles ?? []),
    });
  }

  getCycle(form: CycleFormGroup): ICycle | NewCycle {
    return form.getRawValue() as ICycle | NewCycle;
  }

  resetForm(form: CycleFormGroup, cycle: CycleFormGroupInput): void {
    const cycleRawValue = { ...this.getFormDefaults(), ...cycle };
    form.reset(
      {
        ...cycleRawValue,
        id: { value: cycleRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */,
    );
  }

  private getFormDefaults(): CycleFormDefaults {
    return {
      id: null,
      subjectCycles: [],
    };
  }
}
