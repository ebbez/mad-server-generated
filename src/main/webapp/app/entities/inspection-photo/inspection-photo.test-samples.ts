import { IInspectionPhoto, NewInspectionPhoto } from './inspection-photo.model';

export const sampleWithRequiredData: IInspectionPhoto = {
  id: 20027,
};

export const sampleWithPartialData: IInspectionPhoto = {
  id: 29744,
  photo: '../fake-data/blob/hipster.png',
  photoContentType: 'unknown',
};

export const sampleWithFullData: IInspectionPhoto = {
  id: 13405,
  photo: '../fake-data/blob/hipster.png',
  photoContentType: 'unknown',
};

export const sampleWithNewData: NewInspectionPhoto = {
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
