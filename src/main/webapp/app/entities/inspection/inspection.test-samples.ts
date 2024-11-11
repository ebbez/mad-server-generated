import dayjs from 'dayjs/esm';

import { IInspection, NewInspection } from './inspection.model';

export const sampleWithRequiredData: IInspection = {
  id: 9666,
};

export const sampleWithPartialData: IInspection = {
  id: 10307,
  odometer: 31805,
  description: 'mushy indeed yowza',
};

export const sampleWithFullData: IInspection = {
  id: 14011,
  code: 'motor lifestyle',
  odometer: 15649,
  result: 'scratchy wallaby inwardly',
  description: 'phooey writ',
  photo: '../fake-data/blob/hipster.png',
  photoContentType: 'unknown',
  completed: dayjs('2024-11-10T09:28'),
};

export const sampleWithNewData: NewInspection = {
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
