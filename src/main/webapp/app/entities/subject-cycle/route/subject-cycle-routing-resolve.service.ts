import { inject } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { of, EMPTY, Observable } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { ISubjectCycle } from '../subject-cycle.model';
import { SubjectCycleService } from '../service/subject-cycle.service';

export const subjectCycleResolve = (route: ActivatedRouteSnapshot): Observable<null | ISubjectCycle> => {
  const id = route.params['id'];
  if (id) {
    return inject(SubjectCycleService)
      .find(id)
      .pipe(
        mergeMap((subjectCycle: HttpResponse<ISubjectCycle>) => {
          if (subjectCycle.body) {
            return of(subjectCycle.body);
          } else {
            inject(Router).navigate(['404']);
            return EMPTY;
          }
        }),
      );
  }
  return of(null);
};

export default subjectCycleResolve;
