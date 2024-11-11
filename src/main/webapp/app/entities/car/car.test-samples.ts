import dayjs from 'dayjs/esm';

import { ICar, NewCar } from './car.model';

export const sampleWithRequiredData: ICar = {
  id: 16274,
};

export const sampleWithPartialData: ICar = {
  id: 4204,
  brand: 'eulogise following',
  model: 'quizzically in upon',
  picture: '../fake-data/blob/hipster.png',
  pictureContentType: 'unknown',
  engineSize: 19666,
  modelYear: 1272,
  price: 112.46,
  body: 'MPV',
  longitude: 28479.83,
  latitude: 27635.81,
};

export const sampleWithFullData: ICar = {
  id: 6964,
  brand: 'psst represent',
  model: 'wrathful pinstripe major',
  picture: '../fake-data/blob/hipster.png',
  pictureContentType: 'unknown',
  fuel: 'ELECTRIC',
  options: 'merit',
  licensePlate: 'finally',
  engineSize: 28722,
  modelYear: 25833,
  since: dayjs('2024-11-10'),
  price: 22664.94,
  nrOfSeats: 29976,
  body: 'COUPE',
  longitude: 28766.15,
  latitude: 13224.25,
};

export const sampleWithNewData: NewCar = {
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
