import { Injectable, inject } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable, map } from 'rxjs';

import dayjs from 'dayjs/esm';

import { isPresent } from 'app/core/util/operators';
import { DATE_FORMAT } from 'app/config/input.constants';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IRental, NewRental } from '../rental.model';

export type PartialUpdateRental = Partial<IRental> & Pick<IRental, 'id'>;

type RestOf<T extends IRental | NewRental> = Omit<T, 'fromDate' | 'toDate'> & {
  fromDate?: string | null;
  toDate?: string | null;
};

export type RestRental = RestOf<IRental>;

export type NewRestRental = RestOf<NewRental>;

export type PartialUpdateRestRental = RestOf<PartialUpdateRental>;

export type EntityResponseType = HttpResponse<IRental>;
export type EntityArrayResponseType = HttpResponse<IRental[]>;

@Injectable({ providedIn: 'root' })
export class RentalService {
  protected readonly http = inject(HttpClient);
  protected readonly applicationConfigService = inject(ApplicationConfigService);

  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/rentals');

  create(rental: NewRental): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(rental);
    return this.http
      .post<RestRental>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  update(rental: IRental): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(rental);
    return this.http
      .put<RestRental>(`${this.resourceUrl}/${this.getRentalIdentifier(rental)}`, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  partialUpdate(rental: PartialUpdateRental): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(rental);
    return this.http
      .patch<RestRental>(`${this.resourceUrl}/${this.getRentalIdentifier(rental)}`, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<RestRental>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<RestRental[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map(res => this.convertResponseArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  getRentalIdentifier(rental: Pick<IRental, 'id'>): number {
    return rental.id;
  }

  compareRental(o1: Pick<IRental, 'id'> | null, o2: Pick<IRental, 'id'> | null): boolean {
    return o1 && o2 ? this.getRentalIdentifier(o1) === this.getRentalIdentifier(o2) : o1 === o2;
  }

  addRentalToCollectionIfMissing<Type extends Pick<IRental, 'id'>>(
    rentalCollection: Type[],
    ...rentalsToCheck: (Type | null | undefined)[]
  ): Type[] {
    const rentals: Type[] = rentalsToCheck.filter(isPresent);
    if (rentals.length > 0) {
      const rentalCollectionIdentifiers = rentalCollection.map(rentalItem => this.getRentalIdentifier(rentalItem));
      const rentalsToAdd = rentals.filter(rentalItem => {
        const rentalIdentifier = this.getRentalIdentifier(rentalItem);
        if (rentalCollectionIdentifiers.includes(rentalIdentifier)) {
          return false;
        }
        rentalCollectionIdentifiers.push(rentalIdentifier);
        return true;
      });
      return [...rentalsToAdd, ...rentalCollection];
    }
    return rentalCollection;
  }

  protected convertDateFromClient<T extends IRental | NewRental | PartialUpdateRental>(rental: T): RestOf<T> {
    return {
      ...rental,
      fromDate: rental.fromDate?.format(DATE_FORMAT) ?? null,
      toDate: rental.toDate?.format(DATE_FORMAT) ?? null,
    };
  }

  protected convertDateFromServer(restRental: RestRental): IRental {
    return {
      ...restRental,
      fromDate: restRental.fromDate ? dayjs(restRental.fromDate) : undefined,
      toDate: restRental.toDate ? dayjs(restRental.toDate) : undefined,
    };
  }

  protected convertResponseFromServer(res: HttpResponse<RestRental>): HttpResponse<IRental> {
    return res.clone({
      body: res.body ? this.convertDateFromServer(res.body) : null,
    });
  }

  protected convertResponseArrayFromServer(res: HttpResponse<RestRental[]>): HttpResponse<IRental[]> {
    return res.clone({
      body: res.body ? res.body.map(item => this.convertDateFromServer(item)) : null,
    });
  }
}
