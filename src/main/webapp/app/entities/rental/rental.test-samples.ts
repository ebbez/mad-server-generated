import dayjs from 'dayjs/esm';

import { IRental, NewRental } from './rental.model';

export const sampleWithRequiredData: IRental = {
  id: 5216,
};

export const sampleWithPartialData: IRental = {
  id: 14275,
  longitude: 25041.86,
  fromDate: dayjs('2026-01-06'),
  toDate: dayjs('2026-01-06'),
};

export const sampleWithFullData: IRental = {
  id: 4722,
  code: 'as',
  longitude: 8801.24,
  latitude: 3331.3,
  fromDate: dayjs('2026-01-06'),
  toDate: dayjs('2026-01-06'),
  state: 'RESERVED',
};

export const sampleWithNewData: NewRental = {
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
