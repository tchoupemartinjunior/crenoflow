import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { ISchoolLevel } from '../school-level.model';
import { sampleWithRequiredData, sampleWithNewData, sampleWithPartialData, sampleWithFullData } from '../school-level.test-samples';

import { SchoolLevelService } from './school-level.service';

const requireRestSample: ISchoolLevel = {
  ...sampleWithRequiredData,
};

describe('SchoolLevel Service', () => {
  let service: SchoolLevelService;
  let httpMock: HttpTestingController;
  let expectedResult: ISchoolLevel | ISchoolLevel[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(SchoolLevelService);
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

    it('should create a SchoolLevel', () => {
      const schoolLevel = { ...sampleWithNewData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.create(schoolLevel).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a SchoolLevel', () => {
      const schoolLevel = { ...sampleWithRequiredData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.update(schoolLevel).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a SchoolLevel', () => {
      const patchObject = { ...sampleWithPartialData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of SchoolLevel', () => {
      const returnedFromService = { ...requireRestSample };

      const expected = { ...sampleWithRequiredData };

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toMatchObject([expected]);
    });

    it('should delete a SchoolLevel', () => {
      const expected = true;

      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult).toBe(expected);
    });

    describe('addSchoolLevelToCollectionIfMissing', () => {
      it('should add a SchoolLevel to an empty array', () => {
        const schoolLevel: ISchoolLevel = sampleWithRequiredData;
        expectedResult = service.addSchoolLevelToCollectionIfMissing([], schoolLevel);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(schoolLevel);
      });

      it('should not add a SchoolLevel to an array that contains it', () => {
        const schoolLevel: ISchoolLevel = sampleWithRequiredData;
        const schoolLevelCollection: ISchoolLevel[] = [
          {
            ...schoolLevel,
          },
          sampleWithPartialData,
        ];
        expectedResult = service.addSchoolLevelToCollectionIfMissing(schoolLevelCollection, schoolLevel);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a SchoolLevel to an array that doesn't contain it", () => {
        const schoolLevel: ISchoolLevel = sampleWithRequiredData;
        const schoolLevelCollection: ISchoolLevel[] = [sampleWithPartialData];
        expectedResult = service.addSchoolLevelToCollectionIfMissing(schoolLevelCollection, schoolLevel);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(schoolLevel);
      });

      it('should add only unique SchoolLevel to an array', () => {
        const schoolLevelArray: ISchoolLevel[] = [sampleWithRequiredData, sampleWithPartialData, sampleWithFullData];
        const schoolLevelCollection: ISchoolLevel[] = [sampleWithRequiredData];
        expectedResult = service.addSchoolLevelToCollectionIfMissing(schoolLevelCollection, ...schoolLevelArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const schoolLevel: ISchoolLevel = sampleWithRequiredData;
        const schoolLevel2: ISchoolLevel = sampleWithPartialData;
        expectedResult = service.addSchoolLevelToCollectionIfMissing([], schoolLevel, schoolLevel2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(schoolLevel);
        expect(expectedResult).toContain(schoolLevel2);
      });

      it('should accept null and undefined values', () => {
        const schoolLevel: ISchoolLevel = sampleWithRequiredData;
        expectedResult = service.addSchoolLevelToCollectionIfMissing([], null, schoolLevel, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(schoolLevel);
      });

      it('should return initial array if no SchoolLevel is added', () => {
        const schoolLevelCollection: ISchoolLevel[] = [sampleWithRequiredData];
        expectedResult = service.addSchoolLevelToCollectionIfMissing(schoolLevelCollection, undefined, null);
        expect(expectedResult).toEqual(schoolLevelCollection);
      });
    });

    describe('compareSchoolLevel', () => {
      it('Should return true if both entities are null', () => {
        const entity1 = null;
        const entity2 = null;

        const compareResult = service.compareSchoolLevel(entity1, entity2);

        expect(compareResult).toEqual(true);
      });

      it('Should return false if one entity is null', () => {
        const entity1 = { id: 123 };
        const entity2 = null;

        const compareResult1 = service.compareSchoolLevel(entity1, entity2);
        const compareResult2 = service.compareSchoolLevel(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey differs', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 456 };

        const compareResult1 = service.compareSchoolLevel(entity1, entity2);
        const compareResult2 = service.compareSchoolLevel(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey matches', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 123 };

        const compareResult1 = service.compareSchoolLevel(entity1, entity2);
        const compareResult2 = service.compareSchoolLevel(entity2, entity1);

        expect(compareResult1).toEqual(true);
        expect(compareResult2).toEqual(true);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
