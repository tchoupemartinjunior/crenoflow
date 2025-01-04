import { TestBed } from '@angular/core/testing';

import { sampleWithRequiredData, sampleWithNewData } from '../photo-person.test-samples';

import { PhotoPersonFormService } from './photo-person-form.service';

describe('PhotoPerson Form Service', () => {
  let service: PhotoPersonFormService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(PhotoPersonFormService);
  });

  describe('Service methods', () => {
    describe('createPhotoPersonFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createPhotoPersonFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            photo: expect.any(Object),
            person: expect.any(Object),
          }),
        );
      });

      it('passing IPhotoPerson should create a new form with FormGroup', () => {
        const formGroup = service.createPhotoPersonFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            photo: expect.any(Object),
            person: expect.any(Object),
          }),
        );
      });
    });

    describe('getPhotoPerson', () => {
      it('should return NewPhotoPerson for default PhotoPerson initial value', () => {
        const formGroup = service.createPhotoPersonFormGroup(sampleWithNewData);

        const photoPerson = service.getPhotoPerson(formGroup) as any;

        expect(photoPerson).toMatchObject(sampleWithNewData);
      });

      it('should return NewPhotoPerson for empty PhotoPerson initial value', () => {
        const formGroup = service.createPhotoPersonFormGroup();

        const photoPerson = service.getPhotoPerson(formGroup) as any;

        expect(photoPerson).toMatchObject({});
      });

      it('should return IPhotoPerson', () => {
        const formGroup = service.createPhotoPersonFormGroup(sampleWithRequiredData);

        const photoPerson = service.getPhotoPerson(formGroup) as any;

        expect(photoPerson).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing IPhotoPerson should not enable id FormControl', () => {
        const formGroup = service.createPhotoPersonFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewPhotoPerson should disable id FormControl', () => {
        const formGroup = service.createPhotoPersonFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
