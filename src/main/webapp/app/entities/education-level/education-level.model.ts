export interface IEducationLevel {
  id: number;
  label?: string | null;
}

export type NewEducationLevel = Omit<IEducationLevel, 'id'> & { id: null };
