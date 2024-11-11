import { Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { ASC } from 'app/config/navigation.constants';
import RentalResolve from './route/rental-routing-resolve.service';

const rentalRoute: Routes = [
  {
    path: '',
    loadComponent: () => import('./list/rental.component').then(m => m.RentalComponent),
    data: {
      defaultSort: `id,${ASC}`,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    loadComponent: () => import('./detail/rental-detail.component').then(m => m.RentalDetailComponent),
    resolve: {
      rental: RentalResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    loadComponent: () => import('./update/rental-update.component').then(m => m.RentalUpdateComponent),
    resolve: {
      rental: RentalResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    loadComponent: () => import('./update/rental-update.component').then(m => m.RentalUpdateComponent),
    resolve: {
      rental: RentalResolve,
    },
    canActivate: [UserRouteAccessService],
  },
];

export default rentalRoute;
