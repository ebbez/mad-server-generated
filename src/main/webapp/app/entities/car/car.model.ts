import dayjs from 'dayjs/esm';
import { Fuel } from 'app/entities/enumerations/fuel.model';
import { Body } from 'app/entities/enumerations/body.model';

export interface ICar {
  id: number;
  brand?: string | null;
  model?: string | null;
  picture?: string | null;
  pictureContentType?: string | null;
  fuel?: keyof typeof Fuel | null;
  options?: string | null;
  licensePlate?: string | null;
  engineSize?: number | null;
  modelYear?: number | null;
  since?: dayjs.Dayjs | null;
  price?: number | null;
  nrOfSeats?: number | null;
  body?: keyof typeof Body | null;
  longitude?: number | null;
  latitude?: number | null;
}

export type NewCar = Omit<ICar, 'id'> & { id: null };
