import { Injectable, inject } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IInspectionPhoto, NewInspectionPhoto } from '../inspection-photo.model';

export type PartialUpdateInspectionPhoto = Partial<IInspectionPhoto> & Pick<IInspectionPhoto, 'id'>;

export type EntityResponseType = HttpResponse<IInspectionPhoto>;
export type EntityArrayResponseType = HttpResponse<IInspectionPhoto[]>;

@Injectable({ providedIn: 'root' })
export class InspectionPhotoService {
  protected readonly http = inject(HttpClient);
  protected readonly applicationConfigService = inject(ApplicationConfigService);

  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/inspection-photos');

  create(inspectionPhoto: NewInspectionPhoto): Observable<EntityResponseType> {
    return this.http.post<IInspectionPhoto>(this.resourceUrl, inspectionPhoto, { observe: 'response' });
  }

  update(inspectionPhoto: IInspectionPhoto): Observable<EntityResponseType> {
    return this.http.put<IInspectionPhoto>(`${this.resourceUrl}/${this.getInspectionPhotoIdentifier(inspectionPhoto)}`, inspectionPhoto, {
      observe: 'response',
    });
  }

  partialUpdate(inspectionPhoto: PartialUpdateInspectionPhoto): Observable<EntityResponseType> {
    return this.http.patch<IInspectionPhoto>(`${this.resourceUrl}/${this.getInspectionPhotoIdentifier(inspectionPhoto)}`, inspectionPhoto, {
      observe: 'response',
    });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IInspectionPhoto>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IInspectionPhoto[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  getInspectionPhotoIdentifier(inspectionPhoto: Pick<IInspectionPhoto, 'id'>): number {
    return inspectionPhoto.id;
  }

  compareInspectionPhoto(o1: Pick<IInspectionPhoto, 'id'> | null, o2: Pick<IInspectionPhoto, 'id'> | null): boolean {
    return o1 && o2 ? this.getInspectionPhotoIdentifier(o1) === this.getInspectionPhotoIdentifier(o2) : o1 === o2;
  }

  addInspectionPhotoToCollectionIfMissing<Type extends Pick<IInspectionPhoto, 'id'>>(
    inspectionPhotoCollection: Type[],
    ...inspectionPhotosToCheck: (Type | null | undefined)[]
  ): Type[] {
    const inspectionPhotos: Type[] = inspectionPhotosToCheck.filter(isPresent);
    if (inspectionPhotos.length > 0) {
      const inspectionPhotoCollectionIdentifiers = inspectionPhotoCollection.map(inspectionPhotoItem =>
        this.getInspectionPhotoIdentifier(inspectionPhotoItem),
      );
      const inspectionPhotosToAdd = inspectionPhotos.filter(inspectionPhotoItem => {
        const inspectionPhotoIdentifier = this.getInspectionPhotoIdentifier(inspectionPhotoItem);
        if (inspectionPhotoCollectionIdentifiers.includes(inspectionPhotoIdentifier)) {
          return false;
        }
        inspectionPhotoCollectionIdentifiers.push(inspectionPhotoIdentifier);
        return true;
      });
      return [...inspectionPhotosToAdd, ...inspectionPhotoCollection];
    }
    return inspectionPhotoCollection;
  }
}
