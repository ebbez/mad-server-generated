import { IRouteStop, NewRouteStop } from './route-stop.model';

export const sampleWithRequiredData: IRouteStop = {
  id: 16435,
};

export const sampleWithPartialData: IRouteStop = {
  id: 22613,
  nr: 18767,
};

export const sampleWithFullData: IRouteStop = {
  id: 9971,
  nr: 28581,
};

export const sampleWithNewData: NewRouteStop = {
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
