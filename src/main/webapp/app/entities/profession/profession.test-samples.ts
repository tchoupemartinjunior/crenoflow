import { IProfession, NewProfession } from './profession.model';

export const sampleWithRequiredData: IProfession = {
  id: 21723,
};

export const sampleWithPartialData: IProfession = {
  id: 11314,
};

export const sampleWithFullData: IProfession = {
  id: 29402,
  label: 'mairie',
};

export const sampleWithNewData: NewProfession = {
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
