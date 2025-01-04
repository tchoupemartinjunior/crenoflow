import { ISchoolLevel, NewSchoolLevel } from './school-level.model';

export const sampleWithRequiredData: ISchoolLevel = {
  id: 32063,
  label: 'de',
};

export const sampleWithPartialData: ISchoolLevel = {
  id: 24345,
  label: 'commissionnaire',
};

export const sampleWithFullData: ISchoolLevel = {
  id: 27283,
  label: 'grandement avant que',
};

export const sampleWithNewData: NewSchoolLevel = {
  label: 'biathlète',
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
