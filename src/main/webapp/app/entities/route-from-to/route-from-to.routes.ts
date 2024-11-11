import { Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { ASC } from 'app/config/navigation.constants';
import RouteFromToResolve from './route/route-from-to-routing-resolve.service';

const routeFromToRoute: Routes = [
  {
    path: '',
    loadComponent: () => import('./list/route-from-to.component').then(m => m.RouteFromToComponent),
    data: {
      defaultSort: `id,${ASC}`,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    loadComponent: () => import('./detail/route-from-to-detail.component').then(m => m.RouteFromToDetailComponent),
    resolve: {
      routeFromTo: RouteFromToResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    loadComponent: () => import('./update/route-from-to-update.component').then(m => m.RouteFromToUpdateComponent),
    resolve: {
      routeFromTo: RouteFromToResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    loadComponent: () => import('./update/route-from-to-update.component').then(m => m.RouteFromToUpdateComponent),
    resolve: {
      routeFromTo: RouteFromToResolve,
    },
    canActivate: [UserRouteAccessService],
  },
];

export default routeFromToRoute;
