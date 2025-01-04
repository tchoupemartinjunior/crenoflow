import { TestBed } from '@angular/core/testing';

import { sampleWithRequiredData, sampleWithNewData } from '../cycle.test-samples';

import { CycleFormService } from './cycle-form.service';

describe('Cycle Form Service', () => {
  let service: CycleFormService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(CycleFormService);
  });

  describe('Service methods', () => {
    describe('createCycleFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createCycleFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            label: expect.any(Object),
            subjectCycles: expect.any(Object),
          }),
        );
      });

      it('passing ICycle should create a new form with FormGroup', () => {
        const formGroup = service.createCycleFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            label: expect.any(Object),
            subjectCycles: expect.any(Object),
          }),
        );
      });
    });

    describe('getCycle', () => {
      it('should return NewCycle for default Cycle initial value', () => {
        const formGroup = service.createCycleFormGroup(sampleWithNewData);

        const cycle = service.getCycle(formGroup) as any;

        expect(cycle).toMatchObject(sampleWithNewData);
      });

      it('should return NewCycle for empty Cycle initial value', () => {
        const formGroup = service.createCycleFormGroup();

        const cycle = service.getCycle(formGroup) as any;

        expect(cycle).toMatchObject({});
      });

      it('should return ICycle', () => {
        const formGroup = service.createCycleFormGroup(sampleWithRequiredData);

        const cycle = service.getCycle(formGroup) as any;

        expect(cycle).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing ICycle should not enable id FormControl', () => {
        const formGroup = service.createCycleFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewCycle should disable id FormControl', () => {
        const formGroup = service.createCycleFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
