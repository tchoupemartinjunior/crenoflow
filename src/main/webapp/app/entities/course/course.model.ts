import { ISubject } from 'app/entities/subject/subject.model';
import { ICycle } from 'app/entities/cycle/cycle.model';
import { ICourseLocation } from 'app/entities/course-location/course-location.model';
import { ITeacher } from 'app/entities/teacher/teacher.model';
import { IAvailability } from 'app/entities/availability/availability.model';

export interface ICourse {
  id: number;
  teachersComment?: string | null;
  subject?: Pick<ISubject, 'id' | 'label'> | null;
  cycle?: Pick<ICycle, 'id' | 'label'> | null;
  location?: Pick<ICourseLocation, 'id'> | null;
  teacher?: Pick<ITeacher, 'id'> | null;
  availability?: Pick<IAvailability, 'id'> | null;
}

export type NewCourse = Omit<ICourse, 'id'> & { id: null };
