import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of, Subject, from } from 'rxjs';

import { ISubject } from 'app/entities/subject/subject.model';
import { SubjectService } from 'app/entities/subject/service/subject.service';
import { ICycle } from 'app/entities/cycle/cycle.model';
import { CycleService } from 'app/entities/cycle/service/cycle.service';
import { ICourseLocation } from 'app/entities/course-location/course-location.model';
import { CourseLocationService } from 'app/entities/course-location/service/course-location.service';
import { ITeacher } from 'app/entities/teacher/teacher.model';
import { TeacherService } from 'app/entities/teacher/service/teacher.service';
import { IAvailability } from 'app/entities/availability/availability.model';
import { AvailabilityService } from 'app/entities/availability/service/availability.service';
import { ICourse } from '../course.model';
import { CourseService } from '../service/course.service';
import { CourseFormService } from './course-form.service';

import { CourseUpdateComponent } from './course-update.component';

describe('Course Management Update Component', () => {
  let comp: CourseUpdateComponent;
  let fixture: ComponentFixture<CourseUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let courseFormService: CourseFormService;
  let courseService: CourseService;
  let subjectService: SubjectService;
  let cycleService: CycleService;
  let courseLocationService: CourseLocationService;
  let teacherService: TeacherService;
  let availabilityService: AvailabilityService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([]), CourseUpdateComponent],
      providers: [
        FormBuilder,
        {
          provide: ActivatedRoute,
          useValue: {
            params: from([{}]),
          },
        },
      ],
    })
      .overrideTemplate(CourseUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(CourseUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    courseFormService = TestBed.inject(CourseFormService);
    courseService = TestBed.inject(CourseService);
    subjectService = TestBed.inject(SubjectService);
    cycleService = TestBed.inject(CycleService);
    courseLocationService = TestBed.inject(CourseLocationService);
    teacherService = TestBed.inject(TeacherService);
    availabilityService = TestBed.inject(AvailabilityService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should call Subject query and add missing value', () => {
      const course: ICourse = { id: 456 };
      const subject: ISubject = { id: 3965 };
      course.subject = subject;

      const subjectCollection: ISubject[] = [{ id: 1678 }];
      jest.spyOn(subjectService, 'query').mockReturnValue(of(new HttpResponse({ body: subjectCollection })));
      const additionalSubjects = [subject];
      const expectedCollection: ISubject[] = [...additionalSubjects, ...subjectCollection];
      jest.spyOn(subjectService, 'addSubjectToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ course });
      comp.ngOnInit();

      expect(subjectService.query).toHaveBeenCalled();
      expect(subjectService.addSubjectToCollectionIfMissing).toHaveBeenCalledWith(
        subjectCollection,
        ...additionalSubjects.map(expect.objectContaining),
      );
      expect(comp.subjectsSharedCollection).toEqual(expectedCollection);
    });

    it('Should call Cycle query and add missing value', () => {
      const course: ICourse = { id: 456 };
      const cycle: ICycle = { id: 31695 };
      course.cycle = cycle;

      const cycleCollection: ICycle[] = [{ id: 30551 }];
      jest.spyOn(cycleService, 'query').mockReturnValue(of(new HttpResponse({ body: cycleCollection })));
      const additionalCycles = [cycle];
      const expectedCollection: ICycle[] = [...additionalCycles, ...cycleCollection];
      jest.spyOn(cycleService, 'addCycleToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ course });
      comp.ngOnInit();

      expect(cycleService.query).toHaveBeenCalled();
      expect(cycleService.addCycleToCollectionIfMissing).toHaveBeenCalledWith(
        cycleCollection,
        ...additionalCycles.map(expect.objectContaining),
      );
      expect(comp.cyclesSharedCollection).toEqual(expectedCollection);
    });

    it('Should call CourseLocation query and add missing value', () => {
      const course: ICourse = { id: 456 };
      const location: ICourseLocation = { id: 18225 };
      course.location = location;

      const courseLocationCollection: ICourseLocation[] = [{ id: 17325 }];
      jest.spyOn(courseLocationService, 'query').mockReturnValue(of(new HttpResponse({ body: courseLocationCollection })));
      const additionalCourseLocations = [location];
      const expectedCollection: ICourseLocation[] = [...additionalCourseLocations, ...courseLocationCollection];
      jest.spyOn(courseLocationService, 'addCourseLocationToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ course });
      comp.ngOnInit();

      expect(courseLocationService.query).toHaveBeenCalled();
      expect(courseLocationService.addCourseLocationToCollectionIfMissing).toHaveBeenCalledWith(
        courseLocationCollection,
        ...additionalCourseLocations.map(expect.objectContaining),
      );
      expect(comp.courseLocationsSharedCollection).toEqual(expectedCollection);
    });

    it('Should call Teacher query and add missing value', () => {
      const course: ICourse = { id: 456 };
      const teacher: ITeacher = { id: 32426 };
      course.teacher = teacher;

      const teacherCollection: ITeacher[] = [{ id: 14121 }];
      jest.spyOn(teacherService, 'query').mockReturnValue(of(new HttpResponse({ body: teacherCollection })));
      const additionalTeachers = [teacher];
      const expectedCollection: ITeacher[] = [...additionalTeachers, ...teacherCollection];
      jest.spyOn(teacherService, 'addTeacherToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ course });
      comp.ngOnInit();

      expect(teacherService.query).toHaveBeenCalled();
      expect(teacherService.addTeacherToCollectionIfMissing).toHaveBeenCalledWith(
        teacherCollection,
        ...additionalTeachers.map(expect.objectContaining),
      );
      expect(comp.teachersSharedCollection).toEqual(expectedCollection);
    });

    it('Should call Availability query and add missing value', () => {
      const course: ICourse = { id: 456 };
      const availability: IAvailability = { id: 19979 };
      course.availability = availability;

      const availabilityCollection: IAvailability[] = [{ id: 12468 }];
      jest.spyOn(availabilityService, 'query').mockReturnValue(of(new HttpResponse({ body: availabilityCollection })));
      const additionalAvailabilities = [availability];
      const expectedCollection: IAvailability[] = [...additionalAvailabilities, ...availabilityCollection];
      jest.spyOn(availabilityService, 'addAvailabilityToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ course });
      comp.ngOnInit();

      expect(availabilityService.query).toHaveBeenCalled();
      expect(availabilityService.addAvailabilityToCollectionIfMissing).toHaveBeenCalledWith(
        availabilityCollection,
        ...additionalAvailabilities.map(expect.objectContaining),
      );
      expect(comp.availabilitiesSharedCollection).toEqual(expectedCollection);
    });

    it('Should update editForm', () => {
      const course: ICourse = { id: 456 };
      const subject: ISubject = { id: 9669 };
      course.subject = subject;
      const cycle: ICycle = { id: 2778 };
      course.cycle = cycle;
      const location: ICourseLocation = { id: 5168 };
      course.location = location;
      const teacher: ITeacher = { id: 3183 };
      course.teacher = teacher;
      const availability: IAvailability = { id: 31759 };
      course.availability = availability;

      activatedRoute.data = of({ course });
      comp.ngOnInit();

      expect(comp.subjectsSharedCollection).toContain(subject);
      expect(comp.cyclesSharedCollection).toContain(cycle);
      expect(comp.courseLocationsSharedCollection).toContain(location);
      expect(comp.teachersSharedCollection).toContain(teacher);
      expect(comp.availabilitiesSharedCollection).toContain(availability);
      expect(comp.course).toEqual(course);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<ICourse>>();
      const course = { id: 123 };
      jest.spyOn(courseFormService, 'getCourse').mockReturnValue(course);
      jest.spyOn(courseService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ course });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: course }));
      saveSubject.complete();

      // THEN
      expect(courseFormService.getCourse).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(courseService.update).toHaveBeenCalledWith(expect.objectContaining(course));
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<ICourse>>();
      const course = { id: 123 };
      jest.spyOn(courseFormService, 'getCourse').mockReturnValue({ id: null });
      jest.spyOn(courseService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ course: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: course }));
      saveSubject.complete();

      // THEN
      expect(courseFormService.getCourse).toHaveBeenCalled();
      expect(courseService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<ICourse>>();
      const course = { id: 123 };
      jest.spyOn(courseService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ course });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(courseService.update).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });

  describe('Compare relationships', () => {
    describe('compareSubject', () => {
      it('Should forward to subjectService', () => {
        const entity = { id: 123 };
        const entity2 = { id: 456 };
        jest.spyOn(subjectService, 'compareSubject');
        comp.compareSubject(entity, entity2);
        expect(subjectService.compareSubject).toHaveBeenCalledWith(entity, entity2);
      });
    });

    describe('compareCycle', () => {
      it('Should forward to cycleService', () => {
        const entity = { id: 123 };
        const entity2 = { id: 456 };
        jest.spyOn(cycleService, 'compareCycle');
        comp.compareCycle(entity, entity2);
        expect(cycleService.compareCycle).toHaveBeenCalledWith(entity, entity2);
      });
    });

    describe('compareCourseLocation', () => {
      it('Should forward to courseLocationService', () => {
        const entity = { id: 123 };
        const entity2 = { id: 456 };
        jest.spyOn(courseLocationService, 'compareCourseLocation');
        comp.compareCourseLocation(entity, entity2);
        expect(courseLocationService.compareCourseLocation).toHaveBeenCalledWith(entity, entity2);
      });
    });

    describe('compareTeacher', () => {
      it('Should forward to teacherService', () => {
        const entity = { id: 123 };
        const entity2 = { id: 456 };
        jest.spyOn(teacherService, 'compareTeacher');
        comp.compareTeacher(entity, entity2);
        expect(teacherService.compareTeacher).toHaveBeenCalledWith(entity, entity2);
      });
    });

    describe('compareAvailability', () => {
      it('Should forward to availabilityService', () => {
        const entity = { id: 123 };
        const entity2 = { id: 456 };
        jest.spyOn(availabilityService, 'compareAvailability');
        comp.compareAvailability(entity, entity2);
        expect(availabilityService.compareAvailability).toHaveBeenCalledWith(entity, entity2);
      });
    });
  });
});
