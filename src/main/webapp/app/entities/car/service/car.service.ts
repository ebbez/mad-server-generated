import { Injectable, inject } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable, map } from 'rxjs';

import dayjs from 'dayjs/esm';

import { isPresent } from 'app/core/util/operators';
import { DATE_FORMAT } from 'app/config/input.constants';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { ICar, NewCar } from '../car.model';

export type PartialUpdateCar = Partial<ICar> & Pick<ICar, 'id'>;

type RestOf<T extends ICar | NewCar> = Omit<T, 'since'> & {
  since?: string | null;
};

export type RestCar = RestOf<ICar>;

export type NewRestCar = RestOf<NewCar>;

export type PartialUpdateRestCar = RestOf<PartialUpdateCar>;

export type EntityResponseType = HttpResponse<ICar>;
export type EntityArrayResponseType = HttpResponse<ICar[]>;

@Injectable({ providedIn: 'root' })
export class CarService {
  protected readonly http = inject(HttpClient);
  protected readonly applicationConfigService = inject(ApplicationConfigService);

  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/cars');

  create(car: NewCar): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(car);
    return this.http.post<RestCar>(this.resourceUrl, copy, { observe: 'response' }).pipe(map(res => this.convertResponseFromServer(res)));
  }

  update(car: ICar): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(car);
    return this.http
      .put<RestCar>(`${this.resourceUrl}/${this.getCarIdentifier(car)}`, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  partialUpdate(car: PartialUpdateCar): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(car);
    return this.http
      .patch<RestCar>(`${this.resourceUrl}/${this.getCarIdentifier(car)}`, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<RestCar>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<RestCar[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map(res => this.convertResponseArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  getCarIdentifier(car: Pick<ICar, 'id'>): number {
    return car.id;
  }

  compareCar(o1: Pick<ICar, 'id'> | null, o2: Pick<ICar, 'id'> | null): boolean {
    return o1 && o2 ? this.getCarIdentifier(o1) === this.getCarIdentifier(o2) : o1 === o2;
  }

  addCarToCollectionIfMissing<Type extends Pick<ICar, 'id'>>(carCollection: Type[], ...carsToCheck: (Type | null | undefined)[]): Type[] {
    const cars: Type[] = carsToCheck.filter(isPresent);
    if (cars.length > 0) {
      const carCollectionIdentifiers = carCollection.map(carItem => this.getCarIdentifier(carItem));
      const carsToAdd = cars.filter(carItem => {
        const carIdentifier = this.getCarIdentifier(carItem);
        if (carCollectionIdentifiers.includes(carIdentifier)) {
          return false;
        }
        carCollectionIdentifiers.push(carIdentifier);
        return true;
      });
      return [...carsToAdd, ...carCollection];
    }
    return carCollection;
  }

  protected convertDateFromClient<T extends ICar | NewCar | PartialUpdateCar>(car: T): RestOf<T> {
    return {
      ...car,
      since: car.since?.format(DATE_FORMAT) ?? null,
    };
  }

  protected convertDateFromServer(restCar: RestCar): ICar {
    return {
      ...restCar,
      since: restCar.since ? dayjs(restCar.since) : undefined,
    };
  }

  protected convertResponseFromServer(res: HttpResponse<RestCar>): HttpResponse<ICar> {
    return res.clone({
      body: res.body ? this.convertDateFromServer(res.body) : null,
    });
  }

  protected convertResponseArrayFromServer(res: HttpResponse<RestCar[]>): HttpResponse<ICar[]> {
    return res.clone({
      body: res.body ? res.body.map(item => this.convertDateFromServer(item)) : null,
    });
  }
}
