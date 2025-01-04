import { IPerson } from 'app/entities/person/person.model';

export interface IPhotoPerson {
  id: number;
  photo?: string | null;
  photoContentType?: string | null;
  person?: Pick<IPerson, 'id'> | null;
}

export type NewPhotoPerson = Omit<IPhotoPerson, 'id'> & { id: null };
