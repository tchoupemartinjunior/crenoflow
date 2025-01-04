import { ITeacher, NewTeacher } from './teacher.model';

export const sampleWithRequiredData: ITeacher = {
  id: 19384,
};

export const sampleWithPartialData: ITeacher = {
  id: 22764,
};

export const sampleWithFullData: ITeacher = {
  id: 20898,
  introduction: 'même si hors de',
};

export const sampleWithNewData: NewTeacher = {
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
