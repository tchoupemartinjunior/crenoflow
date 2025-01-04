import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { ICourseLocation, NewCourseLocation } from '../course-location.model';

export type PartialUpdateCourseLocation = Partial<ICourseLocation> & Pick<ICourseLocation, 'id'>;

export type EntityResponseType = HttpResponse<ICourseLocation>;
export type EntityArrayResponseType = HttpResponse<ICourseLocation[]>;

@Injectable({ providedIn: 'root' })
export class CourseLocationService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/course-locations');

  constructor(
    protected http: HttpClient,
    protected applicationConfigService: ApplicationConfigService,
  ) {}

  create(courseLocation: NewCourseLocation): Observable<EntityResponseType> {
    return this.http.post<ICourseLocation>(this.resourceUrl, courseLocation, { observe: 'response' });
  }

  update(courseLocation: ICourseLocation): Observable<EntityResponseType> {
    return this.http.put<ICourseLocation>(`${this.resourceUrl}/${this.getCourseLocationIdentifier(courseLocation)}`, courseLocation, {
      observe: 'response',
    });
  }

  partialUpdate(courseLocation: PartialUpdateCourseLocation): Observable<EntityResponseType> {
    return this.http.patch<ICourseLocation>(`${this.resourceUrl}/${this.getCourseLocationIdentifier(courseLocation)}`, courseLocation, {
      observe: 'response',
    });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<ICourseLocation>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<ICourseLocation[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  getCourseLocationIdentifier(courseLocation: Pick<ICourseLocation, 'id'>): number {
    return courseLocation.id;
  }

  compareCourseLocation(o1: Pick<ICourseLocation, 'id'> | null, o2: Pick<ICourseLocation, 'id'> | null): boolean {
    return o1 && o2 ? this.getCourseLocationIdentifier(o1) === this.getCourseLocationIdentifier(o2) : o1 === o2;
  }

  addCourseLocationToCollectionIfMissing<Type extends Pick<ICourseLocation, 'id'>>(
    courseLocationCollection: Type[],
    ...courseLocationsToCheck: (Type | null | undefined)[]
  ): Type[] {
    const courseLocations: Type[] = courseLocationsToCheck.filter(isPresent);
    if (courseLocations.length > 0) {
      const courseLocationCollectionIdentifiers = courseLocationCollection.map(
        courseLocationItem => this.getCourseLocationIdentifier(courseLocationItem)!,
      );
      const courseLocationsToAdd = courseLocations.filter(courseLocationItem => {
        const courseLocationIdentifier = this.getCourseLocationIdentifier(courseLocationItem);
        if (courseLocationCollectionIdentifiers.includes(courseLocationIdentifier)) {
          return false;
        }
        courseLocationCollectionIdentifiers.push(courseLocationIdentifier);
        return true;
      });
      return [...courseLocationsToAdd, ...courseLocationCollection];
    }
    return courseLocationCollection;
  }
}
