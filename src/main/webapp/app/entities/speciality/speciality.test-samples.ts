import { ISpeciality, NewSpeciality } from './speciality.model';

export const sampleWithRequiredData: ISpeciality = {
  id: 23066,
};

export const sampleWithPartialData: ISpeciality = {
  id: 21626,
};

export const sampleWithFullData: ISpeciality = {
  id: 13980,
  label: 'délégation',
};

export const sampleWithNewData: NewSpeciality = {
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
