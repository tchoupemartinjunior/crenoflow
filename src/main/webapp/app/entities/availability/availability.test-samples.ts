import dayjs from 'dayjs/esm';

import { IAvailability, NewAvailability } from './availability.model';

export const sampleWithRequiredData: IAvailability = {
  id: 4493,
  date: dayjs('2025-01-02'),
  startTime: '20:37',
  endTime: '22:05',
  format: 'IN_PERSON',
  status: 'AVAILABLE',
  creationDate: dayjs('2025-01-03T03:47'),
};

export const sampleWithPartialData: IAvailability = {
  id: 24070,
  date: dayjs('2025-01-03'),
  startTime: '23:24',
  endTime: '09:24',
  format: 'IN_PERSON',
  address: 'reproduire aigre probablement',
  status: 'UNAVAILABLE',
  creationDate: dayjs('2025-01-03T16:22'),
};

export const sampleWithFullData: IAvailability = {
  id: 2285,
  date: dayjs('2025-01-02'),
  startTime: '01:02',
  endTime: '23:33',
  format: 'IN_PERSON',
  comment: 'parce que tellement en vérité',
  videoLink: 'du fait que rechercher',
  address: 'déborder longtemps',
  status: 'UNAVAILABLE',
  creationDate: dayjs('2025-01-03T09:44'),
};

export const sampleWithNewData: NewAvailability = {
  date: dayjs('2025-01-03'),
  startTime: '17:51',
  endTime: '14:30',
  format: 'IN_PERSON',
  status: 'UNAVAILABLE',
  creationDate: dayjs('2025-01-03T14:38'),
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
