import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { ISubjectCycle, NewSubjectCycle } from '../subject-cycle.model';

export type PartialUpdateSubjectCycle = Partial<ISubjectCycle> & Pick<ISubjectCycle, 'id'>;

export type EntityResponseType = HttpResponse<ISubjectCycle>;
export type EntityArrayResponseType = HttpResponse<ISubjectCycle[]>;

@Injectable({ providedIn: 'root' })
export class SubjectCycleService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/subject-cycles');

  constructor(
    protected http: HttpClient,
    protected applicationConfigService: ApplicationConfigService,
  ) {}

  create(subjectCycle: NewSubjectCycle): Observable<EntityResponseType> {
    return this.http.post<ISubjectCycle>(this.resourceUrl, subjectCycle, { observe: 'response' });
  }

  update(subjectCycle: ISubjectCycle): Observable<EntityResponseType> {
    return this.http.put<ISubjectCycle>(`${this.resourceUrl}/${this.getSubjectCycleIdentifier(subjectCycle)}`, subjectCycle, {
      observe: 'response',
    });
  }

  partialUpdate(subjectCycle: PartialUpdateSubjectCycle): Observable<EntityResponseType> {
    return this.http.patch<ISubjectCycle>(`${this.resourceUrl}/${this.getSubjectCycleIdentifier(subjectCycle)}`, subjectCycle, {
      observe: 'response',
    });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<ISubjectCycle>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<ISubjectCycle[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  getSubjectCycleIdentifier(subjectCycle: Pick<ISubjectCycle, 'id'>): number {
    return subjectCycle.id;
  }

  compareSubjectCycle(o1: Pick<ISubjectCycle, 'id'> | null, o2: Pick<ISubjectCycle, 'id'> | null): boolean {
    return o1 && o2 ? this.getSubjectCycleIdentifier(o1) === this.getSubjectCycleIdentifier(o2) : o1 === o2;
  }

  addSubjectCycleToCollectionIfMissing<Type extends Pick<ISubjectCycle, 'id'>>(
    subjectCycleCollection: Type[],
    ...subjectCyclesToCheck: (Type | null | undefined)[]
  ): Type[] {
    const subjectCycles: Type[] = subjectCyclesToCheck.filter(isPresent);
    if (subjectCycles.length > 0) {
      const subjectCycleCollectionIdentifiers = subjectCycleCollection.map(
        subjectCycleItem => this.getSubjectCycleIdentifier(subjectCycleItem)!,
      );
      const subjectCyclesToAdd = subjectCycles.filter(subjectCycleItem => {
        const subjectCycleIdentifier = this.getSubjectCycleIdentifier(subjectCycleItem);
        if (subjectCycleCollectionIdentifiers.includes(subjectCycleIdentifier)) {
          return false;
        }
        subjectCycleCollectionIdentifiers.push(subjectCycleIdentifier);
        return true;
      });
      return [...subjectCyclesToAdd, ...subjectCycleCollection];
    }
    return subjectCycleCollection;
  }
}
