import { TestBed } from '@angular/core/testing';

import { sampleWithRequiredData, sampleWithNewData } from '../school-level.test-samples';

import { SchoolLevelFormService } from './school-level-form.service';

describe('SchoolLevel Form Service', () => {
  let service: SchoolLevelFormService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(SchoolLevelFormService);
  });

  describe('Service methods', () => {
    describe('createSchoolLevelFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createSchoolLevelFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            label: expect.any(Object),
            cycle: expect.any(Object),
          }),
        );
      });

      it('passing ISchoolLevel should create a new form with FormGroup', () => {
        const formGroup = service.createSchoolLevelFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            label: expect.any(Object),
            cycle: expect.any(Object),
          }),
        );
      });
    });

    describe('getSchoolLevel', () => {
      it('should return NewSchoolLevel for default SchoolLevel initial value', () => {
        const formGroup = service.createSchoolLevelFormGroup(sampleWithNewData);

        const schoolLevel = service.getSchoolLevel(formGroup) as any;

        expect(schoolLevel).toMatchObject(sampleWithNewData);
      });

      it('should return NewSchoolLevel for empty SchoolLevel initial value', () => {
        const formGroup = service.createSchoolLevelFormGroup();

        const schoolLevel = service.getSchoolLevel(formGroup) as any;

        expect(schoolLevel).toMatchObject({});
      });

      it('should return ISchoolLevel', () => {
        const formGroup = service.createSchoolLevelFormGroup(sampleWithRequiredData);

        const schoolLevel = service.getSchoolLevel(formGroup) as any;

        expect(schoolLevel).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing ISchoolLevel should not enable id FormControl', () => {
        const formGroup = service.createSchoolLevelFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewSchoolLevel should disable id FormControl', () => {
        const formGroup = service.createSchoolLevelFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
