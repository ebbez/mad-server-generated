import { Injectable, inject } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable, map } from 'rxjs';

import dayjs from 'dayjs/esm';

import { isPresent } from 'app/core/util/operators';
import { DATE_FORMAT } from 'app/config/input.constants';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IRepair, NewRepair } from '../repair.model';

export type PartialUpdateRepair = Partial<IRepair> & Pick<IRepair, 'id'>;

type RestOf<T extends IRepair | NewRepair> = Omit<T, 'dateCompleted'> & {
  dateCompleted?: string | null;
};

export type RestRepair = RestOf<IRepair>;

export type NewRestRepair = RestOf<NewRepair>;

export type PartialUpdateRestRepair = RestOf<PartialUpdateRepair>;

export type EntityResponseType = HttpResponse<IRepair>;
export type EntityArrayResponseType = HttpResponse<IRepair[]>;

@Injectable({ providedIn: 'root' })
export class RepairService {
  protected readonly http = inject(HttpClient);
  protected readonly applicationConfigService = inject(ApplicationConfigService);

  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/repairs');

  create(repair: NewRepair): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(repair);
    return this.http
      .post<RestRepair>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  update(repair: IRepair): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(repair);
    return this.http
      .put<RestRepair>(`${this.resourceUrl}/${this.getRepairIdentifier(repair)}`, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  partialUpdate(repair: PartialUpdateRepair): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(repair);
    return this.http
      .patch<RestRepair>(`${this.resourceUrl}/${this.getRepairIdentifier(repair)}`, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<RestRepair>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<RestRepair[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map(res => this.convertResponseArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  getRepairIdentifier(repair: Pick<IRepair, 'id'>): number {
    return repair.id;
  }

  compareRepair(o1: Pick<IRepair, 'id'> | null, o2: Pick<IRepair, 'id'> | null): boolean {
    return o1 && o2 ? this.getRepairIdentifier(o1) === this.getRepairIdentifier(o2) : o1 === o2;
  }

  addRepairToCollectionIfMissing<Type extends Pick<IRepair, 'id'>>(
    repairCollection: Type[],
    ...repairsToCheck: (Type | null | undefined)[]
  ): Type[] {
    const repairs: Type[] = repairsToCheck.filter(isPresent);
    if (repairs.length > 0) {
      const repairCollectionIdentifiers = repairCollection.map(repairItem => this.getRepairIdentifier(repairItem));
      const repairsToAdd = repairs.filter(repairItem => {
        const repairIdentifier = this.getRepairIdentifier(repairItem);
        if (repairCollectionIdentifiers.includes(repairIdentifier)) {
          return false;
        }
        repairCollectionIdentifiers.push(repairIdentifier);
        return true;
      });
      return [...repairsToAdd, ...repairCollection];
    }
    return repairCollection;
  }

  protected convertDateFromClient<T extends IRepair | NewRepair | PartialUpdateRepair>(repair: T): RestOf<T> {
    return {
      ...repair,
      dateCompleted: repair.dateCompleted?.format(DATE_FORMAT) ?? null,
    };
  }

  protected convertDateFromServer(restRepair: RestRepair): IRepair {
    return {
      ...restRepair,
      dateCompleted: restRepair.dateCompleted ? dayjs(restRepair.dateCompleted) : undefined,
    };
  }

  protected convertResponseFromServer(res: HttpResponse<RestRepair>): HttpResponse<IRepair> {
    return res.clone({
      body: res.body ? this.convertDateFromServer(res.body) : null,
    });
  }

  protected convertResponseArrayFromServer(res: HttpResponse<RestRepair[]>): HttpResponse<IRepair[]> {
    return res.clone({
      body: res.body ? res.body.map(item => this.convertDateFromServer(item)) : null,
    });
  }
}
