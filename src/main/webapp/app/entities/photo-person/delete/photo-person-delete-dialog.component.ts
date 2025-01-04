import { Component } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import SharedModule from 'app/shared/shared.module';
import { ITEM_DELETED_EVENT } from 'app/config/navigation.constants';
import { IPhotoPerson } from '../photo-person.model';
import { PhotoPersonService } from '../service/photo-person.service';

@Component({
  standalone: true,
  templateUrl: './photo-person-delete-dialog.component.html',
  imports: [SharedModule, FormsModule],
})
export class PhotoPersonDeleteDialogComponent {
  photoPerson?: IPhotoPerson;

  constructor(
    protected photoPersonService: PhotoPersonService,
    protected activeModal: NgbActiveModal,
  ) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.photoPersonService.delete(id).subscribe(() => {
      this.activeModal.close(ITEM_DELETED_EVENT);
    });
  }
}
