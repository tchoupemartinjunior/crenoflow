import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IEducationLevel, NewEducationLevel } from '../education-level.model';

export type PartialUpdateEducationLevel = Partial<IEducationLevel> & Pick<IEducationLevel, 'id'>;

export type EntityResponseType = HttpResponse<IEducationLevel>;
export type EntityArrayResponseType = HttpResponse<IEducationLevel[]>;

@Injectable({ providedIn: 'root' })
export class EducationLevelService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/education-levels');

  constructor(
    protected http: HttpClient,
    protected applicationConfigService: ApplicationConfigService,
  ) {}

  create(educationLevel: NewEducationLevel): Observable<EntityResponseType> {
    return this.http.post<IEducationLevel>(this.resourceUrl, educationLevel, { observe: 'response' });
  }

  update(educationLevel: IEducationLevel): Observable<EntityResponseType> {
    return this.http.put<IEducationLevel>(`${this.resourceUrl}/${this.getEducationLevelIdentifier(educationLevel)}`, educationLevel, {
      observe: 'response',
    });
  }

  partialUpdate(educationLevel: PartialUpdateEducationLevel): Observable<EntityResponseType> {
    return this.http.patch<IEducationLevel>(`${this.resourceUrl}/${this.getEducationLevelIdentifier(educationLevel)}`, educationLevel, {
      observe: 'response',
    });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IEducationLevel>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IEducationLevel[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  getEducationLevelIdentifier(educationLevel: Pick<IEducationLevel, 'id'>): number {
    return educationLevel.id;
  }

  compareEducationLevel(o1: Pick<IEducationLevel, 'id'> | null, o2: Pick<IEducationLevel, 'id'> | null): boolean {
    return o1 && o2 ? this.getEducationLevelIdentifier(o1) === this.getEducationLevelIdentifier(o2) : o1 === o2;
  }

  addEducationLevelToCollectionIfMissing<Type extends Pick<IEducationLevel, 'id'>>(
    educationLevelCollection: Type[],
    ...educationLevelsToCheck: (Type | null | undefined)[]
  ): Type[] {
    const educationLevels: Type[] = educationLevelsToCheck.filter(isPresent);
    if (educationLevels.length > 0) {
      const educationLevelCollectionIdentifiers = educationLevelCollection.map(
        educationLevelItem => this.getEducationLevelIdentifier(educationLevelItem)!,
      );
      const educationLevelsToAdd = educationLevels.filter(educationLevelItem => {
        const educationLevelIdentifier = this.getEducationLevelIdentifier(educationLevelItem);
        if (educationLevelCollectionIdentifiers.includes(educationLevelIdentifier)) {
          return false;
        }
        educationLevelCollectionIdentifiers.push(educationLevelIdentifier);
        return true;
      });
      return [...educationLevelsToAdd, ...educationLevelCollection];
    }
    return educationLevelCollection;
  }
}
