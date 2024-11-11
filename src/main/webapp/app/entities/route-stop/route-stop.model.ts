import { IRouteFromTo } from 'app/entities/route-from-to/route-from-to.model';
import { ILocation } from 'app/entities/location/location.model';

export interface IRouteStop {
  id: number;
  nr?: number | null;
  routeFromTo?: IRouteFromTo | null;
  location?: ILocation | null;
}

export type NewRouteStop = Omit<IRouteStop, 'id'> & { id: null };
