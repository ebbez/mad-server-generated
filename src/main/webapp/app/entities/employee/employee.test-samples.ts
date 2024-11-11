import dayjs from 'dayjs/esm';

import { IEmployee, NewEmployee } from './employee.model';

export const sampleWithRequiredData: IEmployee = {
  id: 9716,
};

export const sampleWithPartialData: IEmployee = {
  id: 19331,
  nr: 25614,
  firstName: 'Donavon',
  from: dayjs('2024-11-10'),
};

export const sampleWithFullData: IEmployee = {
  id: 19426,
  nr: 15830,
  lastName: 'Lehner',
  firstName: 'Loy',
  from: dayjs('2024-11-11'),
};

export const sampleWithNewData: NewEmployee = {
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
