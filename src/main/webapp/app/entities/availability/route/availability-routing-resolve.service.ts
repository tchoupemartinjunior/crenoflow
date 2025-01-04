import { inject } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { of, EMPTY, Observable } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IAvailability } from '../availability.model';
import { AvailabilityService } from '../service/availability.service';

export const availabilityResolve = (route: ActivatedRouteSnapshot): Observable<null | IAvailability> => {
  const id = route.params['id'];
  if (id) {
    return inject(AvailabilityService)
      .find(id)
      .pipe(
        mergeMap((availability: HttpResponse<IAvailability>) => {
          if (availability.body) {
            return of(availability.body);
          } else {
            inject(Router).navigate(['404']);
            return EMPTY;
          }
        }),
      );
  }
  return of(null);
};

export default availabilityResolve;
