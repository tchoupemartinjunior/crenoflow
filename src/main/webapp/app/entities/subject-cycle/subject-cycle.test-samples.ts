import { ISubjectCycle, NewSubjectCycle } from './subject-cycle.model';

export const sampleWithRequiredData: ISubjectCycle = {
  id: 17639,
};

export const sampleWithPartialData: ISubjectCycle = {
  id: 31191,
};

export const sampleWithFullData: ISubjectCycle = {
  id: 25830,
};

export const sampleWithNewData: NewSubjectCycle = {
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
