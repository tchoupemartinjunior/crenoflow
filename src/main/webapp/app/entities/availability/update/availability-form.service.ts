import { Injectable } from '@angular/core';
import { FormGroup, FormControl, Validators } from '@angular/forms';

import dayjs from 'dayjs/esm';
import { DATE_TIME_FORMAT } from 'app/config/input.constants';
import { IAvailability, NewAvailability } from '../availability.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IAvailability for edit and NewAvailabilityFormGroupInput for create.
 */
type AvailabilityFormGroupInput = IAvailability | PartialWithRequiredKeyOf<NewAvailability>;

/**
 * Type that converts some properties for forms.
 */
type FormValueOf<T extends IAvailability | NewAvailability> = Omit<T, 'creationDate'> & {
  creationDate?: string | null;
};

type AvailabilityFormRawValue = FormValueOf<IAvailability>;

type NewAvailabilityFormRawValue = FormValueOf<NewAvailability>;

type AvailabilityFormDefaults = Pick<NewAvailability, 'id' | 'creationDate'>;

type AvailabilityFormGroupContent = {
  id: FormControl<AvailabilityFormRawValue['id'] | NewAvailability['id']>;
  date: FormControl<AvailabilityFormRawValue['date']>;
  startTime: FormControl<AvailabilityFormRawValue['startTime']>;
  endTime: FormControl<AvailabilityFormRawValue['endTime']>;
  format: FormControl<AvailabilityFormRawValue['format']>;
  comment: FormControl<AvailabilityFormRawValue['comment']>;
  videoLink: FormControl<AvailabilityFormRawValue['videoLink']>;
  address: FormControl<AvailabilityFormRawValue['address']>;
  status: FormControl<AvailabilityFormRawValue['status']>;
  creationDate: FormControl<AvailabilityFormRawValue['creationDate']>;
};

export type AvailabilityFormGroup = FormGroup<AvailabilityFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class AvailabilityFormService {
  createAvailabilityFormGroup(availability: AvailabilityFormGroupInput = { id: null }): AvailabilityFormGroup {
    const availabilityRawValue = this.convertAvailabilityToAvailabilityRawValue({
      ...this.getFormDefaults(),
      ...availability,
    });
    return new FormGroup<AvailabilityFormGroupContent>({
      id: new FormControl(
        { value: availabilityRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        },
      ),
      date: new FormControl(availabilityRawValue.date, {
        validators: [Validators.required],
      }),
      startTime: new FormControl(availabilityRawValue.startTime, {
        validators: [Validators.required, Validators.pattern('^(?:[01]\\d|2[0-3]):[0-5]\\d$')],
      }),
      endTime: new FormControl(availabilityRawValue.endTime, {
        validators: [Validators.required, Validators.pattern('^(?:[01]\\d|2[0-3]):[0-5]\\d$')],
      }),
      format: new FormControl(availabilityRawValue.format, {
        validators: [Validators.required],
      }),
      comment: new FormControl(availabilityRawValue.comment),
      videoLink: new FormControl(availabilityRawValue.videoLink),
      address: new FormControl(availabilityRawValue.address),
      status: new FormControl(availabilityRawValue.status, {
        validators: [Validators.required],
      }),
      creationDate: new FormControl(availabilityRawValue.creationDate, {
        validators: [Validators.required],
      }),
    });
  }

  getAvailability(form: AvailabilityFormGroup): IAvailability | NewAvailability {
    return this.convertAvailabilityRawValueToAvailability(form.getRawValue() as AvailabilityFormRawValue | NewAvailabilityFormRawValue);
  }

  resetForm(form: AvailabilityFormGroup, availability: AvailabilityFormGroupInput): void {
    const availabilityRawValue = this.convertAvailabilityToAvailabilityRawValue({ ...this.getFormDefaults(), ...availability });
    form.reset(
      {
        ...availabilityRawValue,
        id: { value: availabilityRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */,
    );
  }

  private getFormDefaults(): AvailabilityFormDefaults {
    const currentTime = dayjs();

    return {
      id: null,
      creationDate: currentTime,
    };
  }

  private convertAvailabilityRawValueToAvailability(
    rawAvailability: AvailabilityFormRawValue | NewAvailabilityFormRawValue,
  ): IAvailability | NewAvailability {
    return {
      ...rawAvailability,
      creationDate: dayjs(rawAvailability.creationDate, DATE_TIME_FORMAT),
    };
  }

  private convertAvailabilityToAvailabilityRawValue(
    availability: IAvailability | (Partial<NewAvailability> & AvailabilityFormDefaults),
  ): AvailabilityFormRawValue | PartialWithRequiredKeyOf<NewAvailabilityFormRawValue> {
    return {
      ...availability,
      creationDate: availability.creationDate ? availability.creationDate.format(DATE_TIME_FORMAT) : undefined,
    };
  }
}
