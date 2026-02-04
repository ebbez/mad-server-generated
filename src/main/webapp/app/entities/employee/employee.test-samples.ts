import dayjs from 'dayjs/esm';

import { IEmployee, NewEmployee } from './employee.model';

export const sampleWithRequiredData: IEmployee = {
  id: 8899,
};

export const sampleWithPartialData: IEmployee = {
  id: 16412,
  nr: 10051,
  lastName: 'Veum',
  firstName: 'Jade',
  from: dayjs('2026-01-06'),
};

export const sampleWithFullData: IEmployee = {
  id: 19019,
  nr: 25574,
  lastName: 'Luettgen',
  firstName: 'Nicholaus',
  from: dayjs('2026-01-06'),
};

export const sampleWithNewData: NewEmployee = {
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
