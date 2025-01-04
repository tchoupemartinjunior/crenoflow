import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize } from 'rxjs/operators';

import SharedModule from 'app/shared/shared.module';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';

import { ISpeciality } from '../speciality.model';
import { SpecialityService } from '../service/speciality.service';
import { SpecialityFormService, SpecialityFormGroup } from './speciality-form.service';

@Component({
  standalone: true,
  selector: 'jhi-speciality-update',
  templateUrl: './speciality-update.component.html',
  imports: [SharedModule, FormsModule, ReactiveFormsModule],
})
export class SpecialityUpdateComponent implements OnInit {
  isSaving = false;
  speciality: ISpeciality | null = null;

  editForm: SpecialityFormGroup = this.specialityFormService.createSpecialityFormGroup();

  constructor(
    protected specialityService: SpecialityService,
    protected specialityFormService: SpecialityFormService,
    protected activatedRoute: ActivatedRoute,
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ speciality }) => {
      this.speciality = speciality;
      if (speciality) {
        this.updateForm(speciality);
      }
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const speciality = this.specialityFormService.getSpeciality(this.editForm);
    if (speciality.id !== null) {
      this.subscribeToSaveResponse(this.specialityService.update(speciality));
    } else {
      this.subscribeToSaveResponse(this.specialityService.create(speciality));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<ISpeciality>>): void {
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

  protected updateForm(speciality: ISpeciality): void {
    this.speciality = speciality;
    this.specialityFormService.resetForm(this.editForm, speciality);
  }
}
