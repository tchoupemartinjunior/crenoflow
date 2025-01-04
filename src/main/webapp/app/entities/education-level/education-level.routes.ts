import { Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { ASC } from 'app/config/navigation.constants';
import { EducationLevelComponent } from './list/education-level.component';
import { EducationLevelDetailComponent } from './detail/education-level-detail.component';
import { EducationLevelUpdateComponent } from './update/education-level-update.component';
import EducationLevelResolve from './route/education-level-routing-resolve.service';

const educationLevelRoute: Routes = [
  {
    path: '',
    component: EducationLevelComponent,
    data: {
      defaultSort: 'id,' + ASC,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: EducationLevelDetailComponent,
    resolve: {
      educationLevel: EducationLevelResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: EducationLevelUpdateComponent,
    resolve: {
      educationLevel: EducationLevelResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: EducationLevelUpdateComponent,
    resolve: {
      educationLevel: EducationLevelResolve,
    },
    canActivate: [UserRouteAccessService],
  },
];

export default educationLevelRoute;
