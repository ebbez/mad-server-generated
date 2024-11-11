import dayjs from 'dayjs/esm';

import { IRental, NewRental } from './rental.model';

export const sampleWithRequiredData: IRental = {
  id: 16108,
};

export const sampleWithPartialData: IRental = {
  id: 20433,
  code: 'instead oh',
  longitude: 3253.5,
  fromDate: dayjs('2024-11-10'),
  state: 'ACTIVE',
};

export const sampleWithFullData: IRental = {
  id: 19870,
  code: 'actually',
  longitude: 5735.86,
  latitude: 4295.13,
  fromDate: dayjs('2024-11-10'),
  toDate: dayjs('2024-11-10'),
  state: 'RETURNED',
};

export const sampleWithNewData: NewRental = {
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
