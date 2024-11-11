import { IUser } from './user.model';

export const sampleWithRequiredData: IUser = {
  id: 13889,
  login: 'gN@c13pT4\\zIVEZO\\sz\\08n\\T-BJI',
};

export const sampleWithPartialData: IUser = {
  id: 11826,
  login: 'iB@-JKqF\\3Y8fC\\V3\\Y4nDtB',
};

export const sampleWithFullData: IUser = {
  id: 23195,
  login: 'kCRoL',
};
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
