import { Injectable, inject } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable, map } from 'rxjs';

import dayjs from 'dayjs/esm';

import { isPresent } from 'app/core/util/operators';
import { DATE_FORMAT } from 'app/config/input.constants';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IRouteFromTo, NewRouteFromTo } from '../route-from-to.model';

export type PartialUpdateRouteFromTo = Partial<IRouteFromTo> & Pick<IRouteFromTo, 'id'>;

type RestOf<T extends IRouteFromTo | NewRouteFromTo> = Omit<T, 'date'> & {
  date?: string | null;
};

export type RestRouteFromTo = RestOf<IRouteFromTo>;

export type NewRestRouteFromTo = RestOf<NewRouteFromTo>;

export type PartialUpdateRestRouteFromTo = RestOf<PartialUpdateRouteFromTo>;

export type EntityResponseType = HttpResponse<IRouteFromTo>;
export type EntityArrayResponseType = HttpResponse<IRouteFromTo[]>;

@Injectable({ providedIn: 'root' })
export class RouteFromToService {
  protected readonly http = inject(HttpClient);
  protected readonly applicationConfigService = inject(ApplicationConfigService);

  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/route-from-tos');

  create(routeFromTo: NewRouteFromTo): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(routeFromTo);
    return this.http
      .post<RestRouteFromTo>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  update(routeFromTo: IRouteFromTo): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(routeFromTo);
    return this.http
      .put<RestRouteFromTo>(`${this.resourceUrl}/${this.getRouteFromToIdentifier(routeFromTo)}`, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  partialUpdate(routeFromTo: PartialUpdateRouteFromTo): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(routeFromTo);
    return this.http
      .patch<RestRouteFromTo>(`${this.resourceUrl}/${this.getRouteFromToIdentifier(routeFromTo)}`, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<RestRouteFromTo>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<RestRouteFromTo[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map(res => this.convertResponseArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  getRouteFromToIdentifier(routeFromTo: Pick<IRouteFromTo, 'id'>): number {
    return routeFromTo.id;
  }

  compareRouteFromTo(o1: Pick<IRouteFromTo, 'id'> | null, o2: Pick<IRouteFromTo, 'id'> | null): boolean {
    return o1 && o2 ? this.getRouteFromToIdentifier(o1) === this.getRouteFromToIdentifier(o2) : o1 === o2;
  }

  addRouteFromToToCollectionIfMissing<Type extends Pick<IRouteFromTo, 'id'>>(
    routeFromToCollection: Type[],
    ...routeFromTosToCheck: (Type | null | undefined)[]
  ): Type[] {
    const routeFromTos: Type[] = routeFromTosToCheck.filter(isPresent);
    if (routeFromTos.length > 0) {
      const routeFromToCollectionIdentifiers = routeFromToCollection.map(routeFromToItem => this.getRouteFromToIdentifier(routeFromToItem));
      const routeFromTosToAdd = routeFromTos.filter(routeFromToItem => {
        const routeFromToIdentifier = this.getRouteFromToIdentifier(routeFromToItem);
        if (routeFromToCollectionIdentifiers.includes(routeFromToIdentifier)) {
          return false;
        }
        routeFromToCollectionIdentifiers.push(routeFromToIdentifier);
        return true;
      });
      return [...routeFromTosToAdd, ...routeFromToCollection];
    }
    return routeFromToCollection;
  }

  protected convertDateFromClient<T extends IRouteFromTo | NewRouteFromTo | PartialUpdateRouteFromTo>(routeFromTo: T): RestOf<T> {
    return {
      ...routeFromTo,
      date: routeFromTo.date?.format(DATE_FORMAT) ?? null,
    };
  }

  protected convertDateFromServer(restRouteFromTo: RestRouteFromTo): IRouteFromTo {
    return {
      ...restRouteFromTo,
      date: restRouteFromTo.date ? dayjs(restRouteFromTo.date) : undefined,
    };
  }

  protected convertResponseFromServer(res: HttpResponse<RestRouteFromTo>): HttpResponse<IRouteFromTo> {
    return res.clone({
      body: res.body ? this.convertDateFromServer(res.body) : null,
    });
  }

  protected convertResponseArrayFromServer(res: HttpResponse<RestRouteFromTo[]>): HttpResponse<IRouteFromTo[]> {
    return res.clone({
      body: res.body ? res.body.map(item => this.convertDateFromServer(item)) : null,
    });
  }
}
