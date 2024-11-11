import dayjs from 'dayjs/esm';

import { IRouteFromTo, NewRouteFromTo } from './route-from-to.model';

export const sampleWithRequiredData: IRouteFromTo = {
  id: 31375,
};

export const sampleWithPartialData: IRouteFromTo = {
  id: 24765,
  code: 'if',
  description: 'e-mail glossy',
  date: dayjs('2024-11-10'),
};

export const sampleWithFullData: IRouteFromTo = {
  id: 9246,
  code: 'carefree dish apud',
  description: 'yuck regarding whenever',
  date: dayjs('2024-11-10'),
};

export const sampleWithNewData: NewRouteFromTo = {
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
