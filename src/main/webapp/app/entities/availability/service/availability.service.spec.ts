import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { DATE_FORMAT } from 'app/config/input.constants';
import { IAvailability } from '../availability.model';
import { sampleWithRequiredData, sampleWithNewData, sampleWithPartialData, sampleWithFullData } from '../availability.test-samples';

import { AvailabilityService, RestAvailability } from './availability.service';

const requireRestSample: RestAvailability = {
  ...sampleWithRequiredData,
  date: sampleWithRequiredData.date?.format(DATE_FORMAT),
  creationDate: sampleWithRequiredData.creationDate?.toJSON(),
};

describe('Availability Service', () => {
  let service: AvailabilityService;
  let httpMock: HttpTestingController;
  let expectedResult: IAvailability | IAvailability[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(AvailabilityService);
    httpMock = TestBed.inject(HttpTestingController);
  });

  describe('Service methods', () => {
    it('should find an element', () => {
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.find(123).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should create a Availability', () => {
      const availability = { ...sampleWithNewData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.create(availability).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a Availability', () => {
      const availability = { ...sampleWithRequiredData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.update(availability).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a Availability', () => {
      const patchObject = { ...sampleWithPartialData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of Availability', () => {
      const returnedFromService = { ...requireRestSample };

      const expected = { ...sampleWithRequiredData };

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toMatchObject([expected]);
    });

    it('should delete a Availability', () => {
      const expected = true;

      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult).toBe(expected);
    });

    describe('addAvailabilityToCollectionIfMissing', () => {
      it('should add a Availability to an empty array', () => {
        const availability: IAvailability = sampleWithRequiredData;
        expectedResult = service.addAvailabilityToCollectionIfMissing([], availability);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(availability);
      });

      it('should not add a Availability to an array that contains it', () => {
        const availability: IAvailability = sampleWithRequiredData;
        const availabilityCollection: IAvailability[] = [
          {
            ...availability,
          },
          sampleWithPartialData,
        ];
        expectedResult = service.addAvailabilityToCollectionIfMissing(availabilityCollection, availability);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a Availability to an array that doesn't contain it", () => {
        const availability: IAvailability = sampleWithRequiredData;
        const availabilityCollection: IAvailability[] = [sampleWithPartialData];
        expectedResult = service.addAvailabilityToCollectionIfMissing(availabilityCollection, availability);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(availability);
      });

      it('should add only unique Availability to an array', () => {
        const availabilityArray: IAvailability[] = [sampleWithRequiredData, sampleWithPartialData, sampleWithFullData];
        const availabilityCollection: IAvailability[] = [sampleWithRequiredData];
        expectedResult = service.addAvailabilityToCollectionIfMissing(availabilityCollection, ...availabilityArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const availability: IAvailability = sampleWithRequiredData;
        const availability2: IAvailability = sampleWithPartialData;
        expectedResult = service.addAvailabilityToCollectionIfMissing([], availability, availability2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(availability);
        expect(expectedResult).toContain(availability2);
      });

      it('should accept null and undefined values', () => {
        const availability: IAvailability = sampleWithRequiredData;
        expectedResult = service.addAvailabilityToCollectionIfMissing([], null, availability, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(availability);
      });

      it('should return initial array if no Availability is added', () => {
        const availabilityCollection: IAvailability[] = [sampleWithRequiredData];
        expectedResult = service.addAvailabilityToCollectionIfMissing(availabilityCollection, undefined, null);
        expect(expectedResult).toEqual(availabilityCollection);
      });
    });

    describe('compareAvailability', () => {
      it('Should return true if both entities are null', () => {
        const entity1 = null;
        const entity2 = null;

        const compareResult = service.compareAvailability(entity1, entity2);

        expect(compareResult).toEqual(true);
      });

      it('Should return false if one entity is null', () => {
        const entity1 = { id: 123 };
        const entity2 = null;

        const compareResult1 = service.compareAvailability(entity1, entity2);
        const compareResult2 = service.compareAvailability(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey differs', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 456 };

        const compareResult1 = service.compareAvailability(entity1, entity2);
        const compareResult2 = service.compareAvailability(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey matches', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 123 };

        const compareResult1 = service.compareAvailability(entity1, entity2);
        const compareResult2 = service.compareAvailability(entity2, entity1);

        expect(compareResult1).toEqual(true);
        expect(compareResult2).toEqual(true);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
