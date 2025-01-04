import { TestBed } from '@angular/core/testing';

import { sampleWithRequiredData, sampleWithNewData } from '../speciality.test-samples';

import { SpecialityFormService } from './speciality-form.service';

describe('Speciality Form Service', () => {
  let service: SpecialityFormService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(SpecialityFormService);
  });

  describe('Service methods', () => {
    describe('createSpecialityFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createSpecialityFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            label: expect.any(Object),
          }),
        );
      });

      it('passing ISpeciality should create a new form with FormGroup', () => {
        const formGroup = service.createSpecialityFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            label: expect.any(Object),
          }),
        );
      });
    });

    describe('getSpeciality', () => {
      it('should return NewSpeciality for default Speciality initial value', () => {
        const formGroup = service.createSpecialityFormGroup(sampleWithNewData);

        const speciality = service.getSpeciality(formGroup) as any;

        expect(speciality).toMatchObject(sampleWithNewData);
      });

      it('should return NewSpeciality for empty Speciality initial value', () => {
        const formGroup = service.createSpecialityFormGroup();

        const speciality = service.getSpeciality(formGroup) as any;

        expect(speciality).toMatchObject({});
      });

      it('should return ISpeciality', () => {
        const formGroup = service.createSpecialityFormGroup(sampleWithRequiredData);

        const speciality = service.getSpeciality(formGroup) as any;

        expect(speciality).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing ISpeciality should not enable id FormControl', () => {
        const formGroup = service.createSpecialityFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewSpeciality should disable id FormControl', () => {
        const formGroup = service.createSpecialityFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
