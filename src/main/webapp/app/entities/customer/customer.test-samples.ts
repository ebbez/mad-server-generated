import dayjs from 'dayjs/esm';

import { ICustomer, NewCustomer } from './customer.model';

export const sampleWithRequiredData: ICustomer = {
  id: 23362,
};

export const sampleWithPartialData: ICustomer = {
  id: 19268,
  nr: 406,
  lastName: 'Von',
};

export const sampleWithFullData: ICustomer = {
  id: 7562,
  nr: 18877,
  lastName: 'Mayert',
  firstName: 'Cordell',
  from: dayjs('2024-11-10'),
};

export const sampleWithNewData: NewCustomer = {
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
