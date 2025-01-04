import { Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { ASC } from 'app/config/navigation.constants';
import { CourseLocationComponent } from './list/course-location.component';
import { CourseLocationDetailComponent } from './detail/course-location-detail.component';
import { CourseLocationUpdateComponent } from './update/course-location-update.component';
import CourseLocationResolve from './route/course-location-routing-resolve.service';

const courseLocationRoute: Routes = [
  {
    path: '',
    component: CourseLocationComponent,
    data: {
      defaultSort: 'id,' + ASC,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: CourseLocationDetailComponent,
    resolve: {
      courseLocation: CourseLocationResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: CourseLocationUpdateComponent,
    resolve: {
      courseLocation: CourseLocationResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: CourseLocationUpdateComponent,
    resolve: {
      courseLocation: CourseLocationResolve,
    },
    canActivate: [UserRouteAccessService],
  },
];

export default courseLocationRoute;
