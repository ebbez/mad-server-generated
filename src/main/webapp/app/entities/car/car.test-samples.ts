import dayjs from 'dayjs/esm';

import { ICar, NewCar } from './car.model';

export const sampleWithRequiredData: ICar = {
  id: 4218,
};

export const sampleWithPartialData: ICar = {
  id: 31350,
  model: 'off fully',
  fuel: 'ELECTRIC',
  options: 'yahoo gah cone',
  licensePlate: 'bookcase er',
  price: 1572.62,
  nrOfSeats: 23491,
  body: 'HATCHBACK',
  longitude: 1979.72,
};

export const sampleWithFullData: ICar = {
  id: 22796,
  brand: 'testimonial yahoo a',
  model: 'even parody',
  picture: '../fake-data/blob/hipster.png',
  pictureContentType: 'unknown',
  fuel: 'DIESEL',
  options: 'motor meaningfully',
  licensePlate: 'jumbo short-term finally',
  engineSize: 7375,
  modelYear: 16092,
  since: dayjs('2026-01-05'),
  price: 14001.31,
  nrOfSeats: 9352,
  body: 'HATCHBACK',
  longitude: 25244.79,
  latitude: 21834.55,
};

export const sampleWithNewData: NewCar = {
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
