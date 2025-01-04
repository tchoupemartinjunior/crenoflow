import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import SharedModule from 'app/shared/shared.module';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';

import { IPerson } from 'app/entities/person/person.model';
import { PersonService } from 'app/entities/person/service/person.service';
import { ICourseLocation } from '../course-location.model';
import { CourseLocationService } from '../service/course-location.service';
import { CourseLocationFormService, CourseLocationFormGroup } from './course-location-form.service';

@Component({
  standalone: true,
  selector: 'jhi-course-location-update',
  templateUrl: './course-location-update.component.html',
  imports: [SharedModule, FormsModule, ReactiveFormsModule],
})
export class CourseLocationUpdateComponent implements OnInit {
  isSaving = false;
  courseLocation: ICourseLocation | null = null;

  peopleSharedCollection: IPerson[] = [];

  editForm: CourseLocationFormGroup = this.courseLocationFormService.createCourseLocationFormGroup();

  constructor(
    protected courseLocationService: CourseLocationService,
    protected courseLocationFormService: CourseLocationFormService,
    protected personService: PersonService,
    protected activatedRoute: ActivatedRoute,
  ) {}

  comparePerson = (o1: IPerson | null, o2: IPerson | null): boolean => this.personService.comparePerson(o1, o2);

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ courseLocation }) => {
      this.courseLocation = courseLocation;
      if (courseLocation) {
        this.updateForm(courseLocation);
      }

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const courseLocation = this.courseLocationFormService.getCourseLocation(this.editForm);
    if (courseLocation.id !== null) {
      this.subscribeToSaveResponse(this.courseLocationService.update(courseLocation));
    } else {
      this.subscribeToSaveResponse(this.courseLocationService.create(courseLocation));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<ICourseLocation>>): void {
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

  protected updateForm(courseLocation: ICourseLocation): void {
    this.courseLocation = courseLocation;
    this.courseLocationFormService.resetForm(this.editForm, courseLocation);

    this.peopleSharedCollection = this.personService.addPersonToCollectionIfMissing<IPerson>(
      this.peopleSharedCollection,
      courseLocation.manager,
    );
  }

  protected loadRelationshipsOptions(): void {
    this.personService
      .query()
      .pipe(map((res: HttpResponse<IPerson[]>) => res.body ?? []))
      .pipe(map((people: IPerson[]) => this.personService.addPersonToCollectionIfMissing<IPerson>(people, this.courseLocation?.manager)))
      .subscribe((people: IPerson[]) => (this.peopleSharedCollection = people));
  }
}
