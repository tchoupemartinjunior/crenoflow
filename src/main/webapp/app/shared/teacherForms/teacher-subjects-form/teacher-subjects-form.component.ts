import { Component, OnInit, Output, EventEmitter } from '@angular/core';
import { FormGroup, FormBuilder, Validators, FormArray } from '@angular/forms';
import { SubjectService } from '../../../entities/subject/service/subject.service';
import { ISubject } from '../../../entities/subject/subject.model';
import { TeacherRegistrationServiceService } from '../../../teacher/registration/teacher-registration-service.service';

@Component({
  selector: 'jhi-teacher-subjects-form',
  templateUrl: './teacher-subjects-form.component.html',
  styleUrls: ['./teacher-subjects-form.component.scss'],
})
export class TeacherSubjectsFormComponent implements OnInit {
  @Output() _selectedSubjects: EventEmitter<ISubject[]> = new EventEmitter<ISubject[]>();

  subjectsForm: FormGroup;
  subjects: ISubject[] = [];
  constructor(
    private fb: FormBuilder,
    private subjectService: SubjectService,
    private registrationService: TeacherRegistrationServiceService,
  ) {
    this.subjectsForm = fb.group({
      selectedSubjects: new FormArray([], [Validators.required, Validators.minLength(1)]),
    });
  }

  ngOnInit(): void {
    this.subjectService.query().subscribe(response => {
      this.subjects = response?.body || [];
    });
    this.subjectsForm.statusChanges.subscribe(status => {
      if (status === 'VALID') {
        this.registrationService.teacherSubjectsForm.next(this.subjectsForm);
      } else {
        this.registrationService.teacherSubjectsForm.next(null);
      }
    });
  }
  onCheckboxChange(event: Event, subject: ISubject): void {
    const isChecked = (event.target as HTMLInputElement).checked;
    if (isChecked) {
      this.selectedSubjects.push(this.fb.control(subject));
    } else {
      const index = this.selectedSubjects.controls.findIndex(ctrl => ctrl.value.id === subject.id);
      if (index !== -1) {
        this.selectedSubjects.removeAt(index);
      }
    }
    console.log(this.selectedSubjects);
    console.log(this.subjectsForm);
  }

  get selectedSubjects(): FormArray {
    return this.subjectsForm.get('selectedSubjects') as FormArray;
  }
}
