import { IRouteStop, NewRouteStop } from './route-stop.model';

export const sampleWithRequiredData: IRouteStop = {
  id: 9931,
};

export const sampleWithPartialData: IRouteStop = {
  id: 20007,
  nr: 32330,
};

export const sampleWithFullData: IRouteStop = {
  id: 13017,
  nr: 13457,
};

export const sampleWithNewData: NewRouteStop = {
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
