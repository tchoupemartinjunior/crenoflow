import { Component } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import SharedModule from 'app/shared/shared.module';
import { ITEM_DELETED_EVENT } from 'app/config/navigation.constants';
import { ISpeciality } from '../speciality.model';
import { SpecialityService } from '../service/speciality.service';

@Component({
  standalone: true,
  templateUrl: './speciality-delete-dialog.component.html',
  imports: [SharedModule, FormsModule],
})
export class SpecialityDeleteDialogComponent {
  speciality?: ISpeciality;

  constructor(
    protected specialityService: SpecialityService,
    protected activeModal: NgbActiveModal,
  ) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.specialityService.delete(id).subscribe(() => {
      this.activeModal.close(ITEM_DELETED_EVENT);
    });
  }
}
