import { Injectable, inject } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IRouteStop, NewRouteStop } from '../route-stop.model';

export type PartialUpdateRouteStop = Partial<IRouteStop> & Pick<IRouteStop, 'id'>;

export type EntityResponseType = HttpResponse<IRouteStop>;
export type EntityArrayResponseType = HttpResponse<IRouteStop[]>;

@Injectable({ providedIn: 'root' })
export class RouteStopService {
  protected readonly http = inject(HttpClient);
  protected readonly applicationConfigService = inject(ApplicationConfigService);

  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/route-stops');

  create(routeStop: NewRouteStop): Observable<EntityResponseType> {
    return this.http.post<IRouteStop>(this.resourceUrl, routeStop, { observe: 'response' });
  }

  update(routeStop: IRouteStop): Observable<EntityResponseType> {
    return this.http.put<IRouteStop>(`${this.resourceUrl}/${this.getRouteStopIdentifier(routeStop)}`, routeStop, { observe: 'response' });
  }

  partialUpdate(routeStop: PartialUpdateRouteStop): Observable<EntityResponseType> {
    return this.http.patch<IRouteStop>(`${this.resourceUrl}/${this.getRouteStopIdentifier(routeStop)}`, routeStop, { observe: 'response' });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IRouteStop>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IRouteStop[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  getRouteStopIdentifier(routeStop: Pick<IRouteStop, 'id'>): number {
    return routeStop.id;
  }

  compareRouteStop(o1: Pick<IRouteStop, 'id'> | null, o2: Pick<IRouteStop, 'id'> | null): boolean {
    return o1 && o2 ? this.getRouteStopIdentifier(o1) === this.getRouteStopIdentifier(o2) : o1 === o2;
  }

  addRouteStopToCollectionIfMissing<Type extends Pick<IRouteStop, 'id'>>(
    routeStopCollection: Type[],
    ...routeStopsToCheck: (Type | null | undefined)[]
  ): Type[] {
    const routeStops: Type[] = routeStopsToCheck.filter(isPresent);
    if (routeStops.length > 0) {
      const routeStopCollectionIdentifiers = routeStopCollection.map(routeStopItem => this.getRouteStopIdentifier(routeStopItem));
      const routeStopsToAdd = routeStops.filter(routeStopItem => {
        const routeStopIdentifier = this.getRouteStopIdentifier(routeStopItem);
        if (routeStopCollectionIdentifiers.includes(routeStopIdentifier)) {
          return false;
        }
        routeStopCollectionIdentifiers.push(routeStopIdentifier);
        return true;
      });
      return [...routeStopsToAdd, ...routeStopCollection];
    }
    return routeStopCollection;
  }
}
