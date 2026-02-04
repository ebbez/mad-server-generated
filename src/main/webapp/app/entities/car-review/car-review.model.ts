import { ICustomer } from 'app/entities/customer/customer.model';
import { ICar } from 'app/entities/car/car.model';

export interface ICarReview {
  id: number;
  review?: string | null;
  customer?: ICustomer | null;
  car?: ICar | null;
}

export type NewCarReview = Omit<ICarReview, 'id'> & { id: null };
