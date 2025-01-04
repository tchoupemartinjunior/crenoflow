import { inject } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { of, EMPTY, Observable } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { ISchoolLevel } from '../school-level.model';
import { SchoolLevelService } from '../service/school-level.service';

export const schoolLevelResolve = (route: ActivatedRouteSnapshot): Observable<null | ISchoolLevel> => {
  const id = route.params['id'];
  if (id) {
    return inject(SchoolLevelService)
      .find(id)
      .pipe(
        mergeMap((schoolLevel: HttpResponse<ISchoolLevel>) => {
          if (schoolLevel.body) {
            return of(schoolLevel.body);
          } else {
            inject(Router).navigate(['404']);
            return EMPTY;
          }
        }),
      );
  }
  return of(null);
};

export default schoolLevelResolve;
