import { IPhotoPerson, NewPhotoPerson } from './photo-person.model';

export const sampleWithRequiredData: IPhotoPerson = {
  id: 5171,
};

export const sampleWithPartialData: IPhotoPerson = {
  id: 25696,
};

export const sampleWithFullData: IPhotoPerson = {
  id: 22254,
  photo: '../fake-data/blob/hipster.png',
  photoContentType: 'unknown',
};

export const sampleWithNewData: NewPhotoPerson = {
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
