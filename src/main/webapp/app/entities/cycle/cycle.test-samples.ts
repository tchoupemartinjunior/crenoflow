import { ICycle, NewCycle } from './cycle.model';

export const sampleWithRequiredData: ICycle = {
  id: 2093,
};

export const sampleWithPartialData: ICycle = {
  id: 18185,
};

export const sampleWithFullData: ICycle = {
  id: 4188,
  label: 'que',
};

export const sampleWithNewData: NewCycle = {
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
