import { inject } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { EMPTY, Observable, of } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IInspectionPhoto } from '../inspection-photo.model';
import { InspectionPhotoService } from '../service/inspection-photo.service';

const inspectionPhotoResolve = (route: ActivatedRouteSnapshot): Observable<null | IInspectionPhoto> => {
  const id = route.params.id;
  if (id) {
    return inject(InspectionPhotoService)
      .find(id)
      .pipe(
        mergeMap((inspectionPhoto: HttpResponse<IInspectionPhoto>) => {
          if (inspectionPhoto.body) {
            return of(inspectionPhoto.body);
          }
          inject(Router).navigate(['404']);
          return EMPTY;
        }),
      );
  }
  return of(null);
};

export default inspectionPhotoResolve;
