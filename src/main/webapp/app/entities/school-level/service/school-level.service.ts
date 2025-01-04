import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { ISchoolLevel, NewSchoolLevel } from '../school-level.model';

export type PartialUpdateSchoolLevel = Partial<ISchoolLevel> & Pick<ISchoolLevel, 'id'>;

export type EntityResponseType = HttpResponse<ISchoolLevel>;
export type EntityArrayResponseType = HttpResponse<ISchoolLevel[]>;

@Injectable({ providedIn: 'root' })
export class SchoolLevelService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/school-levels');

  constructor(
    protected http: HttpClient,
    protected applicationConfigService: ApplicationConfigService,
  ) {}

  create(schoolLevel: NewSchoolLevel): Observable<EntityResponseType> {
    return this.http.post<ISchoolLevel>(this.resourceUrl, schoolLevel, { observe: 'response' });
  }

  update(schoolLevel: ISchoolLevel): Observable<EntityResponseType> {
    return this.http.put<ISchoolLevel>(`${this.resourceUrl}/${this.getSchoolLevelIdentifier(schoolLevel)}`, schoolLevel, {
      observe: 'response',
    });
  }

  partialUpdate(schoolLevel: PartialUpdateSchoolLevel): Observable<EntityResponseType> {
    return this.http.patch<ISchoolLevel>(`${this.resourceUrl}/${this.getSchoolLevelIdentifier(schoolLevel)}`, schoolLevel, {
      observe: 'response',
    });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<ISchoolLevel>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<ISchoolLevel[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  getSchoolLevelIdentifier(schoolLevel: Pick<ISchoolLevel, 'id'>): number {
    return schoolLevel.id;
  }

  compareSchoolLevel(o1: Pick<ISchoolLevel, 'id'> | null, o2: Pick<ISchoolLevel, 'id'> | null): boolean {
    return o1 && o2 ? this.getSchoolLevelIdentifier(o1) === this.getSchoolLevelIdentifier(o2) : o1 === o2;
  }

  addSchoolLevelToCollectionIfMissing<Type extends Pick<ISchoolLevel, 'id'>>(
    schoolLevelCollection: Type[],
    ...schoolLevelsToCheck: (Type | null | undefined)[]
  ): Type[] {
    const schoolLevels: Type[] = schoolLevelsToCheck.filter(isPresent);
    if (schoolLevels.length > 0) {
      const schoolLevelCollectionIdentifiers = schoolLevelCollection.map(
        schoolLevelItem => this.getSchoolLevelIdentifier(schoolLevelItem)!,
      );
      const schoolLevelsToAdd = schoolLevels.filter(schoolLevelItem => {
        const schoolLevelIdentifier = this.getSchoolLevelIdentifier(schoolLevelItem);
        if (schoolLevelCollectionIdentifiers.includes(schoolLevelIdentifier)) {
          return false;
        }
        schoolLevelCollectionIdentifiers.push(schoolLevelIdentifier);
        return true;
      });
      return [...schoolLevelsToAdd, ...schoolLevelCollection];
    }
    return schoolLevelCollection;
  }
}
