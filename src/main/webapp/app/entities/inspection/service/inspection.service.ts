import { Injectable, inject } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable, map } from 'rxjs';

import dayjs from 'dayjs/esm';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IInspection, NewInspection } from '../inspection.model';

export type PartialUpdateInspection = Partial<IInspection> & Pick<IInspection, 'id'>;

type RestOf<T extends IInspection | NewInspection> = Omit<T, 'completed'> & {
  completed?: string | null;
};

export type RestInspection = RestOf<IInspection>;

export type NewRestInspection = RestOf<NewInspection>;

export type PartialUpdateRestInspection = RestOf<PartialUpdateInspection>;

export type EntityResponseType = HttpResponse<IInspection>;
export type EntityArrayResponseType = HttpResponse<IInspection[]>;

@Injectable({ providedIn: 'root' })
export class InspectionService {
  protected readonly http = inject(HttpClient);
  protected readonly applicationConfigService = inject(ApplicationConfigService);

  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/inspections');

  create(inspection: NewInspection): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(inspection);
    return this.http
      .post<RestInspection>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  update(inspection: IInspection): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(inspection);
    return this.http
      .put<RestInspection>(`${this.resourceUrl}/${this.getInspectionIdentifier(inspection)}`, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  partialUpdate(inspection: PartialUpdateInspection): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(inspection);
    return this.http
      .patch<RestInspection>(`${this.resourceUrl}/${this.getInspectionIdentifier(inspection)}`, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<RestInspection>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<RestInspection[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map(res => this.convertResponseArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  getInspectionIdentifier(inspection: Pick<IInspection, 'id'>): number {
    return inspection.id;
  }

  compareInspection(o1: Pick<IInspection, 'id'> | null, o2: Pick<IInspection, 'id'> | null): boolean {
    return o1 && o2 ? this.getInspectionIdentifier(o1) === this.getInspectionIdentifier(o2) : o1 === o2;
  }

  addInspectionToCollectionIfMissing<Type extends Pick<IInspection, 'id'>>(
    inspectionCollection: Type[],
    ...inspectionsToCheck: (Type | null | undefined)[]
  ): Type[] {
    const inspections: Type[] = inspectionsToCheck.filter(isPresent);
    if (inspections.length > 0) {
      const inspectionCollectionIdentifiers = inspectionCollection.map(inspectionItem => this.getInspectionIdentifier(inspectionItem));
      const inspectionsToAdd = inspections.filter(inspectionItem => {
        const inspectionIdentifier = this.getInspectionIdentifier(inspectionItem);
        if (inspectionCollectionIdentifiers.includes(inspectionIdentifier)) {
          return false;
        }
        inspectionCollectionIdentifiers.push(inspectionIdentifier);
        return true;
      });
      return [...inspectionsToAdd, ...inspectionCollection];
    }
    return inspectionCollection;
  }

  protected convertDateFromClient<T extends IInspection | NewInspection | PartialUpdateInspection>(inspection: T): RestOf<T> {
    return {
      ...inspection,
      completed: inspection.completed?.toJSON() ?? null,
    };
  }

  protected convertDateFromServer(restInspection: RestInspection): IInspection {
    return {
      ...restInspection,
      completed: restInspection.completed ? dayjs(restInspection.completed) : undefined,
    };
  }

  protected convertResponseFromServer(res: HttpResponse<RestInspection>): HttpResponse<IInspection> {
    return res.clone({
      body: res.body ? this.convertDateFromServer(res.body) : null,
    });
  }

  protected convertResponseArrayFromServer(res: HttpResponse<RestInspection[]>): HttpResponse<IInspection[]> {
    return res.clone({
      body: res.body ? res.body.map(item => this.convertDateFromServer(item)) : null,
    });
  }
}
