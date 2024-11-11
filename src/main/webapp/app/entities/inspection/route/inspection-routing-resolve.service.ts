import { inject } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { EMPTY, Observable, of } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IInspection } from '../inspection.model';
import { InspectionService } from '../service/inspection.service';

const inspectionResolve = (route: ActivatedRouteSnapshot): Observable<null | IInspection> => {
  const id = route.params.id;
  if (id) {
    return inject(InspectionService)
      .find(id)
      .pipe(
        mergeMap((inspection: HttpResponse<IInspection>) => {
          if (inspection.body) {
            return of(inspection.body);
          }
          inject(Router).navigate(['404']);
          return EMPTY;
        }),
      );
  }
  return of(null);
};

export default inspectionResolve;
