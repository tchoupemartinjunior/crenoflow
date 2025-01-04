import { inject } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { of, EMPTY, Observable } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { ICourseLocation } from '../course-location.model';
import { CourseLocationService } from '../service/course-location.service';

export const courseLocationResolve = (route: ActivatedRouteSnapshot): Observable<null | ICourseLocation> => {
  const id = route.params['id'];
  if (id) {
    return inject(CourseLocationService)
      .find(id)
      .pipe(
        mergeMap((courseLocation: HttpResponse<ICourseLocation>) => {
          if (courseLocation.body) {
            return of(courseLocation.body);
          } else {
            inject(Router).navigate(['404']);
            return EMPTY;
          }
        }),
      );
  }
  return of(null);
};

export default courseLocationResolve;
