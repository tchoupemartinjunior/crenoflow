import { Component } from '@angular/core';
import { FormBuilder, FormControl, FormGroup, Validators } from '@angular/forms';
import { TeacherRegistrationServiceService } from '../../../teacher/registration/teacher-registration-service.service';

@Component({
  selector: 'jhi-teacher-profil-form',
  templateUrl: './teacher-profil-form.component.html',
  styleUrls: ['./teacher-profil-form.component.scss'],
})
export class TeacherProfilFormComponent {
  profilForm: FormGroup;
  constructor(
    private fb: FormBuilder,
    private registrationService: TeacherRegistrationServiceService,
  ) {
    this.profilForm = fb.group({
      profession: ['', [Validators.required]],
      educationLevel: ['', [Validators.required]],
      speciality: ['', [Validators.required]],
    });
  }
  ngOnInit(): void {
    this.profilForm.statusChanges.subscribe(status => {
      if ((status = 'VALID')) {
        this.registrationService.teacherProfilForm.next(this.profilForm);
      } else {
        this.registrationService.teacherProfilForm.next(null);
      }
    });
  }

  getFormControl(control: string): FormControl {
    return this.profilForm.get(control) as FormControl;
  }
}
