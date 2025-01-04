export interface ISpeciality {
  id: number;
  label?: string | null;
}

export type NewSpeciality = Omit<ISpeciality, 'id'> & { id: null };
