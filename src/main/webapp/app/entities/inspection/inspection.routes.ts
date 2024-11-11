import { Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { ASC } from 'app/config/navigation.constants';
import InspectionResolve from './route/inspection-routing-resolve.service';

const inspectionRoute: Routes = [
  {
    path: '',
    loadComponent: () => import('./list/inspection.component').then(m => m.InspectionComponent),
    data: {
      defaultSort: `id,${ASC}`,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    loadComponent: () => import('./detail/inspection-detail.component').then(m => m.InspectionDetailComponent),
    resolve: {
      inspection: InspectionResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    loadComponent: () => import('./update/inspection-update.component').then(m => m.InspectionUpdateComponent),
    resolve: {
      inspection: InspectionResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    loadComponent: () => import('./update/inspection-update.component').then(m => m.InspectionUpdateComponent),
    resolve: {
      inspection: InspectionResolve,
    },
    canActivate: [UserRouteAccessService],
  },
];

export default inspectionRoute;
