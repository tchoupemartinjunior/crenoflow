import { Component } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import SharedModule from 'app/shared/shared.module';
import { ITEM_DELETED_EVENT } from 'app/config/navigation.constants';
import { IAvailability } from '../availability.model';
import { AvailabilityService } from '../service/availability.service';

@Component({
  standalone: true,
  templateUrl: './availability-delete-dialog.component.html',
  imports: [SharedModule, FormsModule],
})
export class AvailabilityDeleteDialogComponent {
  availability?: IAvailability;

  constructor(
    protected availabilityService: AvailabilityService,
    protected activeModal: NgbActiveModal,
  ) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.availabilityService.delete(id).subscribe(() => {
      this.activeModal.close(ITEM_DELETED_EVENT);
    });
  }
}
