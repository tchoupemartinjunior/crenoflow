import { Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { ASC } from 'app/config/navigation.constants';
import { CycleComponent } from './list/cycle.component';
import { CycleDetailComponent } from './detail/cycle-detail.component';
import { CycleUpdateComponent } from './update/cycle-update.component';
import CycleResolve from './route/cycle-routing-resolve.service';

const cycleRoute: Routes = [
  {
    path: '',
    component: CycleComponent,
    data: {
      defaultSort: 'id,' + ASC,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: CycleDetailComponent,
    resolve: {
      cycle: CycleResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: CycleUpdateComponent,
    resolve: {
      cycle: CycleResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: CycleUpdateComponent,
    resolve: {
      cycle: CycleResolve,
    },
    canActivate: [UserRouteAccessService],
  },
];

export default cycleRoute;
