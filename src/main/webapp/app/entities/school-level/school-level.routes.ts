import { Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { ASC } from 'app/config/navigation.constants';
import { SchoolLevelComponent } from './list/school-level.component';
import { SchoolLevelDetailComponent } from './detail/school-level-detail.component';
import { SchoolLevelUpdateComponent } from './update/school-level-update.component';
import SchoolLevelResolve from './route/school-level-routing-resolve.service';

const schoolLevelRoute: Routes = [
  {
    path: '',
    component: SchoolLevelComponent,
    data: {
      defaultSort: 'id,' + ASC,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: SchoolLevelDetailComponent,
    resolve: {
      schoolLevel: SchoolLevelResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: SchoolLevelUpdateComponent,
    resolve: {
      schoolLevel: SchoolLevelResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: SchoolLevelUpdateComponent,
    resolve: {
      schoolLevel: SchoolLevelResolve,
    },
    canActivate: [UserRouteAccessService],
  },
];

export default schoolLevelRoute;
