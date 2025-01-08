import { Component } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';

@Component({
  selector: 'jhi-teacher-profil-form',
  templateUrl: './teacher-profil-form.component.html',
  styleUrls: ['./teacher-profil-form.component.scss'],
})
export class TeacherProfilFormComponent {
  profilForm: FormGroup;
  constructor(private fb: FormBuilder) {
    this.profilForm = fb.group({
      profession: ['', [Validators.required]],
      educationLevel: ['', [Validators.required]],
      speciality: ['', [Validators.required]],
    });
  }
}
