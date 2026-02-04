import dayjs from 'dayjs/esm';

import { IRouteFromTo, NewRouteFromTo } from './route-from-to.model';

export const sampleWithRequiredData: IRouteFromTo = {
  id: 19934,
};

export const sampleWithPartialData: IRouteFromTo = {
  id: 811,
  code: 'terribly',
};

export const sampleWithFullData: IRouteFromTo = {
  id: 1241,
  code: 'whenever breed since',
  description: 'steak overvalue hence',
  date: dayjs('2026-01-06'),
};

export const sampleWithNewData: NewRouteFromTo = {
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
