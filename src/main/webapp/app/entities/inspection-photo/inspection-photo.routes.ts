import { Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { ASC } from 'app/config/navigation.constants';
import InspectionPhotoResolve from './route/inspection-photo-routing-resolve.service';

const inspectionPhotoRoute: Routes = [
  {
    path: '',
    loadComponent: () => import('./list/inspection-photo.component').then(m => m.InspectionPhotoComponent),
    data: {
      defaultSort: `id,${ASC}`,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    loadComponent: () => import('./detail/inspection-photo-detail.component').then(m => m.InspectionPhotoDetailComponent),
    resolve: {
      inspectionPhoto: InspectionPhotoResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    loadComponent: () => import('./update/inspection-photo-update.component').then(m => m.InspectionPhotoUpdateComponent),
    resolve: {
      inspectionPhoto: InspectionPhotoResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    loadComponent: () => import('./update/inspection-photo-update.component').then(m => m.InspectionPhotoUpdateComponent),
    resolve: {
      inspectionPhoto: InspectionPhotoResolve,
    },
    canActivate: [UserRouteAccessService],
  },
];

export default inspectionPhotoRoute;
