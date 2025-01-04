import { TestBed } from '@angular/core/testing';

import { sampleWithRequiredData, sampleWithNewData } from '../subject-cycle.test-samples';

import { SubjectCycleFormService } from './subject-cycle-form.service';

describe('SubjectCycle Form Service', () => {
  let service: SubjectCycleFormService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(SubjectCycleFormService);
  });

  describe('Service methods', () => {
    describe('createSubjectCycleFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createSubjectCycleFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            teachers: expect.any(Object),
            subjects: expect.any(Object),
            cycles: expect.any(Object),
          }),
        );
      });

      it('passing ISubjectCycle should create a new form with FormGroup', () => {
        const formGroup = service.createSubjectCycleFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            teachers: expect.any(Object),
            subjects: expect.any(Object),
            cycles: expect.any(Object),
          }),
        );
      });
    });

    describe('getSubjectCycle', () => {
      it('should return NewSubjectCycle for default SubjectCycle initial value', () => {
        const formGroup = service.createSubjectCycleFormGroup(sampleWithNewData);

        const subjectCycle = service.getSubjectCycle(formGroup) as any;

        expect(subjectCycle).toMatchObject(sampleWithNewData);
      });

      it('should return NewSubjectCycle for empty SubjectCycle initial value', () => {
        const formGroup = service.createSubjectCycleFormGroup();

        const subjectCycle = service.getSubjectCycle(formGroup) as any;

        expect(subjectCycle).toMatchObject({});
      });

      it('should return ISubjectCycle', () => {
        const formGroup = service.createSubjectCycleFormGroup(sampleWithRequiredData);

        const subjectCycle = service.getSubjectCycle(formGroup) as any;

        expect(subjectCycle).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing ISubjectCycle should not enable id FormControl', () => {
        const formGroup = service.createSubjectCycleFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewSubjectCycle should disable id FormControl', () => {
        const formGroup = service.createSubjectCycleFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
