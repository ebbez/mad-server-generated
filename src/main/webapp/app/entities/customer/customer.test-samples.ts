import dayjs from 'dayjs/esm';

import { ICustomer, NewCustomer } from './customer.model';

export const sampleWithRequiredData: ICustomer = {
  id: 3366,
};

export const sampleWithPartialData: ICustomer = {
  id: 9679,
  from: dayjs('2026-01-06'),
};

export const sampleWithFullData: ICustomer = {
  id: 4149,
  nr: 21252,
  licenseChecked: true,
  lastName: 'Yundt',
  firstName: 'Conor',
  from: dayjs('2026-01-05'),
};

export const sampleWithNewData: NewCustomer = {
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
