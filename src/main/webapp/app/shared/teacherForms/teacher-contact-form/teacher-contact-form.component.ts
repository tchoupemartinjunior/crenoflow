import { Component } from '@angular/core';
import { FormGroup, FormBuilder, Validators } from '@angular/forms';

@Component({
  selector: 'jhi-teacher-contact-form',
  templateUrl: './teacher-contact-form.component.html',
  styleUrls: ['./teacher-contact-form.component.scss'],
})
export class TeacherContactFormComponent {
  contactForm: FormGroup;
  profileImageUrl: string | ArrayBuffer | null = null;
  defaultImage = 'https://via.placeholder.com/150?text=Photo+de+profil';

  constructor(private fb: FormBuilder) {
    this.contactForm = fb.group({
      gender: ['', [Validators.required]],
      firstName: ['', [Validators.required]],
      lastName: ['', [Validators.required]],
      email: ['', [Validators.required]],
      telephone: ['', [Validators.required]],
      birthdate: ['', [Validators.required]],
      adresse: ['', [Validators.required]],
      town: ['', [Validators.required]],
      postalCode: ['', [Validators.required]],
      introduction: ['', [Validators.required]],
    });
  }

  onProfileImageChange(event: Event): void {
    const fileInput = event.target as HTMLInputElement;
    if (fileInput.files && fileInput.files[0]) {
      const reader = new FileReader();
      reader.onload = () => {
        this.profileImageUrl = reader.result;
      };
      reader.readAsDataURL(fileInput.files[0]);
    }
  }
}
