import { TestBed } from '@angular/core/testing';

import { sampleWithRequiredData, sampleWithNewData } from '../education-level.test-samples';

import { EducationLevelFormService } from './education-level-form.service';

describe('EducationLevel Form Service', () => {
  let service: EducationLevelFormService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(EducationLevelFormService);
  });

  describe('Service methods', () => {
    describe('createEducationLevelFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createEducationLevelFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            label: expect.any(Object),
          }),
        );
      });

      it('passing IEducationLevel should create a new form with FormGroup', () => {
        const formGroup = service.createEducationLevelFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            label: expect.any(Object),
          }),
        );
      });
    });

    describe('getEducationLevel', () => {
      it('should return NewEducationLevel for default EducationLevel initial value', () => {
        const formGroup = service.createEducationLevelFormGroup(sampleWithNewData);

        const educationLevel = service.getEducationLevel(formGroup) as any;

        expect(educationLevel).toMatchObject(sampleWithNewData);
      });

      it('should return NewEducationLevel for empty EducationLevel initial value', () => {
        const formGroup = service.createEducationLevelFormGroup();

        const educationLevel = service.getEducationLevel(formGroup) as any;

        expect(educationLevel).toMatchObject({});
      });

      it('should return IEducationLevel', () => {
        const formGroup = service.createEducationLevelFormGroup(sampleWithRequiredData);

        const educationLevel = service.getEducationLevel(formGroup) as any;

        expect(educationLevel).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing IEducationLevel should not enable id FormControl', () => {
        const formGroup = service.createEducationLevelFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewEducationLevel should disable id FormControl', () => {
        const formGroup = service.createEducationLevelFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
