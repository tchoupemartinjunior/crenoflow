import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import SharedModule from 'app/shared/shared.module';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';

import { ISubjectCycle } from 'app/entities/subject-cycle/subject-cycle.model';
import { SubjectCycleService } from 'app/entities/subject-cycle/service/subject-cycle.service';
import { ICycle } from '../cycle.model';
import { CycleService } from '../service/cycle.service';
import { CycleFormService, CycleFormGroup } from './cycle-form.service';

@Component({
  standalone: true,
  selector: 'jhi-cycle-update',
  templateUrl: './cycle-update.component.html',
  imports: [SharedModule, FormsModule, ReactiveFormsModule],
})
export class CycleUpdateComponent implements OnInit {
  isSaving = false;
  cycle: ICycle | null = null;

  subjectCyclesSharedCollection: ISubjectCycle[] = [];

  editForm: CycleFormGroup = this.cycleFormService.createCycleFormGroup();

  constructor(
    protected cycleService: CycleService,
    protected cycleFormService: CycleFormService,
    protected subjectCycleService: SubjectCycleService,
    protected activatedRoute: ActivatedRoute,
  ) {}

  compareSubjectCycle = (o1: ISubjectCycle | null, o2: ISubjectCycle | null): boolean =>
    this.subjectCycleService.compareSubjectCycle(o1, o2);

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ cycle }) => {
      this.cycle = cycle;
      if (cycle) {
        this.updateForm(cycle);
      }

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const cycle = this.cycleFormService.getCycle(this.editForm);
    if (cycle.id !== null) {
      this.subscribeToSaveResponse(this.cycleService.update(cycle));
    } else {
      this.subscribeToSaveResponse(this.cycleService.create(cycle));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<ICycle>>): void {
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

  protected updateForm(cycle: ICycle): void {
    this.cycle = cycle;
    this.cycleFormService.resetForm(this.editForm, cycle);

    this.subjectCyclesSharedCollection = this.subjectCycleService.addSubjectCycleToCollectionIfMissing<ISubjectCycle>(
      this.subjectCyclesSharedCollection,
      ...(cycle.subjectCycles ?? []),
    );
  }

  protected loadRelationshipsOptions(): void {
    this.subjectCycleService
      .query()
      .pipe(map((res: HttpResponse<ISubjectCycle[]>) => res.body ?? []))
      .pipe(
        map((subjectCycles: ISubjectCycle[]) =>
          this.subjectCycleService.addSubjectCycleToCollectionIfMissing<ISubjectCycle>(subjectCycles, ...(this.cycle?.subjectCycles ?? [])),
        ),
      )
      .subscribe((subjectCycles: ISubjectCycle[]) => (this.subjectCyclesSharedCollection = subjectCycles));
  }
}
