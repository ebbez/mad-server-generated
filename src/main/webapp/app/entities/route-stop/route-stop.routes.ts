import { Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { ASC } from 'app/config/navigation.constants';
import RouteStopResolve from './route/route-stop-routing-resolve.service';

const routeStopRoute: Routes = [
  {
    path: '',
    loadComponent: () => import('./list/route-stop.component').then(m => m.RouteStopComponent),
    data: {
      defaultSort: `id,${ASC}`,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    loadComponent: () => import('./detail/route-stop-detail.component').then(m => m.RouteStopDetailComponent),
    resolve: {
      routeStop: RouteStopResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    loadComponent: () => import('./update/route-stop-update.component').then(m => m.RouteStopUpdateComponent),
    resolve: {
      routeStop: RouteStopResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    loadComponent: () => import('./update/route-stop-update.component').then(m => m.RouteStopUpdateComponent),
    resolve: {
      routeStop: RouteStopResolve,
    },
    canActivate: [UserRouteAccessService],
  },
];

export default routeStopRoute;
