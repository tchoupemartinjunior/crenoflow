import dayjs from 'dayjs/esm';
import { ICourse } from 'app/entities/course/course.model';
import { IPerson } from 'app/entities/person/person.model';
import { BookingStatus } from 'app/entities/enumerations/booking-status.model';

export interface IBooking {
  id: number;
  status?: keyof typeof BookingStatus | null;
  bookingDate?: dayjs.Dayjs | null;
  modificationDate?: dayjs.Dayjs | null;
  course?: Pick<ICourse, 'id'> | null;
  parent?: Pick<IPerson, 'id'> | null;
  student?: Pick<IPerson, 'id'> | null;
}

export type NewBooking = Omit<IBooking, 'id'> & { id: null };
