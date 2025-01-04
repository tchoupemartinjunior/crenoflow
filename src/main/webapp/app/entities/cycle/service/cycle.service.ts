import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { ICycle, NewCycle } from '../cycle.model';

export type PartialUpdateCycle = Partial<ICycle> & Pick<ICycle, 'id'>;

export type EntityResponseType = HttpResponse<ICycle>;
export type EntityArrayResponseType = HttpResponse<ICycle[]>;

@Injectable({ providedIn: 'root' })
export class CycleService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/cycles');

  constructor(
    protected http: HttpClient,
    protected applicationConfigService: ApplicationConfigService,
  ) {}

  create(cycle: NewCycle): Observable<EntityResponseType> {
    return this.http.post<ICycle>(this.resourceUrl, cycle, { observe: 'response' });
  }

  update(cycle: ICycle): Observable<EntityResponseType> {
    return this.http.put<ICycle>(`${this.resourceUrl}/${this.getCycleIdentifier(cycle)}`, cycle, { observe: 'response' });
  }

  partialUpdate(cycle: PartialUpdateCycle): Observable<EntityResponseType> {
    return this.http.patch<ICycle>(`${this.resourceUrl}/${this.getCycleIdentifier(cycle)}`, cycle, { observe: 'response' });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<ICycle>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<ICycle[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  getCycleIdentifier(cycle: Pick<ICycle, 'id'>): number {
    return cycle.id;
  }

  compareCycle(o1: Pick<ICycle, 'id'> | null, o2: Pick<ICycle, 'id'> | null): boolean {
    return o1 && o2 ? this.getCycleIdentifier(o1) === this.getCycleIdentifier(o2) : o1 === o2;
  }

  addCycleToCollectionIfMissing<Type extends Pick<ICycle, 'id'>>(
    cycleCollection: Type[],
    ...cyclesToCheck: (Type | null | undefined)[]
  ): Type[] {
    const cycles: Type[] = cyclesToCheck.filter(isPresent);
    if (cycles.length > 0) {
      const cycleCollectionIdentifiers = cycleCollection.map(cycleItem => this.getCycleIdentifier(cycleItem)!);
      const cyclesToAdd = cycles.filter(cycleItem => {
        const cycleIdentifier = this.getCycleIdentifier(cycleItem);
        if (cycleCollectionIdentifiers.includes(cycleIdentifier)) {
          return false;
        }
        cycleCollectionIdentifiers.push(cycleIdentifier);
        return true;
      });
      return [...cyclesToAdd, ...cycleCollection];
    }
    return cycleCollection;
  }
}
