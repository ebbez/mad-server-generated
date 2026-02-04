import dayjs from 'dayjs/esm';

import { IInspection, NewInspection } from './inspection.model';

export const sampleWithRequiredData: IInspection = {
  id: 13424,
};

export const sampleWithPartialData: IInspection = {
  id: 29035,
  code: 'shampoo',
};

export const sampleWithFullData: IInspection = {
  id: 19872,
  code: 'far-off',
  odometer: 312,
  result: 'abaft carelessly',
  description: 'at',
  photo: '../fake-data/blob/hipster.png',
  photoContentType: 'unknown',
  completed: dayjs('2026-01-06T00:30'),
};

export const sampleWithNewData: NewInspection = {
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
