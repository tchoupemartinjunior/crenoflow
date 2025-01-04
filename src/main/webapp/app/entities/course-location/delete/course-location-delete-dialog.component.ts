import { Component } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import SharedModule from 'app/shared/shared.module';
import { ITEM_DELETED_EVENT } from 'app/config/navigation.constants';
import { ICourseLocation } from '../course-location.model';
import { CourseLocationService } from '../service/course-location.service';

@Component({
  standalone: true,
  templateUrl: './course-location-delete-dialog.component.html',
  imports: [SharedModule, FormsModule],
})
export class CourseLocationDeleteDialogComponent {
  courseLocation?: ICourseLocation;

  constructor(
    protected courseLocationService: CourseLocationService,
    protected activeModal: NgbActiveModal,
  ) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.courseLocationService.delete(id).subscribe(() => {
      this.activeModal.close(ITEM_DELETED_EVENT);
    });
  }
}
