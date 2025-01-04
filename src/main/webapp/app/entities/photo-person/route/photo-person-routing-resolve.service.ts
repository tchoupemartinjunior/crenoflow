import { inject } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { of, EMPTY, Observable } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IPhotoPerson } from '../photo-person.model';
import { PhotoPersonService } from '../service/photo-person.service';

export const photoPersonResolve = (route: ActivatedRouteSnapshot): Observable<null | IPhotoPerson> => {
  const id = route.params['id'];
  if (id) {
    return inject(PhotoPersonService)
      .find(id)
      .pipe(
        mergeMap((photoPerson: HttpResponse<IPhotoPerson>) => {
          if (photoPerson.body) {
            return of(photoPerson.body);
          } else {
            inject(Router).navigate(['404']);
            return EMPTY;
          }
        }),
      );
  }
  return of(null);
};

export default photoPersonResolve;
