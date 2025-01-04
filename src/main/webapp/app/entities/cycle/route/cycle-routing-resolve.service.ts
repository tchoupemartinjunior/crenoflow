import { inject } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { of, EMPTY, Observable } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { ICycle } from '../cycle.model';
import { CycleService } from '../service/cycle.service';

export const cycleResolve = (route: ActivatedRouteSnapshot): Observable<null | ICycle> => {
  const id = route.params['id'];
  if (id) {
    return inject(CycleService)
      .find(id)
      .pipe(
        mergeMap((cycle: HttpResponse<ICycle>) => {
          if (cycle.body) {
            return of(cycle.body);
          } else {
            inject(Router).navigate(['404']);
            return EMPTY;
          }
        }),
      );
  }
  return of(null);
};

export default cycleResolve;
