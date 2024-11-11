import { Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { ASC } from 'app/config/navigation.constants';
import RepairResolve from './route/repair-routing-resolve.service';

const repairRoute: Routes = [
  {
    path: '',
    loadComponent: () => import('./list/repair.component').then(m => m.RepairComponent),
    data: {
      defaultSort: `id,${ASC}`,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    loadComponent: () => import('./detail/repair-detail.component').then(m => m.RepairDetailComponent),
    resolve: {
      repair: RepairResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    loadComponent: () => import('./update/repair-update.component').then(m => m.RepairUpdateComponent),
    resolve: {
      repair: RepairResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    loadComponent: () => import('./update/repair-update.component').then(m => m.RepairUpdateComponent),
    resolve: {
      repair: RepairResolve,
    },
    canActivate: [UserRouteAccessService],
  },
];

export default repairRoute;
