import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize } from 'rxjs/operators';

import SharedModule from 'app/shared/shared.module';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';

import { IEducationLevel } from '../education-level.model';
import { EducationLevelService } from '../service/education-level.service';
import { EducationLevelFormService, EducationLevelFormGroup } from './education-level-form.service';

@Component({
  standalone: true,
  selector: 'jhi-education-level-update',
  templateUrl: './education-level-update.component.html',
  imports: [SharedModule, FormsModule, ReactiveFormsModule],
})
export class EducationLevelUpdateComponent implements OnInit {
  isSaving = false;
  educationLevel: IEducationLevel | null = null;

  editForm: EducationLevelFormGroup = this.educationLevelFormService.createEducationLevelFormGroup();

  constructor(
    protected educationLevelService: EducationLevelService,
    protected educationLevelFormService: EducationLevelFormService,
    protected activatedRoute: ActivatedRoute,
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ educationLevel }) => {
      this.educationLevel = educationLevel;
      if (educationLevel) {
        this.updateForm(educationLevel);
      }
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const educationLevel = this.educationLevelFormService.getEducationLevel(this.editForm);
    if (educationLevel.id !== null) {
      this.subscribeToSaveResponse(this.educationLevelService.update(educationLevel));
    } else {
      this.subscribeToSaveResponse(this.educationLevelService.create(educationLevel));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IEducationLevel>>): void {
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

  protected updateForm(educationLevel: IEducationLevel): void {
    this.educationLevel = educationLevel;
    this.educationLevelFormService.resetForm(this.editForm, educationLevel);
  }
}
