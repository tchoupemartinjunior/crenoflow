import dayjs from 'dayjs/esm';
import { IUser } from 'app/entities/user/user.model';
import { ISchoolLevel } from 'app/entities/school-level/school-level.model';

export interface IPerson {
  id: number;
  firstName?: string | null;
  lastName?: string | null;
  email?: string | null;
  phoneNumber?: string | null;
  address?: string | null;
  city?: string | null;
  postalCode?: number | null;
  birthday?: dayjs.Dayjs | null;
  user?: Pick<IUser, 'id'> | null;
  schoolLevel?: Pick<ISchoolLevel, 'id' | 'label'> | null;
}

export type NewPerson = Omit<IPerson, 'id'> & { id: null };
