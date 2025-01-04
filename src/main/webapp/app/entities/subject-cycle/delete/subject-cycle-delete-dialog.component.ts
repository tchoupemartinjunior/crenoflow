import { Component } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import SharedModule from 'app/shared/shared.module';
import { ITEM_DELETED_EVENT } from 'app/config/navigation.constants';
import { ISubjectCycle } from '../subject-cycle.model';
import { SubjectCycleService } from '../service/subject-cycle.service';

@Component({
  standalone: true,
  templateUrl: './subject-cycle-delete-dialog.component.html',
  imports: [SharedModule, FormsModule],
})
export class SubjectCycleDeleteDialogComponent {
  subjectCycle?: ISubjectCycle;

  constructor(
    protected subjectCycleService: SubjectCycleService,
    protected activeModal: NgbActiveModal,
  ) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.subjectCycleService.delete(id).subscribe(() => {
      this.activeModal.close(ITEM_DELETED_EVENT);
    });
  }
}
