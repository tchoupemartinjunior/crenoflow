import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { map } from 'rxjs/operators';

import dayjs from 'dayjs/esm';

import { isPresent } from 'app/core/util/operators';
import { DATE_FORMAT } from 'app/config/input.constants';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IAvailability, NewAvailability } from '../availability.model';

export type PartialUpdateAvailability = Partial<IAvailability> & Pick<IAvailability, 'id'>;

type RestOf<T extends IAvailability | NewAvailability> = Omit<T, 'date' | 'creationDate'> & {
  date?: string | null;
  creationDate?: string | null;
};

export type RestAvailability = RestOf<IAvailability>;

export type NewRestAvailability = RestOf<NewAvailability>;

export type PartialUpdateRestAvailability = RestOf<PartialUpdateAvailability>;

export type EntityResponseType = HttpResponse<IAvailability>;
export type EntityArrayResponseType = HttpResponse<IAvailability[]>;

@Injectable({ providedIn: 'root' })
export class AvailabilityService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/availabilities');

  constructor(
    protected http: HttpClient,
    protected applicationConfigService: ApplicationConfigService,
  ) {}

  create(availability: NewAvailability): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(availability);
    return this.http
      .post<RestAvailability>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  update(availability: IAvailability): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(availability);
    return this.http
      .put<RestAvailability>(`${this.resourceUrl}/${this.getAvailabilityIdentifier(availability)}`, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  partialUpdate(availability: PartialUpdateAvailability): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(availability);
    return this.http
      .patch<RestAvailability>(`${this.resourceUrl}/${this.getAvailabilityIdentifier(availability)}`, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<RestAvailability>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<RestAvailability[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map(res => this.convertResponseArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  getAvailabilityIdentifier(availability: Pick<IAvailability, 'id'>): number {
    return availability.id;
  }

  compareAvailability(o1: Pick<IAvailability, 'id'> | null, o2: Pick<IAvailability, 'id'> | null): boolean {
    return o1 && o2 ? this.getAvailabilityIdentifier(o1) === this.getAvailabilityIdentifier(o2) : o1 === o2;
  }

  addAvailabilityToCollectionIfMissing<Type extends Pick<IAvailability, 'id'>>(
    availabilityCollection: Type[],
    ...availabilitiesToCheck: (Type | null | undefined)[]
  ): Type[] {
    const availabilities: Type[] = availabilitiesToCheck.filter(isPresent);
    if (availabilities.length > 0) {
      const availabilityCollectionIdentifiers = availabilityCollection.map(
        availabilityItem => this.getAvailabilityIdentifier(availabilityItem)!,
      );
      const availabilitiesToAdd = availabilities.filter(availabilityItem => {
        const availabilityIdentifier = this.getAvailabilityIdentifier(availabilityItem);
        if (availabilityCollectionIdentifiers.includes(availabilityIdentifier)) {
          return false;
        }
        availabilityCollectionIdentifiers.push(availabilityIdentifier);
        return true;
      });
      return [...availabilitiesToAdd, ...availabilityCollection];
    }
    return availabilityCollection;
  }

  protected convertDateFromClient<T extends IAvailability | NewAvailability | PartialUpdateAvailability>(availability: T): RestOf<T> {
    return {
      ...availability,
      date: availability.date?.format(DATE_FORMAT) ?? null,
      creationDate: availability.creationDate?.toJSON() ?? null,
    };
  }

  protected convertDateFromServer(restAvailability: RestAvailability): IAvailability {
    return {
      ...restAvailability,
      date: restAvailability.date ? dayjs(restAvailability.date) : undefined,
      creationDate: restAvailability.creationDate ? dayjs(restAvailability.creationDate) : undefined,
    };
  }

  protected convertResponseFromServer(res: HttpResponse<RestAvailability>): HttpResponse<IAvailability> {
    return res.clone({
      body: res.body ? this.convertDateFromServer(res.body) : null,
    });
  }

  protected convertResponseArrayFromServer(res: HttpResponse<RestAvailability[]>): HttpResponse<IAvailability[]> {
    return res.clone({
      body: res.body ? res.body.map(item => this.convertDateFromServer(item)) : null,
    });
  }
}
