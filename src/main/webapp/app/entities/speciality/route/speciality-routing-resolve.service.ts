import { inject } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { of, EMPTY, Observable } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { ISpeciality } from '../speciality.model';
import { SpecialityService } from '../service/speciality.service';

export const specialityResolve = (route: ActivatedRouteSnapshot): Observable<null | ISpeciality> => {
  const id = route.params['id'];
  if (id) {
    return inject(SpecialityService)
      .find(id)
      .pipe(
        mergeMap((speciality: HttpResponse<ISpeciality>) => {
          if (speciality.body) {
            return of(speciality.body);
          } else {
            inject(Router).navigate(['404']);
            return EMPTY;
          }
        }),
      );
  }
  return of(null);
};

export default specialityResolve;
