import { inject } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { of, EMPTY, Observable } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IProfession } from '../profession.model';
import { ProfessionService } from '../service/profession.service';

export const professionResolve = (route: ActivatedRouteSnapshot): Observable<null | IProfession> => {
  const id = route.params['id'];
  if (id) {
    return inject(ProfessionService)
      .find(id)
      .pipe(
        mergeMap((profession: HttpResponse<IProfession>) => {
          if (profession.body) {
            return of(profession.body);
          } else {
            inject(Router).navigate(['404']);
            return EMPTY;
          }
        }),
      );
  }
  return of(null);
};

export default professionResolve;
