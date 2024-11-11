import { IAuthority, NewAuthority } from './authority.model';

export const sampleWithRequiredData: IAuthority = {
  name: 'e1847a88-b8b2-4323-900e-39e4e70f9e35',
};

export const sampleWithPartialData: IAuthority = {
  name: 'fe800c8e-10cf-47db-bd1a-632ab9781ca3',
};

export const sampleWithFullData: IAuthority = {
  name: '9fa07795-bdfb-4f23-b445-11120526d605',
};

export const sampleWithNewData: NewAuthority = {
  name: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
