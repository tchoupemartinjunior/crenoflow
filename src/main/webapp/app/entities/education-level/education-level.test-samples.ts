import { IEducationLevel, NewEducationLevel } from './education-level.model';

export const sampleWithRequiredData: IEducationLevel = {
  id: 6046,
};

export const sampleWithPartialData: IEducationLevel = {
  id: 5902,
  label: 'à moins de plic dessus',
};

export const sampleWithFullData: IEducationLevel = {
  id: 19293,
  label: 'conseil municipal ouch calme',
};

export const sampleWithNewData: NewEducationLevel = {
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
