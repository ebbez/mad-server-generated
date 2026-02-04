import { Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { ASC } from 'app/config/navigation.constants';
import CarReviewResolve from './route/car-review-routing-resolve.service';

const carReviewRoute: Routes = [
  {
    path: '',
    loadComponent: () => import('./list/car-review.component').then(m => m.CarReviewComponent),
    data: {
      defaultSort: `id,${ASC}`,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    loadComponent: () => import('./detail/car-review-detail.component').then(m => m.CarReviewDetailComponent),
    resolve: {
      carReview: CarReviewResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    loadComponent: () => import('./update/car-review-update.component').then(m => m.CarReviewUpdateComponent),
    resolve: {
      carReview: CarReviewResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    loadComponent: () => import('./update/car-review-update.component').then(m => m.CarReviewUpdateComponent),
    resolve: {
      carReview: CarReviewResolve,
    },
    canActivate: [UserRouteAccessService],
  },
];

export default carReviewRoute;
