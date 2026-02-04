import { ICarReview, NewCarReview } from './car-review.model';

export const sampleWithRequiredData: ICarReview = {
  id: 30018,
};

export const sampleWithPartialData: ICarReview = {
  id: 19928,
};

export const sampleWithFullData: ICarReview = {
  id: 29646,
  review: 'needily beneath uh-huh',
};

export const sampleWithNewData: NewCarReview = {
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
