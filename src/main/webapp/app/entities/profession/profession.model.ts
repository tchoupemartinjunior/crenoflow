export interface IProfession {
  id: number;
  label?: string | null;
}

export type NewProfession = Omit<IProfession, 'id'> & { id: null };
