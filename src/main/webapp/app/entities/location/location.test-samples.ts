import { ILocation, NewLocation } from './location.model';

export const sampleWithRequiredData: ILocation = {
  id: 287,
};

export const sampleWithPartialData: ILocation = {
  id: 10468,
  streetAddress: 'questioningly',
  postalCode: 'chip down geez',
  stateProvince: 'till',
};

export const sampleWithFullData: ILocation = {
  id: 24864,
  streetAddress: 'schlep',
  postalCode: 'hence amidst toe',
  city: 'Lafayettemouth',
  stateProvince: 'fit blindly usually',
};

export const sampleWithNewData: NewLocation = {
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
