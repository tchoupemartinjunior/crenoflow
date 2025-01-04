import dayjs from 'dayjs/esm';

import { IPerson, NewPerson } from './person.model';

export const sampleWithRequiredData: IPerson = {
  id: 766,
};

export const sampleWithPartialData: IPerson = {
  id: 32110,
  city: 'Drancy',
  postalCode: 24391,
};

export const sampleWithFullData: IPerson = {
  id: 17288,
  firstName: 'Damien',
  lastName: 'Perez',
  email: 'Antigone.Caron@hotmail.fr',
  phoneNumber: 'de sorte que',
  address: 'pacifique',
  city: 'Neuilly-sur-Seine',
  postalCode: 8634,
  birthday: dayjs('2025-01-03'),
};

export const sampleWithNewData: NewPerson = {
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
