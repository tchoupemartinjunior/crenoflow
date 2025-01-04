import { Component } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import SharedModule from 'app/shared/shared.module';
import { ITEM_DELETED_EVENT } from 'app/config/navigation.constants';
import { ISchoolLevel } from '../school-level.model';
import { SchoolLevelService } from '../service/school-level.service';

@Component({
  standalone: true,
  templateUrl: './school-level-delete-dialog.component.html',
  imports: [SharedModule, FormsModule],
})
export class SchoolLevelDeleteDialogComponent {
  schoolLevel?: ISchoolLevel;

  constructor(
    protected schoolLevelService: SchoolLevelService,
    protected activeModal: NgbActiveModal,
  ) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.schoolLevelService.delete(id).subscribe(() => {
      this.activeModal.close(ITEM_DELETED_EVENT);
    });
  }
}
