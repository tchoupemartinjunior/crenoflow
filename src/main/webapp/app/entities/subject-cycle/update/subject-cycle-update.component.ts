import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize } from 'rxjs/operators';

import SharedModule from 'app/shared/shared.module';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';

import { ISubjectCycle } from '../subject-cycle.model';
import { SubjectCycleService } from '../service/subject-cycle.service';
import { SubjectCycleFormService, SubjectCycleFormGroup } from './subject-cycle-form.service';

@Component({
  standalone: true,
  selector: 'jhi-subject-cycle-update',
  templateUrl: './subject-cycle-update.component.html',
  imports: [SharedModule, FormsModule, ReactiveFormsModule],
})
export class SubjectCycleUpdateComponent implements OnInit {
  isSaving = false;
  subjectCycle: ISubjectCycle | null = null;

  editForm: SubjectCycleFormGroup = this.subjectCycleFormService.createSubjectCycleFormGroup();

  constructor(
    protected subjectCycleService: SubjectCycleService,
    protected subjectCycleFormService: SubjectCycleFormService,
    protected activatedRoute: ActivatedRoute,
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ subjectCycle }) => {
      this.subjectCycle = subjectCycle;
      if (subjectCycle) {
        this.updateForm(subjectCycle);
      }
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const subjectCycle = this.subjectCycleFormService.getSubjectCycle(this.editForm);
    if (subjectCycle.id !== null) {
      this.subscribeToSaveResponse(this.subjectCycleService.update(subjectCycle));
    } else {
      this.subscribeToSaveResponse(this.subjectCycleService.create(subjectCycle));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<ISubjectCycle>>): void {
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

  protected updateForm(subjectCycle: ISubjectCycle): void {
    this.subjectCycle = subjectCycle;
    this.subjectCycleFormService.resetForm(this.editForm, subjectCycle);
  }
}
