import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';
import { RegistrationComponent } from './registration/registration.component';
import { errorRoute } from '../layouts/error/error.route';
import { DEBUG_INFO_ENABLED } from '../app.constants';
import { Authority } from '../config/authority.constants';
import { UserRouteAccessService } from '../core/auth/user-route-access.service';
import { TeachersPlanningComponent } from './teachers-planning/teachers-planning.component';
import { ManagementComponent } from './management/management.component';

@NgModule({
  imports: [
    RouterModule.forChild([
      {
        path: 'registration',
        component: RegistrationComponent,
        title: 'teacher.registration',
        data: {
          authorities: [Authority.ADMIN, Authority.USER],
        },
        canActivate: [UserRouteAccessService],
      },
      {
        path: 'manage',
        component: ManagementComponent,
        title: 'teacher.management',
        children: [
          {
            path: 'planning',
            component: TeachersPlanningComponent,
            title: 'teacher.planning',
          },
        ],
        data: {
          authorities: [Authority.ADMIN, Authority.USER],
        },
        canActivate: [UserRouteAccessService],
      },
      ...errorRoute,
    ]),
  ],
  exports: [RouterModule],
})
export class TeacherRoutingModule {}
