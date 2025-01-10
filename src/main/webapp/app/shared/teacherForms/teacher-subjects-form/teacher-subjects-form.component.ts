import { Component } from '@angular/core';

@Component({
  selector: 'jhi-teacher-subjects-form',
  templateUrl: './teacher-subjects-form.component.html',
  styleUrls: ['./teacher-subjects-form.component.scss'],
})
export class TeacherSubjectsFormComponent {
  selectedOptions: string[] = [];
  options = ['option 1', 'option 2', 'option 1', 'option 2', 'option 1', 'option 2', 'option 1', 'option 2'];
  onCheckboxChange(event: Event, optionValue: string): void {
    const isChecked = (event.target as HTMLInputElement).checked;
    if (isChecked) {
      this.selectedOptions.push(optionValue);
    } else {
      this.selectedOptions = this.selectedOptions.filter(value => value !== optionValue);
    }
  }
}
