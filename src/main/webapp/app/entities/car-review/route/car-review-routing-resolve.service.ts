import { inject } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { EMPTY, Observable, of } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { ICarReview } from '../car-review.model';
import { CarReviewService } from '../service/car-review.service';

const carReviewResolve = (route: ActivatedRouteSnapshot): Observable<null | ICarReview> => {
  const id = route.params.id;
  if (id) {
    return inject(CarReviewService)
      .find(id)
      .pipe(
        mergeMap((carReview: HttpResponse<ICarReview>) => {
          if (carReview.body) {
            return of(carReview.body);
          }
          inject(Router).navigate(['404']);
          return EMPTY;
        }),
      );
  }
  return of(null);
};

export default carReviewResolve;
