import { ISubjectCycle } from 'app/entities/subject-cycle/subject-cycle.model';

export interface ICycle {
  id: number;
  label?: string | null;
  subjectCycles?: Pick<ISubjectCycle, 'id'>[] | null;
}

export type NewCycle = Omit<ICycle, 'id'> & { id: null };
