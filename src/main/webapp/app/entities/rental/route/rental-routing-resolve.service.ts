import { inject } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { EMPTY, Observable, of } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IRental } from '../rental.model';
import { RentalService } from '../service/rental.service';

const rentalResolve = (route: ActivatedRouteSnapshot): Observable<null | IRental> => {
  const id = route.params.id;
  if (id) {
    return inject(RentalService)
      .find(id)
      .pipe(
        mergeMap((rental: HttpResponse<IRental>) => {
          if (rental.body) {
            return of(rental.body);
          }
          inject(Router).navigate(['404']);
          return EMPTY;
        }),
      );
  }
  return of(null);
};

export default rentalResolve;
