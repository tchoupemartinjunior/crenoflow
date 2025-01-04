import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import SharedModule from 'app/shared/shared.module';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';

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
import { CourseService } from '../service/course.service';
import { ICourse } from '../course.model';
import { CourseFormService, CourseFormGroup } from './course-form.service';

@Component({
  standalone: true,
  selector: 'jhi-course-update',
  templateUrl: './course-update.component.html',
  imports: [SharedModule, FormsModule, ReactiveFormsModule],
})
export class CourseUpdateComponent implements OnInit {
  isSaving = false;
  course: ICourse | null = null;

  subjectsSharedCollection: ISubject[] = [];
  cyclesSharedCollection: ICycle[] = [];
  courseLocationsSharedCollection: ICourseLocation[] = [];
  teachersSharedCollection: ITeacher[] = [];
  availabilitiesSharedCollection: IAvailability[] = [];

  editForm: CourseFormGroup = this.courseFormService.createCourseFormGroup();

  constructor(
    protected courseService: CourseService,
    protected courseFormService: CourseFormService,
    protected subjectService: SubjectService,
    protected cycleService: CycleService,
    protected courseLocationService: CourseLocationService,
    protected teacherService: TeacherService,
    protected availabilityService: AvailabilityService,
    protected activatedRoute: ActivatedRoute,
  ) {}

  compareSubject = (o1: ISubject | null, o2: ISubject | null): boolean => this.subjectService.compareSubject(o1, o2);

  compareCycle = (o1: ICycle | null, o2: ICycle | null): boolean => this.cycleService.compareCycle(o1, o2);

  compareCourseLocation = (o1: ICourseLocation | null, o2: ICourseLocation | null): boolean =>
    this.courseLocationService.compareCourseLocation(o1, o2);

  compareTeacher = (o1: ITeacher | null, o2: ITeacher | null): boolean => this.teacherService.compareTeacher(o1, o2);

  compareAvailability = (o1: IAvailability | null, o2: IAvailability | null): boolean =>
    this.availabilityService.compareAvailability(o1, o2);

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ course }) => {
      this.course = course;
      if (course) {
        this.updateForm(course);
      }

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const course = this.courseFormService.getCourse(this.editForm);
    if (course.id !== null) {
      this.subscribeToSaveResponse(this.courseService.update(course));
    } else {
      this.subscribeToSaveResponse(this.courseService.create(course));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<ICourse>>): void {
    result.pipe(finalize(() => this.onSaveFinalize())).subscribe({
      next: () => this.onSaveSuccess(),
      error: () => this.onSaveError(),
    });
  }

  protected onSaveSuccess(): void {
    this.previousState();
  }

  protected onSaveError(): void {
    // Api for inheritance.
  }

  protected onSaveFinalize(): void {
    this.isSaving = false;
  }

  protected updateForm(course: ICourse): void {
    this.course = course;
    this.courseFormService.resetForm(this.editForm, course);

    this.subjectsSharedCollection = this.subjectService.addSubjectToCollectionIfMissing<ISubject>(
      this.subjectsSharedCollection,
      course.subject,
    );
    this.cyclesSharedCollection = this.cycleService.addCycleToCollectionIfMissing<ICycle>(this.cyclesSharedCollection, course.cycle);
    this.courseLocationsSharedCollection = this.courseLocationService.addCourseLocationToCollectionIfMissing<ICourseLocation>(
      this.courseLocationsSharedCollection,
      course.location,
    );
    this.teachersSharedCollection = this.teacherService.addTeacherToCollectionIfMissing<ITeacher>(
      this.teachersSharedCollection,
      course.teacher,
    );
    this.availabilitiesSharedCollection = this.availabilityService.addAvailabilityToCollectionIfMissing<IAvailability>(
      this.availabilitiesSharedCollection,
      course.availability,
    );
  }

  protected loadRelationshipsOptions(): void {
    this.subjectService
      .query()
      .pipe(map((res: HttpResponse<ISubject[]>) => res.body ?? []))
      .pipe(map((subjects: ISubject[]) => this.subjectService.addSubjectToCollectionIfMissing<ISubject>(subjects, this.course?.subject)))
      .subscribe((subjects: ISubject[]) => (this.subjectsSharedCollection = subjects));

    this.cycleService
      .query()
      .pipe(map((res: HttpResponse<ICycle[]>) => res.body ?? []))
      .pipe(map((cycles: ICycle[]) => this.cycleService.addCycleToCollectionIfMissing<ICycle>(cycles, this.course?.cycle)))
      .subscribe((cycles: ICycle[]) => (this.cyclesSharedCollection = cycles));

    this.courseLocationService
      .query()
      .pipe(map((res: HttpResponse<ICourseLocation[]>) => res.body ?? []))
      .pipe(
        map((courseLocations: ICourseLocation[]) =>
          this.courseLocationService.addCourseLocationToCollectionIfMissing<ICourseLocation>(courseLocations, this.course?.location),
        ),
      )
      .subscribe((courseLocations: ICourseLocation[]) => (this.courseLocationsSharedCollection = courseLocations));

    this.teacherService
      .query()
      .pipe(map((res: HttpResponse<ITeacher[]>) => res.body ?? []))
      .pipe(map((teachers: ITeacher[]) => this.teacherService.addTeacherToCollectionIfMissing<ITeacher>(teachers, this.course?.teacher)))
      .subscribe((teachers: ITeacher[]) => (this.teachersSharedCollection = teachers));

    this.availabilityService
      .query()
      .pipe(map((res: HttpResponse<IAvailability[]>) => res.body ?? []))
      .pipe(
        map((availabilities: IAvailability[]) =>
          this.availabilityService.addAvailabilityToCollectionIfMissing<IAvailability>(availabilities, this.course?.availability),
        ),
      )
      .subscribe((availabilities: IAvailability[]) => (this.availabilitiesSharedCollection = availabilities));
  }
}
