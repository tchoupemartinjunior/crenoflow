import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize } from 'rxjs/operators';

import SharedModule from 'app/shared/shared.module';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';

import { CourseFormat } from 'app/entities/enumerations/course-format.model';
import { SlotStatus } from 'app/entities/enumerations/slot-status.model';
import { IAvailability } from '../availability.model';
import { AvailabilityService } from '../service/availability.service';
import { AvailabilityFormService, AvailabilityFormGroup } from './availability-form.service';

@Component({
  standalone: true,
  selector: 'jhi-availability-update',
  templateUrl: './availability-update.component.html',
  imports: [SharedModule, FormsModule, ReactiveFormsModule],
})
export class AvailabilityUpdateComponent implements OnInit {
  isSaving = false;
  availability: IAvailability | null = null;
  courseFormatValues = Object.keys(CourseFormat);
  slotStatusValues = Object.keys(SlotStatus);

  editForm: AvailabilityFormGroup = this.availabilityFormService.createAvailabilityFormGroup();

  constructor(
    protected availabilityService: AvailabilityService,
    protected availabilityFormService: AvailabilityFormService,
    protected activatedRoute: ActivatedRoute,
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ availability }) => {
      this.availability = availability;
      if (availability) {
        this.updateForm(availability);
      }
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const availability = this.availabilityFormService.getAvailability(this.editForm);
    if (availability.id !== null) {
      this.subscribeToSaveResponse(this.availabilityService.update(availability));
    } else {
      this.subscribeToSaveResponse(this.availabilityService.create(availability));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IAvailability>>): void {
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

  protected updateForm(availability: IAvailability): void {
    this.availability = availability;
    this.availabilityFormService.resetForm(this.editForm, availability);
  }
}
