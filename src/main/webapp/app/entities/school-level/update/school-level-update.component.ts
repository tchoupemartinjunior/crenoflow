import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import SharedModule from 'app/shared/shared.module';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';

import { ICycle } from 'app/entities/cycle/cycle.model';
import { CycleService } from 'app/entities/cycle/service/cycle.service';
import { ISchoolLevel } from '../school-level.model';
import { SchoolLevelService } from '../service/school-level.service';
import { SchoolLevelFormService, SchoolLevelFormGroup } from './school-level-form.service';

@Component({
  standalone: true,
  selector: 'jhi-school-level-update',
  templateUrl: './school-level-update.component.html',
  imports: [SharedModule, FormsModule, ReactiveFormsModule],
})
export class SchoolLevelUpdateComponent implements OnInit {
  isSaving = false;
  schoolLevel: ISchoolLevel | null = null;

  cyclesSharedCollection: ICycle[] = [];

  editForm: SchoolLevelFormGroup = this.schoolLevelFormService.createSchoolLevelFormGroup();

  constructor(
    protected schoolLevelService: SchoolLevelService,
    protected schoolLevelFormService: SchoolLevelFormService,
    protected cycleService: CycleService,
    protected activatedRoute: ActivatedRoute,
  ) {}

  compareCycle = (o1: ICycle | null, o2: ICycle | null): boolean => this.cycleService.compareCycle(o1, o2);

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ schoolLevel }) => {
      this.schoolLevel = schoolLevel;
      if (schoolLevel) {
        this.updateForm(schoolLevel);
      }

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const schoolLevel = this.schoolLevelFormService.getSchoolLevel(this.editForm);
    if (schoolLevel.id !== null) {
      this.subscribeToSaveResponse(this.schoolLevelService.update(schoolLevel));
    } else {
      this.subscribeToSaveResponse(this.schoolLevelService.create(schoolLevel));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<ISchoolLevel>>): void {
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

  protected updateForm(schoolLevel: ISchoolLevel): void {
    this.schoolLevel = schoolLevel;
    this.schoolLevelFormService.resetForm(this.editForm, schoolLevel);

    this.cyclesSharedCollection = this.cycleService.addCycleToCollectionIfMissing<ICycle>(this.cyclesSharedCollection, schoolLevel.cycle);
  }

  protected loadRelationshipsOptions(): void {
    this.cycleService
      .query()
      .pipe(map((res: HttpResponse<ICycle[]>) => res.body ?? []))
      .pipe(map((cycles: ICycle[]) => this.cycleService.addCycleToCollectionIfMissing<ICycle>(cycles, this.schoolLevel?.cycle)))
      .subscribe((cycles: ICycle[]) => (this.cyclesSharedCollection = cycles));
  }
}
