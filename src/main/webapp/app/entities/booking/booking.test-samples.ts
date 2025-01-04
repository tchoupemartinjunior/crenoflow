import dayjs from 'dayjs/esm';

import { IBooking, NewBooking } from './booking.model';

export const sampleWithRequiredData: IBooking = {
  id: 22130,
  status: 'CONFIRMED',
};

export const sampleWithPartialData: IBooking = {
  id: 29746,
  status: 'CANCELLED',
  bookingDate: dayjs('2025-01-03T03:07'),
};

export const sampleWithFullData: IBooking = {
  id: 11716,
  status: 'CONFIRMED',
  bookingDate: dayjs('2025-01-03T09:11'),
  modificationDate: dayjs('2025-01-03T12:07'),
};

export const sampleWithNewData: NewBooking = {
  status: 'PENDING',
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
