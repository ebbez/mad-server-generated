import dayjs from 'dayjs/esm';
import { ICustomer } from 'app/entities/customer/customer.model';
import { ICar } from 'app/entities/car/car.model';
import { RentalState } from 'app/entities/enumerations/rental-state.model';

export interface IRental {
  id: number;
  code?: string | null;
  longitude?: number | null;
  latitude?: number | null;
  fromDate?: dayjs.Dayjs | null;
  toDate?: dayjs.Dayjs | null;
  state?: keyof typeof RentalState | null;
  customer?: ICustomer | null;
  car?: ICar | null;
}

export type NewRental = Omit<IRental, 'id'> & { id: null };
