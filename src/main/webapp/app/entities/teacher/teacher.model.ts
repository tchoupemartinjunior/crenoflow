import { IPerson } from 'app/entities/person/person.model';
import { IEducationLevel } from 'app/entities/education-level/education-level.model';
import { IProfession } from 'app/entities/profession/profession.model';
import { ISpeciality } from 'app/entities/speciality/speciality.model';
import { ISubjectCycle } from 'app/entities/subject-cycle/subject-cycle.model';

export interface ITeacher {
  id: number;
  introduction?: string | null;
  person?: Pick<IPerson, 'id' | 'lastName'> | null;
  educationLevel?: Pick<IEducationLevel, 'id' | 'label'> | null;
  profession?: Pick<IProfession, 'id' | 'label'> | null;
  speciality?: Pick<ISpeciality, 'id' | 'label'> | null;
  subjectCycles?: Pick<ISubjectCycle, 'id'>[] | null;
}

export type NewTeacher = Omit<ITeacher, 'id'> & { id: null };
