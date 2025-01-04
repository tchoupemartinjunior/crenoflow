import { Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { ASC } from 'app/config/navigation.constants';
import { SubjectCycleComponent } from './list/subject-cycle.component';
import { SubjectCycleDetailComponent } from './detail/subject-cycle-detail.component';
import { SubjectCycleUpdateComponent } from './update/subject-cycle-update.component';
import SubjectCycleResolve from './route/subject-cycle-routing-resolve.service';

const subjectCycleRoute: Routes = [
  {
    path: '',
    component: SubjectCycleComponent,
    data: {
      defaultSort: 'id,' + ASC,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: SubjectCycleDetailComponent,
    resolve: {
      subjectCycle: SubjectCycleResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: SubjectCycleUpdateComponent,
    resolve: {
      subjectCycle: SubjectCycleResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: SubjectCycleUpdateComponent,
    resolve: {
      subjectCycle: SubjectCycleResolve,
    },
    canActivate: [UserRouteAccessService],
  },
];

export default subjectCycleRoute;
