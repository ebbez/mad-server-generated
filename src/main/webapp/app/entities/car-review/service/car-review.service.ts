import { Injectable, inject } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { ICarReview, NewCarReview } from '../car-review.model';

export type PartialUpdateCarReview = Partial<ICarReview> & Pick<ICarReview, 'id'>;

export type EntityResponseType = HttpResponse<ICarReview>;
export type EntityArrayResponseType = HttpResponse<ICarReview[]>;

@Injectable({ providedIn: 'root' })
export class CarReviewService {
  protected readonly http = inject(HttpClient);
  protected readonly applicationConfigService = inject(ApplicationConfigService);

  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/car-reviews');

  create(carReview: NewCarReview): Observable<EntityResponseType> {
    return this.http.post<ICarReview>(this.resourceUrl, carReview, { observe: 'response' });
  }

  update(carReview: ICarReview): Observable<EntityResponseType> {
    return this.http.put<ICarReview>(`${this.resourceUrl}/${this.getCarReviewIdentifier(carReview)}`, carReview, { observe: 'response' });
  }

  partialUpdate(carReview: PartialUpdateCarReview): Observable<EntityResponseType> {
    return this.http.patch<ICarReview>(`${this.resourceUrl}/${this.getCarReviewIdentifier(carReview)}`, carReview, { observe: 'response' });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<ICarReview>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<ICarReview[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  getCarReviewIdentifier(carReview: Pick<ICarReview, 'id'>): number {
    return carReview.id;
  }

  compareCarReview(o1: Pick<ICarReview, 'id'> | null, o2: Pick<ICarReview, 'id'> | null): boolean {
    return o1 && o2 ? this.getCarReviewIdentifier(o1) === this.getCarReviewIdentifier(o2) : o1 === o2;
  }

  addCarReviewToCollectionIfMissing<Type extends Pick<ICarReview, 'id'>>(
    carReviewCollection: Type[],
    ...carReviewsToCheck: (Type | null | undefined)[]
  ): Type[] {
    const carReviews: Type[] = carReviewsToCheck.filter(isPresent);
    if (carReviews.length > 0) {
      const carReviewCollectionIdentifiers = carReviewCollection.map(carReviewItem => this.getCarReviewIdentifier(carReviewItem));
      const carReviewsToAdd = carReviews.filter(carReviewItem => {
        const carReviewIdentifier = this.getCarReviewIdentifier(carReviewItem);
        if (carReviewCollectionIdentifiers.includes(carReviewIdentifier)) {
          return false;
        }
        carReviewCollectionIdentifiers.push(carReviewIdentifier);
        return true;
      });
      return [...carReviewsToAdd, ...carReviewCollection];
    }
    return carReviewCollection;
  }
}
