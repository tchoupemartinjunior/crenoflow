import dayjs from 'dayjs/esm';
import { CourseFormat } from 'app/entities/enumerations/course-format.model';
import { SlotStatus } from 'app/entities/enumerations/slot-status.model';

export interface IAvailability {
  id: number;
  date?: dayjs.Dayjs | null;
  startTime?: string | null;
  endTime?: string | null;
  format?: keyof typeof CourseFormat | null;
  comment?: string | null;
  videoLink?: string | null;
  address?: string | null;
  status?: keyof typeof SlotStatus | null;
  creationDate?: dayjs.Dayjs | null;
}

export type NewAvailability = Omit<IAvailability, 'id'> & { id: null };
