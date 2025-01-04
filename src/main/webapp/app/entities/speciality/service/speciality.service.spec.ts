import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { ISpeciality } from '../speciality.model';
import { sampleWithRequiredData, sampleWithNewData, sampleWithPartialData, sampleWithFullData } from '../speciality.test-samples';

import { SpecialityService } from './speciality.service';

const requireRestSample: ISpeciality = {
  ...sampleWithRequiredData,
};

describe('Speciality Service', () => {
  let service: SpecialityService;
  let httpMock: HttpTestingController;
  let expectedResult: ISpeciality | ISpeciality[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(SpecialityService);
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

    it('should create a Speciality', () => {
      const speciality = { ...sampleWithNewData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.create(speciality).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a Speciality', () => {
      const speciality = { ...sampleWithRequiredData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.update(speciality).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a Speciality', () => {
      const patchObject = { ...sampleWithPartialData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of Speciality', () => {
      const returnedFromService = { ...requireRestSample };

      const expected = { ...sampleWithRequiredData };

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toMatchObject([expected]);
    });

    it('should delete a Speciality', () => {
      const expected = true;

      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult).toBe(expected);
    });

    describe('addSpecialityToCollectionIfMissing', () => {
      it('should add a Speciality to an empty array', () => {
        const speciality: ISpeciality = sampleWithRequiredData;
        expectedResult = service.addSpecialityToCollectionIfMissing([], speciality);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(speciality);
      });

      it('should not add a Speciality to an array that contains it', () => {
        const speciality: ISpeciality = sampleWithRequiredData;
        const specialityCollection: ISpeciality[] = [
          {
            ...speciality,
          },
          sampleWithPartialData,
        ];
        expectedResult = service.addSpecialityToCollectionIfMissing(specialityCollection, speciality);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a Speciality to an array that doesn't contain it", () => {
        const speciality: ISpeciality = sampleWithRequiredData;
        const specialityCollection: ISpeciality[] = [sampleWithPartialData];
        expectedResult = service.addSpecialityToCollectionIfMissing(specialityCollection, speciality);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(speciality);
      });

      it('should add only unique Speciality to an array', () => {
        const specialityArray: ISpeciality[] = [sampleWithRequiredData, sampleWithPartialData, sampleWithFullData];
        const specialityCollection: ISpeciality[] = [sampleWithRequiredData];
        expectedResult = service.addSpecialityToCollectionIfMissing(specialityCollection, ...specialityArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const speciality: ISpeciality = sampleWithRequiredData;
        const speciality2: ISpeciality = sampleWithPartialData;
        expectedResult = service.addSpecialityToCollectionIfMissing([], speciality, speciality2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(speciality);
        expect(expectedResult).toContain(speciality2);
      });

      it('should accept null and undefined values', () => {
        const speciality: ISpeciality = sampleWithRequiredData;
        expectedResult = service.addSpecialityToCollectionIfMissing([], null, speciality, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(speciality);
      });

      it('should return initial array if no Speciality is added', () => {
        const specialityCollection: ISpeciality[] = [sampleWithRequiredData];
        expectedResult = service.addSpecialityToCollectionIfMissing(specialityCollection, undefined, null);
        expect(expectedResult).toEqual(specialityCollection);
      });
    });

    describe('compareSpeciality', () => {
      it('Should return true if both entities are null', () => {
        const entity1 = null;
        const entity2 = null;

        const compareResult = service.compareSpeciality(entity1, entity2);

        expect(compareResult).toEqual(true);
      });

      it('Should return false if one entity is null', () => {
        const entity1 = { id: 123 };
        const entity2 = null;

        const compareResult1 = service.compareSpeciality(entity1, entity2);
        const compareResult2 = service.compareSpeciality(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey differs', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 456 };

        const compareResult1 = service.compareSpeciality(entity1, entity2);
        const compareResult2 = service.compareSpeciality(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey matches', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 123 };

        const compareResult1 = service.compareSpeciality(entity1, entity2);
        const compareResult2 = service.compareSpeciality(entity2, entity1);

        expect(compareResult1).toEqual(true);
        expect(compareResult2).toEqual(true);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
