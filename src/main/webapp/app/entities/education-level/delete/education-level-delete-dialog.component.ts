import { Component } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import SharedModule from 'app/shared/shared.module';
import { ITEM_DELETED_EVENT } from 'app/config/navigation.constants';
import { IEducationLevel } from '../education-level.model';
import { EducationLevelService } from '../service/education-level.service';

@Component({
  standalone: true,
  templateUrl: './education-level-delete-dialog.component.html',
  imports: [SharedModule, FormsModule],
})
export class EducationLevelDeleteDialogComponent {
  educationLevel?: IEducationLevel;

  constructor(
    protected educationLevelService: EducationLevelService,
    protected activeModal: NgbActiveModal,
  ) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.educationLevelService.delete(id).subscribe(() => {
      this.activeModal.close(ITEM_DELETED_EVENT);
    });
  }
}
