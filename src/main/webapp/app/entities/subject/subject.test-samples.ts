import { ISubject, NewSubject } from './subject.model';

export const sampleWithRequiredData: ISubject = {
  id: 7826,
  label: 'puis',
};

export const sampleWithPartialData: ISubject = {
  id: 10488,
  label: 'miam',
};

export const sampleWithFullData: ISubject = {
  id: 13321,
  label: 'alentour super assez',
};

export const sampleWithNewData: NewSubject = {
  label: 'grandement responsable',
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
