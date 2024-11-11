import { inject } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { EMPTY, Observable, of } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IRepair } from '../repair.model';
import { RepairService } from '../service/repair.service';

const repairResolve = (route: ActivatedRouteSnapshot): Observable<null | IRepair> => {
  const id = route.params.id;
  if (id) {
    return inject(RepairService)
      .find(id)
      .pipe(
        mergeMap((repair: HttpResponse<IRepair>) => {
          if (repair.body) {
            return of(repair.body);
          }
          inject(Router).navigate(['404']);
          return EMPTY;
        }),
      );
  }
  return of(null);
};

export default repairResolve;
