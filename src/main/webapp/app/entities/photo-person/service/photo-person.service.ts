import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IPhotoPerson, NewPhotoPerson } from '../photo-person.model';

export type PartialUpdatePhotoPerson = Partial<IPhotoPerson> & Pick<IPhotoPerson, 'id'>;

export type EntityResponseType = HttpResponse<IPhotoPerson>;
export type EntityArrayResponseType = HttpResponse<IPhotoPerson[]>;

@Injectable({ providedIn: 'root' })
export class PhotoPersonService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/photo-people');

  constructor(
    protected http: HttpClient,
    protected applicationConfigService: ApplicationConfigService,
  ) {}

  create(photoPerson: NewPhotoPerson): Observable<EntityResponseType> {
    return this.http.post<IPhotoPerson>(this.resourceUrl, photoPerson, { observe: 'response' });
  }

  update(photoPerson: IPhotoPerson): Observable<EntityResponseType> {
    return this.http.put<IPhotoPerson>(`${this.resourceUrl}/${this.getPhotoPersonIdentifier(photoPerson)}`, photoPerson, {
      observe: 'response',
    });
  }

  partialUpdate(photoPerson: PartialUpdatePhotoPerson): Observable<EntityResponseType> {
    return this.http.patch<IPhotoPerson>(`${this.resourceUrl}/${this.getPhotoPersonIdentifier(photoPerson)}`, photoPerson, {
      observe: 'response',
    });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IPhotoPerson>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IPhotoPerson[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  getPhotoPersonIdentifier(photoPerson: Pick<IPhotoPerson, 'id'>): number {
    return photoPerson.id;
  }

  comparePhotoPerson(o1: Pick<IPhotoPerson, 'id'> | null, o2: Pick<IPhotoPerson, 'id'> | null): boolean {
    return o1 && o2 ? this.getPhotoPersonIdentifier(o1) === this.getPhotoPersonIdentifier(o2) : o1 === o2;
  }

  addPhotoPersonToCollectionIfMissing<Type extends Pick<IPhotoPerson, 'id'>>(
    photoPersonCollection: Type[],
    ...photoPeopleToCheck: (Type | null | undefined)[]
  ): Type[] {
    const photoPeople: Type[] = photoPeopleToCheck.filter(isPresent);
    if (photoPeople.length > 0) {
      const photoPersonCollectionIdentifiers = photoPersonCollection.map(
        photoPersonItem => this.getPhotoPersonIdentifier(photoPersonItem)!,
      );
      const photoPeopleToAdd = photoPeople.filter(photoPersonItem => {
        const photoPersonIdentifier = this.getPhotoPersonIdentifier(photoPersonItem);
        if (photoPersonCollectionIdentifiers.includes(photoPersonIdentifier)) {
          return false;
        }
        photoPersonCollectionIdentifiers.push(photoPersonIdentifier);
        return true;
      });
      return [...photoPeopleToAdd, ...photoPersonCollection];
    }
    return photoPersonCollection;
  }
}
