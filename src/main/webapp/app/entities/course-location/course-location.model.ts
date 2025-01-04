import { IPerson } from 'app/entities/person/person.model';

export interface ICourseLocation {
  id: number;
  address?: string | null;
  name?: string | null;
  manager?: Pick<IPerson, 'id'> | null;
}

export type NewCourseLocation = Omit<ICourseLocation, 'id'> & { id: null };
