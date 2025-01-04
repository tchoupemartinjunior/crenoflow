import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { ISpeciality, NewSpeciality } from '../speciality.model';

export type PartialUpdateSpeciality = Partial<ISpeciality> & Pick<ISpeciality, 'id'>;

export type EntityResponseType = HttpResponse<ISpeciality>;
export type EntityArrayResponseType = HttpResponse<ISpeciality[]>;

@Injectable({ providedIn: 'root' })
export class SpecialityService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/specialities');

  constructor(
    protected http: HttpClient,
    protected applicationConfigService: ApplicationConfigService,
  ) {}

  create(speciality: NewSpeciality): Observable<EntityResponseType> {
    return this.http.post<ISpeciality>(this.resourceUrl, speciality, { observe: 'response' });
  }

  update(speciality: ISpeciality): Observable<EntityResponseType> {
    return this.http.put<ISpeciality>(`${this.resourceUrl}/${this.getSpecialityIdentifier(speciality)}`, speciality, {
      observe: 'response',
    });
  }

  partialUpdate(speciality: PartialUpdateSpeciality): Observable<EntityResponseType> {
    return this.http.patch<ISpeciality>(`${this.resourceUrl}/${this.getSpecialityIdentifier(speciality)}`, speciality, {
      observe: 'response',
    });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<ISpeciality>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<ISpeciality[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  getSpecialityIdentifier(speciality: Pick<ISpeciality, 'id'>): number {
    return speciality.id;
  }

  compareSpeciality(o1: Pick<ISpeciality, 'id'> | null, o2: Pick<ISpeciality, 'id'> | null): boolean {
    return o1 && o2 ? this.getSpecialityIdentifier(o1) === this.getSpecialityIdentifier(o2) : o1 === o2;
  }

  addSpecialityToCollectionIfMissing<Type extends Pick<ISpeciality, 'id'>>(
    specialityCollection: Type[],
    ...specialitiesToCheck: (Type | null | undefined)[]
  ): Type[] {
    const specialities: Type[] = specialitiesToCheck.filter(isPresent);
    if (specialities.length > 0) {
      const specialityCollectionIdentifiers = specialityCollection.map(specialityItem => this.getSpecialityIdentifier(specialityItem)!);
      const specialitiesToAdd = specialities.filter(specialityItem => {
        const specialityIdentifier = this.getSpecialityIdentifier(specialityItem);
        if (specialityCollectionIdentifiers.includes(specialityIdentifier)) {
          return false;
        }
        specialityCollectionIdentifiers.push(specialityIdentifier);
        return true;
      });
      return [...specialitiesToAdd, ...specialityCollection];
    }
    return specialityCollection;
  }
}
