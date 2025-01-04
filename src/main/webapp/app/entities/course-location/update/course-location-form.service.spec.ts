import { TestBed } from '@angular/core/testing';

import { sampleWithRequiredData, sampleWithNewData } from '../course-location.test-samples';

import { CourseLocationFormService } from './course-location-form.service';

describe('CourseLocation Form Service', () => {
  let service: CourseLocationFormService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(CourseLocationFormService);
  });

  describe('Service methods', () => {
    describe('createCourseLocationFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createCourseLocationFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            address: expect.any(Object),
            name: expect.any(Object),
            manager: expect.any(Object),
          }),
        );
      });

      it('passing ICourseLocation should create a new form with FormGroup', () => {
        const formGroup = service.createCourseLocationFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            address: expect.any(Object),
            name: expect.any(Object),
            manager: expect.any(Object),
          }),
        );
      });
    });

    describe('getCourseLocation', () => {
      it('should return NewCourseLocation for default CourseLocation initial value', () => {
        const formGroup = service.createCourseLocationFormGroup(sampleWithNewData);

        const courseLocation = service.getCourseLocation(formGroup) as any;

        expect(courseLocation).toMatchObject(sampleWithNewData);
      });

      it('should return NewCourseLocation for empty CourseLocation initial value', () => {
        const formGroup = service.createCourseLocationFormGroup();

        const courseLocation = service.getCourseLocation(formGroup) as any;

        expect(courseLocation).toMatchObject({});
      });

      it('should return ICourseLocation', () => {
        const formGroup = service.createCourseLocationFormGroup(sampleWithRequiredData);

        const courseLocation = service.getCourseLocation(formGroup) as any;

        expect(courseLocation).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing ICourseLocation should not enable id FormControl', () => {
        const formGroup = service.createCourseLocationFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewCourseLocation should disable id FormControl', () => {
        const formGroup = service.createCourseLocationFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
