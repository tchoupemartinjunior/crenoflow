import { Injectable } from '@angular/core';
import { FormGroup, FormControl, Validators } from '@angular/forms';

import dayjs from 'dayjs/esm';
import { DATE_TIME_FORMAT } from 'app/config/input.constants';
import { IBooking, NewBooking } from '../booking.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IBooking for edit and NewBookingFormGroupInput for create.
 */
type BookingFormGroupInput = IBooking | PartialWithRequiredKeyOf<NewBooking>;

/**
 * Type that converts some properties for forms.
 */
type FormValueOf<T extends IBooking | NewBooking> = Omit<T, 'bookingDate' | 'modificationDate'> & {
  bookingDate?: string | null;
  modificationDate?: string | null;
};

type BookingFormRawValue = FormValueOf<IBooking>;

type NewBookingFormRawValue = FormValueOf<NewBooking>;

type BookingFormDefaults = Pick<NewBooking, 'id' | 'bookingDate' | 'modificationDate'>;

type BookingFormGroupContent = {
  id: FormControl<BookingFormRawValue['id'] | NewBooking['id']>;
  status: FormControl<BookingFormRawValue['status']>;
  bookingDate: FormControl<BookingFormRawValue['bookingDate']>;
  modificationDate: FormControl<BookingFormRawValue['modificationDate']>;
  course: FormControl<BookingFormRawValue['course']>;
  parent: FormControl<BookingFormRawValue['parent']>;
  student: FormControl<BookingFormRawValue['student']>;
};

export type BookingFormGroup = FormGroup<BookingFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class BookingFormService {
  createBookingFormGroup(booking: BookingFormGroupInput = { id: null }): BookingFormGroup {
    const bookingRawValue = this.convertBookingToBookingRawValue({
      ...this.getFormDefaults(),
      ...booking,
    });
    return new FormGroup<BookingFormGroupContent>({
      id: new FormControl(
        { value: bookingRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        },
      ),
      status: new FormControl(bookingRawValue.status, {
        validators: [Validators.required],
      }),
      bookingDate: new FormControl(bookingRawValue.bookingDate),
      modificationDate: new FormControl(bookingRawValue.modificationDate),
      course: new FormControl(bookingRawValue.course),
      parent: new FormControl(bookingRawValue.parent),
      student: new FormControl(bookingRawValue.student),
    });
  }

  getBooking(form: BookingFormGroup): IBooking | NewBooking {
    return this.convertBookingRawValueToBooking(form.getRawValue() as BookingFormRawValue | NewBookingFormRawValue);
  }

  resetForm(form: BookingFormGroup, booking: BookingFormGroupInput): void {
    const bookingRawValue = this.convertBookingToBookingRawValue({ ...this.getFormDefaults(), ...booking });
    form.reset(
      {
        ...bookingRawValue,
        id: { value: bookingRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */,
    );
  }

  private getFormDefaults(): BookingFormDefaults {
    const currentTime = dayjs();

    return {
      id: null,
      bookingDate: currentTime,
      modificationDate: currentTime,
    };
  }

  private convertBookingRawValueToBooking(rawBooking: BookingFormRawValue | NewBookingFormRawValue): IBooking | NewBooking {
    return {
      ...rawBooking,
      bookingDate: dayjs(rawBooking.bookingDate, DATE_TIME_FORMAT),
      modificationDate: dayjs(rawBooking.modificationDate, DATE_TIME_FORMAT),
    };
  }

  private convertBookingToBookingRawValue(
    booking: IBooking | (Partial<NewBooking> & BookingFormDefaults),
  ): BookingFormRawValue | PartialWithRequiredKeyOf<NewBookingFormRawValue> {
    return {
      ...booking,
      bookingDate: booking.bookingDate ? booking.bookingDate.format(DATE_TIME_FORMAT) : undefined,
      modificationDate: booking.modificationDate ? booking.modificationDate.format(DATE_TIME_FORMAT) : undefined,
    };
  }
}
