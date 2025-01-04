import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { IEducationLevel } from '../education-level.model';
import { sampleWithRequiredData, sampleWithNewData, sampleWithPartialData, sampleWithFullData } from '../education-level.test-samples';

import { EducationLevelService } from './education-level.service';

const requireRestSample: IEducationLevel = {
  ...sampleWithRequiredData,
};

describe('EducationLevel Service', () => {
  let service: EducationLevelService;
  let httpMock: HttpTestingController;
  let expectedResult: IEducationLevel | IEducationLevel[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(EducationLevelService);
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

    it('should create a EducationLevel', () => {
      const educationLevel = { ...sampleWithNewData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.create(educationLevel).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a EducationLevel', () => {
      const educationLevel = { ...sampleWithRequiredData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.update(educationLevel).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a EducationLevel', () => {
      const patchObject = { ...sampleWithPartialData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of EducationLevel', () => {
      const returnedFromService = { ...requireRestSample };

      const expected = { ...sampleWithRequiredData };

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toMatchObject([expected]);
    });

    it('should delete a EducationLevel', () => {
      const expected = true;

      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult).toBe(expected);
    });

    describe('addEducationLevelToCollectionIfMissing', () => {
      it('should add a EducationLevel to an empty array', () => {
        const educationLevel: IEducationLevel = sampleWithRequiredData;
        expectedResult = service.addEducationLevelToCollectionIfMissing([], educationLevel);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(educationLevel);
      });

      it('should not add a EducationLevel to an array that contains it', () => {
        const educationLevel: IEducationLevel = sampleWithRequiredData;
        const educationLevelCollection: IEducationLevel[] = [
          {
            ...educationLevel,
          },
          sampleWithPartialData,
        ];
        expectedResult = service.addEducationLevelToCollectionIfMissing(educationLevelCollection, educationLevel);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a EducationLevel to an array that doesn't contain it", () => {
        const educationLevel: IEducationLevel = sampleWithRequiredData;
        const educationLevelCollection: IEducationLevel[] = [sampleWithPartialData];
        expectedResult = service.addEducationLevelToCollectionIfMissing(educationLevelCollection, educationLevel);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(educationLevel);
      });

      it('should add only unique EducationLevel to an array', () => {
        const educationLevelArray: IEducationLevel[] = [sampleWithRequiredData, sampleWithPartialData, sampleWithFullData];
        const educationLevelCollection: IEducationLevel[] = [sampleWithRequiredData];
        expectedResult = service.addEducationLevelToCollectionIfMissing(educationLevelCollection, ...educationLevelArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const educationLevel: IEducationLevel = sampleWithRequiredData;
        const educationLevel2: IEducationLevel = sampleWithPartialData;
        expectedResult = service.addEducationLevelToCollectionIfMissing([], educationLevel, educationLevel2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(educationLevel);
        expect(expectedResult).toContain(educationLevel2);
      });

      it('should accept null and undefined values', () => {
        const educationLevel: IEducationLevel = sampleWithRequiredData;
        expectedResult = service.addEducationLevelToCollectionIfMissing([], null, educationLevel, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(educationLevel);
      });

      it('should return initial array if no EducationLevel is added', () => {
        const educationLevelCollection: IEducationLevel[] = [sampleWithRequiredData];
        expectedResult = service.addEducationLevelToCollectionIfMissing(educationLevelCollection, undefined, null);
        expect(expectedResult).toEqual(educationLevelCollection);
      });
    });

    describe('compareEducationLevel', () => {
      it('Should return true if both entities are null', () => {
        const entity1 = null;
        const entity2 = null;

        const compareResult = service.compareEducationLevel(entity1, entity2);

        expect(compareResult).toEqual(true);
      });

      it('Should return false if one entity is null', () => {
        const entity1 = { id: 123 };
        const entity2 = null;

        const compareResult1 = service.compareEducationLevel(entity1, entity2);
        const compareResult2 = service.compareEducationLevel(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey differs', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 456 };

        const compareResult1 = service.compareEducationLevel(entity1, entity2);
        const compareResult2 = service.compareEducationLevel(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey matches', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 123 };

        const compareResult1 = service.compareEducationLevel(entity1, entity2);
        const compareResult2 = service.compareEducationLevel(entity2, entity1);

        expect(compareResult1).toEqual(true);
        expect(compareResult2).toEqual(true);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
