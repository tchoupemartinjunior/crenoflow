import { Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { ASC } from 'app/config/navigation.constants';
import { PhotoPersonComponent } from './list/photo-person.component';
import { PhotoPersonDetailComponent } from './detail/photo-person-detail.component';
import { PhotoPersonUpdateComponent } from './update/photo-person-update.component';
import PhotoPersonResolve from './route/photo-person-routing-resolve.service';

const photoPersonRoute: Routes = [
  {
    path: '',
    component: PhotoPersonComponent,
    data: {
      defaultSort: 'id,' + ASC,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: PhotoPersonDetailComponent,
    resolve: {
      photoPerson: PhotoPersonResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: PhotoPersonUpdateComponent,
    resolve: {
      photoPerson: PhotoPersonResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: PhotoPersonUpdateComponent,
    resolve: {
      photoPerson: PhotoPersonResolve,
    },
    canActivate: [UserRouteAccessService],
  },
];

export default photoPersonRoute;
