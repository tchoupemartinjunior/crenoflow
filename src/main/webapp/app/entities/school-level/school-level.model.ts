import { ICycle } from 'app/entities/cycle/cycle.model';

export interface ISchoolLevel {
  id: number;
  label?: string | null;
  cycle?: Pick<ICycle, 'id' | 'label'> | null;
}

export type NewSchoolLevel = Omit<ISchoolLevel, 'id'> & { id: null };
