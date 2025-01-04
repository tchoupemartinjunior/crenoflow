import { Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { ASC } from 'app/config/navigation.constants';
import { SpecialityComponent } from './list/speciality.component';
import { SpecialityDetailComponent } from './detail/speciality-detail.component';
import { SpecialityUpdateComponent } from './update/speciality-update.component';
import SpecialityResolve from './route/speciality-routing-resolve.service';

const specialityRoute: Routes = [
  {
    path: '',
    component: SpecialityComponent,
    data: {
      defaultSort: 'id,' + ASC,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: SpecialityDetailComponent,
    resolve: {
      speciality: SpecialityResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: SpecialityUpdateComponent,
    resolve: {
      speciality: SpecialityResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: SpecialityUpdateComponent,
    resolve: {
      speciality: SpecialityResolve,
    },
    canActivate: [UserRouteAccessService],
  },
];

export default specialityRoute;
