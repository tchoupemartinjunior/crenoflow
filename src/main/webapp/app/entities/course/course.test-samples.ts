import { ICourse, NewCourse } from './course.model';

export const sampleWithRequiredData: ICourse = {
  id: 16775,
};

export const sampleWithPartialData: ICourse = {
  id: 20080,
  teachersComment: 'diplomate au défaut de clac',
};

export const sampleWithFullData: ICourse = {
  id: 17497,
  teachersComment: 'franco',
};

export const sampleWithNewData: NewCourse = {
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
