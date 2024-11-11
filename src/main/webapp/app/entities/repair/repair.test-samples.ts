import dayjs from 'dayjs/esm';

import { IRepair, NewRepair } from './repair.model';

export const sampleWithRequiredData: IRepair = {
  id: 6747,
};

export const sampleWithPartialData: IRepair = {
  id: 27344,
  repairStatus: 'DOING',
};

export const sampleWithFullData: IRepair = {
  id: 5399,
  description: 'and',
  repairStatus: 'CANCELLED',
  dateCompleted: dayjs('2024-11-11'),
};

export const sampleWithNewData: NewRepair = {
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
