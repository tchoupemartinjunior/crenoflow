import { TestBed } from '@angular/core/testing';

import { sampleWithRequiredData, sampleWithNewData } from '../availability.test-samples';

import { AvailabilityFormService } from './availability-form.service';

describe('Availability Form Service', () => {
  let service: AvailabilityFormService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(AvailabilityFormService);
  });

  describe('Service methods', () => {
    describe('createAvailabilityFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createAvailabilityFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            date: expect.any(Object),
            startTime: expect.any(Object),
            endTime: expect.any(Object),
            format: expect.any(Object),
            comment: expect.any(Object),
            videoLink: expect.any(Object),
            address: expect.any(Object),
            status: expect.any(Object),
            creationDate: expect.any(Object),
          }),
        );
      });

      it('passing IAvailability should create a new form with FormGroup', () => {
        const formGroup = service.createAvailabilityFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            date: expect.any(Object),
            startTime: expect.any(Object),
            endTime: expect.any(Object),
            format: expect.any(Object),
            comment: expect.any(Object),
            videoLink: expect.any(Object),
            address: expect.any(Object),
            status: expect.any(Object),
            creationDate: expect.any(Object),
          }),
        );
      });
    });

    describe('getAvailability', () => {
      it('should return NewAvailability for default Availability initial value', () => {
        const formGroup = service.createAvailabilityFormGroup(sampleWithNewData);

        const availability = service.getAvailability(formGroup) as any;

        expect(availability).toMatchObject(sampleWithNewData);
      });

      it('should return NewAvailability for empty Availability initial value', () => {
        const formGroup = service.createAvailabilityFormGroup();

        const availability = service.getAvailability(formGroup) as any;

        expect(availability).toMatchObject({});
      });

      it('should return IAvailability', () => {
        const formGroup = service.createAvailabilityFormGroup(sampleWithRequiredData);

        const availability = service.getAvailability(formGroup) as any;

        expect(availability).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing IAvailability should not enable id FormControl', () => {
        const formGroup = service.createAvailabilityFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewAvailability should disable id FormControl', () => {
        const formGroup = service.createAvailabilityFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
