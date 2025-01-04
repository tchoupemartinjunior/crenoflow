import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import SharedModule from 'app/shared/shared.module';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';

import { AlertError } from 'app/shared/alert/alert-error.model';
import { EventManager, EventWithContent } from 'app/core/util/event-manager.service';
import { DataUtils, FileLoadError } from 'app/core/util/data-util.service';
import { IPerson } from 'app/entities/person/person.model';
import { PersonService } from 'app/entities/person/service/person.service';
import { PhotoPersonService } from '../service/photo-person.service';
import { IPhotoPerson } from '../photo-person.model';
import { PhotoPersonFormService, PhotoPersonFormGroup } from './photo-person-form.service';

@Component({
  standalone: true,
  selector: 'jhi-photo-person-update',
  templateUrl: './photo-person-update.component.html',
  imports: [SharedModule, FormsModule, ReactiveFormsModule],
})
export class PhotoPersonUpdateComponent implements OnInit {
  isSaving = false;
  photoPerson: IPhotoPerson | null = null;

  peopleCollection: IPerson[] = [];

  editForm: PhotoPersonFormGroup = this.photoPersonFormService.createPhotoPersonFormGroup();

  constructor(
    protected dataUtils: DataUtils,
    protected eventManager: EventManager,
    protected photoPersonService: PhotoPersonService,
    protected photoPersonFormService: PhotoPersonFormService,
    protected personService: PersonService,
    protected activatedRoute: ActivatedRoute,
  ) {}

  comparePerson = (o1: IPerson | null, o2: IPerson | null): boolean => this.personService.comparePerson(o1, o2);

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ photoPerson }) => {
      this.photoPerson = photoPerson;
      if (photoPerson) {
        this.updateForm(photoPerson);
      }

      this.loadRelationshipsOptions();
    });
  }

  byteSize(base64String: string): string {
    return this.dataUtils.byteSize(base64String);
  }

  openFile(base64String: string, contentType: string | null | undefined): void {
    this.dataUtils.openFile(base64String, contentType);
  }

  setFileData(event: Event, field: string, isImage: boolean): void {
    this.dataUtils.loadFileToForm(event, this.editForm, field, isImage).subscribe({
      error: (err: FileLoadError) =>
        this.eventManager.broadcast(new EventWithContent<AlertError>('crenoFlowApp.error', { ...err, key: 'error.file.' + err.key })),
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const photoPerson = this.photoPersonFormService.getPhotoPerson(this.editForm);
    if (photoPerson.id !== null) {
      this.subscribeToSaveResponse(this.photoPersonService.update(photoPerson));
    } else {
      this.subscribeToSaveResponse(this.photoPersonService.create(photoPerson));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IPhotoPerson>>): void {
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

  protected updateForm(photoPerson: IPhotoPerson): void {
    this.photoPerson = photoPerson;
    this.photoPersonFormService.resetForm(this.editForm, photoPerson);

    this.peopleCollection = this.personService.addPersonToCollectionIfMissing<IPerson>(this.peopleCollection, photoPerson.person);
  }

  protected loadRelationshipsOptions(): void {
    this.personService
      .query({ 'photoPersonId.specified': 'false' })
      .pipe(map((res: HttpResponse<IPerson[]>) => res.body ?? []))
      .pipe(map((people: IPerson[]) => this.personService.addPersonToCollectionIfMissing<IPerson>(people, this.photoPerson?.person)))
      .subscribe((people: IPerson[]) => (this.peopleCollection = people));
  }
}
