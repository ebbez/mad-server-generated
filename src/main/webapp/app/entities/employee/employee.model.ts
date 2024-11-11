import dayjs from 'dayjs/esm';

export interface IEmployee {
  id: number;
  nr?: number | null;
  lastName?: string | null;
  firstName?: string | null;
  from?: dayjs.Dayjs | null;
}

export type NewEmployee = Omit<IEmployee, 'id'> & { id: null };
