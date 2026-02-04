import dayjs from 'dayjs/esm';

import { IRepair, NewRepair } from './repair.model';

export const sampleWithRequiredData: IRepair = {
  id: 31331,
};

export const sampleWithPartialData: IRepair = {
  id: 4437,
  description: 'odd wash whoever',
};

export const sampleWithFullData: IRepair = {
  id: 9867,
  description: 'subsidy abaft',
  repairStatus: 'CANCELLED',
  dateCompleted: dayjs('2026-01-06'),
};

export const sampleWithNewData: NewRepair = {
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
