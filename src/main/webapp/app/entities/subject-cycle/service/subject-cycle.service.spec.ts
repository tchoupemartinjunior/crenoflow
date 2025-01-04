import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { ISubjectCycle } from '../subject-cycle.model';
import { sampleWithRequiredData, sampleWithNewData, sampleWithPartialData, sampleWithFullData } from '../subject-cycle.test-samples';

import { SubjectCycleService } from './subject-cycle.service';

const requireRestSample: ISubjectCycle = {
  ...sampleWithRequiredData,
};

describe('SubjectCycle Service', () => {
  let service: SubjectCycleService;
  let httpMock: HttpTestingController;
  let expectedResult: ISubjectCycle | ISubjectCycle[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(SubjectCycleService);
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

    it('should create a SubjectCycle', () => {
      const subjectCycle = { ...sampleWithNewData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.create(subjectCycle).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a SubjectCycle', () => {
      const subjectCycle = { ...sampleWithRequiredData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.update(subjectCycle).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a SubjectCycle', () => {
      const patchObject = { ...sampleWithPartialData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of SubjectCycle', () => {
      const returnedFromService = { ...requireRestSample };

      const expected = { ...sampleWithRequiredData };

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toMatchObject([expected]);
    });

    it('should delete a SubjectCycle', () => {
      const expected = true;

      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult).toBe(expected);
    });

    describe('addSubjectCycleToCollectionIfMissing', () => {
      it('should add a SubjectCycle to an empty array', () => {
        const subjectCycle: ISubjectCycle = sampleWithRequiredData;
        expectedResult = service.addSubjectCycleToCollectionIfMissing([], subjectCycle);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(subjectCycle);
      });

      it('should not add a SubjectCycle to an array that contains it', () => {
        const subjectCycle: ISubjectCycle = sampleWithRequiredData;
        const subjectCycleCollection: ISubjectCycle[] = [
          {
            ...subjectCycle,
          },
          sampleWithPartialData,
        ];
        expectedResult = service.addSubjectCycleToCollectionIfMissing(subjectCycleCollection, subjectCycle);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a SubjectCycle to an array that doesn't contain it", () => {
        const subjectCycle: ISubjectCycle = sampleWithRequiredData;
        const subjectCycleCollection: ISubjectCycle[] = [sampleWithPartialData];
        expectedResult = service.addSubjectCycleToCollectionIfMissing(subjectCycleCollection, subjectCycle);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(subjectCycle);
      });

      it('should add only unique SubjectCycle to an array', () => {
        const subjectCycleArray: ISubjectCycle[] = [sampleWithRequiredData, sampleWithPartialData, sampleWithFullData];
        const subjectCycleCollection: ISubjectCycle[] = [sampleWithRequiredData];
        expectedResult = service.addSubjectCycleToCollectionIfMissing(subjectCycleCollection, ...subjectCycleArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const subjectCycle: ISubjectCycle = sampleWithRequiredData;
        const subjectCycle2: ISubjectCycle = sampleWithPartialData;
        expectedResult = service.addSubjectCycleToCollectionIfMissing([], subjectCycle, subjectCycle2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(subjectCycle);
        expect(expectedResult).toContain(subjectCycle2);
      });

      it('should accept null and undefined values', () => {
        const subjectCycle: ISubjectCycle = sampleWithRequiredData;
        expectedResult = service.addSubjectCycleToCollectionIfMissing([], null, subjectCycle, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(subjectCycle);
      });

      it('should return initial array if no SubjectCycle is added', () => {
        const subjectCycleCollection: ISubjectCycle[] = [sampleWithRequiredData];
        expectedResult = service.addSubjectCycleToCollectionIfMissing(subjectCycleCollection, undefined, null);
        expect(expectedResult).toEqual(subjectCycleCollection);
      });
    });

    describe('compareSubjectCycle', () => {
      it('Should return true if both entities are null', () => {
        const entity1 = null;
        const entity2 = null;

        const compareResult = service.compareSubjectCycle(entity1, entity2);

        expect(compareResult).toEqual(true);
      });

      it('Should return false if one entity is null', () => {
        const entity1 = { id: 123 };
        const entity2 = null;

        const compareResult1 = service.compareSubjectCycle(entity1, entity2);
        const compareResult2 = service.compareSubjectCycle(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey differs', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 456 };

        const compareResult1 = service.compareSubjectCycle(entity1, entity2);
        const compareResult2 = service.compareSubjectCycle(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey matches', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 123 };

        const compareResult1 = service.compareSubjectCycle(entity1, entity2);
        const compareResult2 = service.compareSubjectCycle(entity2, entity1);

        expect(compareResult1).toEqual(true);
        expect(compareResult2).toEqual(true);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
