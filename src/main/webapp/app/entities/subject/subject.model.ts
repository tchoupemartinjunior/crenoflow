import { ISubjectCycle } from 'app/entities/subject-cycle/subject-cycle.model';

export interface ISubject {
  id: number;
  label?: string | null;
  subjectCycles?: Pick<ISubjectCycle, 'id'>[] | null;
}

export type NewSubject = Omit<ISubject, 'id'> & { id: null };
