import dayjs from 'dayjs/esm';
import { IEmployee } from 'app/entities/employee/employee.model';

export interface IRouteFromTo {
  id: number;
  code?: string | null;
  description?: string | null;
  date?: dayjs.Dayjs | null;
  employee?: IEmployee | null;
}

export type NewRouteFromTo = Omit<IRouteFromTo, 'id'> & { id: null };
