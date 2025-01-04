import { ITeacher } from 'app/entities/teacher/teacher.model';
import { ISubject } from 'app/entities/subject/subject.model';
import { ICycle } from 'app/entities/cycle/cycle.model';

export interface ISubjectCycle {
  id: number;
  teachers?: Pick<ITeacher, 'id'>[] | null;
  subjects?: Pick<ISubject, 'id' | 'label'>[] | null;
  cycles?: Pick<ICycle, 'id' | 'label'>[] | null;
}

export type NewSubjectCycle = Omit<ISubjectCycle, 'id'> & { id: null };
