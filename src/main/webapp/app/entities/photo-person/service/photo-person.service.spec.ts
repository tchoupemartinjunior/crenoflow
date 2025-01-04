import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { IPhotoPerson } from '../photo-person.model';
import { sampleWithRequiredData, sampleWithNewData, sampleWithPartialData, sampleWithFullData } from '../photo-person.test-samples';

import { PhotoPersonService } from './photo-person.service';

const requireRestSample: IPhotoPerson = {
  ...sampleWithRequiredData,
};

describe('PhotoPerson Service', () => {
  let service: PhotoPersonService;
  let httpMock: HttpTestingController;
  let expectedResult: IPhotoPerson | IPhotoPerson[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(PhotoPersonService);
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

    it('should create a PhotoPerson', () => {
      const photoPerson = { ...sampleWithNewData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.create(photoPerson).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a PhotoPerson', () => {
      const photoPerson = { ...sampleWithRequiredData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.update(photoPerson).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a PhotoPerson', () => {
      const patchObject = { ...sampleWithPartialData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of PhotoPerson', () => {
      const returnedFromService = { ...requireRestSample };

      const expected = { ...sampleWithRequiredData };

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toMatchObject([expected]);
    });

    it('should delete a PhotoPerson', () => {
      const expected = true;

      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult).toBe(expected);
    });

    describe('addPhotoPersonToCollectionIfMissing', () => {
      it('should add a PhotoPerson to an empty array', () => {
        const photoPerson: IPhotoPerson = sampleWithRequiredData;
        expectedResult = service.addPhotoPersonToCollectionIfMissing([], photoPerson);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(photoPerson);
      });

      it('should not add a PhotoPerson to an array that contains it', () => {
        const photoPerson: IPhotoPerson = sampleWithRequiredData;
        const photoPersonCollection: IPhotoPerson[] = [
          {
            ...photoPerson,
          },
          sampleWithPartialData,
        ];
        expectedResult = service.addPhotoPersonToCollectionIfMissing(photoPersonCollection, photoPerson);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a PhotoPerson to an array that doesn't contain it", () => {
        const photoPerson: IPhotoPerson = sampleWithRequiredData;
        const photoPersonCollection: IPhotoPerson[] = [sampleWithPartialData];
        expectedResult = service.addPhotoPersonToCollectionIfMissing(photoPersonCollection, photoPerson);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(photoPerson);
      });

      it('should add only unique PhotoPerson to an array', () => {
        const photoPersonArray: IPhotoPerson[] = [sampleWithRequiredData, sampleWithPartialData, sampleWithFullData];
        const photoPersonCollection: IPhotoPerson[] = [sampleWithRequiredData];
        expectedResult = service.addPhotoPersonToCollectionIfMissing(photoPersonCollection, ...photoPersonArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const photoPerson: IPhotoPerson = sampleWithRequiredData;
        const photoPerson2: IPhotoPerson = sampleWithPartialData;
        expectedResult = service.addPhotoPersonToCollectionIfMissing([], photoPerson, photoPerson2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(photoPerson);
        expect(expectedResult).toContain(photoPerson2);
      });

      it('should accept null and undefined values', () => {
        const photoPerson: IPhotoPerson = sampleWithRequiredData;
        expectedResult = service.addPhotoPersonToCollectionIfMissing([], null, photoPerson, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(photoPerson);
      });

      it('should return initial array if no PhotoPerson is added', () => {
        const photoPersonCollection: IPhotoPerson[] = [sampleWithRequiredData];
        expectedResult = service.addPhotoPersonToCollectionIfMissing(photoPersonCollection, undefined, null);
        expect(expectedResult).toEqual(photoPersonCollection);
      });
    });

    describe('comparePhotoPerson', () => {
      it('Should return true if both entities are null', () => {
        const entity1 = null;
        const entity2 = null;

        const compareResult = service.comparePhotoPerson(entity1, entity2);

        expect(compareResult).toEqual(true);
      });

      it('Should return false if one entity is null', () => {
        const entity1 = { id: 123 };
        const entity2 = null;

        const compareResult1 = service.comparePhotoPerson(entity1, entity2);
        const compareResult2 = service.comparePhotoPerson(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey differs', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 456 };

        const compareResult1 = service.comparePhotoPerson(entity1, entity2);
        const compareResult2 = service.comparePhotoPerson(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey matches', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 123 };

        const compareResult1 = service.comparePhotoPerson(entity1, entity2);
        const compareResult2 = service.comparePhotoPerson(entity2, entity1);

        expect(compareResult1).toEqual(true);
        expect(compareResult2).toEqual(true);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
