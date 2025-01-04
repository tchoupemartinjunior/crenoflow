import { Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { ASC } from 'app/config/navigation.constants';
import { AvailabilityComponent } from './list/availability.component';
import { AvailabilityDetailComponent } from './detail/availability-detail.component';
import { AvailabilityUpdateComponent } from './update/availability-update.component';
import AvailabilityResolve from './route/availability-routing-resolve.service';

const availabilityRoute: Routes = [
  {
    path: '',
    component: AvailabilityComponent,
    data: {
      defaultSort: 'id,' + ASC,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: AvailabilityDetailComponent,
    resolve: {
      availability: AvailabilityResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: AvailabilityUpdateComponent,
    resolve: {
      availability: AvailabilityResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: AvailabilityUpdateComponent,
    resolve: {
      availability: AvailabilityResolve,
    },
    canActivate: [UserRouteAccessService],
  },
];

export default availabilityRoute;
