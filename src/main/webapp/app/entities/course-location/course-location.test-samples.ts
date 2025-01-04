import { ICourseLocation, NewCourseLocation } from './course-location.model';

export const sampleWithRequiredData: ICourseLocation = {
  id: 22539,
  name: 'camarade porte-parole oh',
};

export const sampleWithPartialData: ICourseLocation = {
  id: 13065,
  name: 'patientèle',
};

export const sampleWithFullData: ICourseLocation = {
  id: 29614,
  address: 'dans la mesure où intrépide rocher',
  name: 'pacifique',
};

export const sampleWithNewData: NewCourseLocation = {
  name: 'hier avare',
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
