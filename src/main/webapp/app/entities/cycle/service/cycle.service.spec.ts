import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { ICycle } from '../cycle.model';
import { sampleWithRequiredData, sampleWithNewData, sampleWithPartialData, sampleWithFullData } from '../cycle.test-samples';

import { CycleService } from './cycle.service';

const requireRestSample: ICycle = {
  ...sampleWithRequiredData,
};

describe('Cycle Service', () => {
  let service: CycleService;
  let httpMock: HttpTestingController;
  let expectedResult: ICycle | ICycle[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(CycleService);
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

    it('should create a Cycle', () => {
      const cycle = { ...sampleWithNewData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.create(cycle).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a Cycle', () => {
      const cycle = { ...sampleWithRequiredData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.update(cycle).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a Cycle', () => {
      const patchObject = { ...sampleWithPartialData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of Cycle', () => {
      const returnedFromService = { ...requireRestSample };

      const expected = { ...sampleWithRequiredData };

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toMatchObject([expected]);
    });

    it('should delete a Cycle', () => {
      const expected = true;

      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult).toBe(expected);
    });

    describe('addCycleToCollectionIfMissing', () => {
      it('should add a Cycle to an empty array', () => {
        const cycle: ICycle = sampleWithRequiredData;
        expectedResult = service.addCycleToCollectionIfMissing([], cycle);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(cycle);
      });

      it('should not add a Cycle to an array that contains it', () => {
        const cycle: ICycle = sampleWithRequiredData;
        const cycleCollection: ICycle[] = [
          {
            ...cycle,
          },
          sampleWithPartialData,
        ];
        expectedResult = service.addCycleToCollectionIfMissing(cycleCollection, cycle);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a Cycle to an array that doesn't contain it", () => {
        const cycle: ICycle = sampleWithRequiredData;
        const cycleCollection: ICycle[] = [sampleWithPartialData];
        expectedResult = service.addCycleToCollectionIfMissing(cycleCollection, cycle);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(cycle);
      });

      it('should add only unique Cycle to an array', () => {
        const cycleArray: ICycle[] = [sampleWithRequiredData, sampleWithPartialData, sampleWithFullData];
        const cycleCollection: ICycle[] = [sampleWithRequiredData];
        expectedResult = service.addCycleToCollectionIfMissing(cycleCollection, ...cycleArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const cycle: ICycle = sampleWithRequiredData;
        const cycle2: ICycle = sampleWithPartialData;
        expectedResult = service.addCycleToCollectionIfMissing([], cycle, cycle2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(cycle);
        expect(expectedResult).toContain(cycle2);
      });

      it('should accept null and undefined values', () => {
        const cycle: ICycle = sampleWithRequiredData;
        expectedResult = service.addCycleToCollectionIfMissing([], null, cycle, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(cycle);
      });

      it('should return initial array if no Cycle is added', () => {
        const cycleCollection: ICycle[] = [sampleWithRequiredData];
        expectedResult = service.addCycleToCollectionIfMissing(cycleCollection, undefined, null);
        expect(expectedResult).toEqual(cycleCollection);
      });
    });

    describe('compareCycle', () => {
      it('Should return true if both entities are null', () => {
        const entity1 = null;
        const entity2 = null;

        const compareResult = service.compareCycle(entity1, entity2);

        expect(compareResult).toEqual(true);
      });

      it('Should return false if one entity is null', () => {
        const entity1 = { id: 123 };
        const entity2 = null;

        const compareResult1 = service.compareCycle(entity1, entity2);
        const compareResult2 = service.compareCycle(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey differs', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 456 };

        const compareResult1 = service.compareCycle(entity1, entity2);
        const compareResult2 = service.compareCycle(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey matches', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 123 };

        const compareResult1 = service.compareCycle(entity1, entity2);
        const compareResult2 = service.compareCycle(entity2, entity1);

        expect(compareResult1).toEqual(true);
        expect(compareResult2).toEqual(true);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
