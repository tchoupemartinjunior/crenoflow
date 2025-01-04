import { inject } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { of, EMPTY, Observable } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IEducationLevel } from '../education-level.model';
import { EducationLevelService } from '../service/education-level.service';

export const educationLevelResolve = (route: ActivatedRouteSnapshot): Observable<null | IEducationLevel> => {
  const id = route.params['id'];
  if (id) {
    return inject(EducationLevelService)
      .find(id)
      .pipe(
        mergeMap((educationLevel: HttpResponse<IEducationLevel>) => {
          if (educationLevel.body) {
            return of(educationLevel.body);
          } else {
            inject(Router).navigate(['404']);
            return EMPTY;
          }
        }),
      );
  }
  return of(null);
};

export default educationLevelResolve;
