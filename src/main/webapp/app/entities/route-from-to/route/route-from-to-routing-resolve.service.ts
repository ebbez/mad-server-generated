import { inject } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { EMPTY, Observable, of } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IRouteFromTo } from '../route-from-to.model';
import { RouteFromToService } from '../service/route-from-to.service';

const routeFromToResolve = (route: ActivatedRouteSnapshot): Observable<null | IRouteFromTo> => {
  const id = route.params.id;
  if (id) {
    return inject(RouteFromToService)
      .find(id)
      .pipe(
        mergeMap((routeFromTo: HttpResponse<IRouteFromTo>) => {
          if (routeFromTo.body) {
            return of(routeFromTo.body);
          }
          inject(Router).navigate(['404']);
          return EMPTY;
        }),
      );
  }
  return of(null);
};

export default routeFromToResolve;
