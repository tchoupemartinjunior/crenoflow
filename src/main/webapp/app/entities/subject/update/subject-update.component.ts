import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import SharedModule from 'app/shared/shared.module';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';

import { ISubjectCycle } from 'app/entities/subject-cycle/subject-cycle.model';
import { SubjectCycleService } from 'app/entities/subject-cycle/service/subject-cycle.service';
import { ISubject } from '../subject.model';
import { SubjectService } from '../service/subject.service';
import { SubjectFormService, SubjectFormGroup } from './subject-form.service';

@Component({
  standalone: true,
  selector: 'jhi-subject-update',
  templateUrl: './subject-update.component.html',
  imports: [SharedModule, FormsModule, ReactiveFormsModule],
})
export class SubjectUpdateComponent implements OnInit {
  isSaving = false;
  subject: ISubject | null = null;

  subjectCyclesSharedCollection: ISubjectCycle[] = [];

  editForm: SubjectFormGroup = this.subjectFormService.createSubjectFormGroup();

  constructor(
    protected subjectService: SubjectService,
    protected subjectFormService: SubjectFormService,
    protected subjectCycleService: SubjectCycleService,
    protected activatedRoute: ActivatedRoute,
  ) {}

  compareSubjectCycle = (o1: ISubjectCycle | null, o2: ISubjectCycle | null): boolean =>
    this.subjectCycleService.compareSubjectCycle(o1, o2);

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ subject }) => {
      this.subject = subject;
      if (subject) {
        this.updateForm(subject);
      }

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const subject = this.subjectFormService.getSubject(this.editForm);
    if (subject.id !== null) {
      this.subscribeToSaveResponse(this.subjectService.update(subject));
    } else {
      this.subscribeToSaveResponse(this.subjectService.create(subject));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<ISubject>>): void {
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

  protected updateForm(subject: ISubject): void {
    this.subject = subject;
    this.subjectFormService.resetForm(this.editForm, subject);

    this.subjectCyclesSharedCollection = this.subjectCycleService.addSubjectCycleToCollectionIfMissing<ISubjectCycle>(
      this.subjectCyclesSharedCollection,
      ...(subject.subjectCycles ?? []),
    );
  }

  protected loadRelationshipsOptions(): void {
    this.subjectCycleService
      .query()
      .pipe(map((res: HttpResponse<ISubjectCycle[]>) => res.body ?? []))
      .pipe(
        map((subjectCycles: ISubjectCycle[]) =>
          this.subjectCycleService.addSubjectCycleToCollectionIfMissing<ISubjectCycle>(
            subjectCycles,
            ...(this.subject?.subjectCycles ?? []),
          ),
        ),
      )
      .subscribe((subjectCycles: ISubjectCycle[]) => (this.subjectCyclesSharedCollection = subjectCycles));
  }
}
