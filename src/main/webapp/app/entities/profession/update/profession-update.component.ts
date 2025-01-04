import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize } from 'rxjs/operators';

import SharedModule from 'app/shared/shared.module';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';

import { IProfession } from '../profession.model';
import { ProfessionService } from '../service/profession.service';
import { ProfessionFormService, ProfessionFormGroup } from './profession-form.service';

@Component({
  standalone: true,
  selector: 'jhi-profession-update',
  templateUrl: './profession-update.component.html',
  imports: [SharedModule, FormsModule, ReactiveFormsModule],
})
export class ProfessionUpdateComponent implements OnInit {
  isSaving = false;
  profession: IProfession | null = null;

  editForm: ProfessionFormGroup = this.professionFormService.createProfessionFormGroup();

  constructor(
    protected professionService: ProfessionService,
    protected professionFormService: ProfessionFormService,
    protected activatedRoute: ActivatedRoute,
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ profession }) => {
      this.profession = profession;
      if (profession) {
        this.updateForm(profession);
      }
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const profession = this.professionFormService.getProfession(this.editForm);
    if (profession.id !== null) {
      this.subscribeToSaveResponse(this.professionService.update(profession));
    } else {
      this.subscribeToSaveResponse(this.professionService.create(profession));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IProfession>>): void {
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

  protected updateForm(profession: IProfession): void {
    this.profession = profession;
    this.professionFormService.resetForm(this.editForm, profession);
  }
}
