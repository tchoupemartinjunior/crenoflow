import { Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { ASC } from 'app/config/navigation.constants';
import { ProfessionComponent } from './list/profession.component';
import { ProfessionDetailComponent } from './detail/profession-detail.component';
import { ProfessionUpdateComponent } from './update/profession-update.component';
import ProfessionResolve from './route/profession-routing-resolve.service';

const professionRoute: Routes = [
  {
    path: '',
    component: ProfessionComponent,
    data: {
      defaultSort: 'id,' + ASC,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: ProfessionDetailComponent,
    resolve: {
      profession: ProfessionResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: ProfessionUpdateComponent,
    resolve: {
      profession: ProfessionResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: ProfessionUpdateComponent,
    resolve: {
      profession: ProfessionResolve,
    },
    canActivate: [UserRouteAccessService],
  },
];

export default professionRoute;
