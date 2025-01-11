import { Component, OnInit } from '@angular/core';
import { FormGroup, FormBuilder, Validators } from '@angular/forms';
import { TeacherRegistrationServiceService } from '../../../teacher/registration/teacher-registration-service.service';

@Component({
  selector: 'jhi-teacher-contact-form',
  templateUrl: './teacher-contact-form.component.html',
  styleUrls: ['./teacher-contact-form.component.scss'],
})
export class TeacherContactFormComponent implements OnInit {
  contactForm: FormGroup;
  profileImageUrl: string | ArrayBuffer | null = null;
  defaultImage = 'https://via.placeholder.com/150?text=Photo+de+profil';

  constructor(
    private fb: FormBuilder,
    private registrationService: TeacherRegistrationServiceService,
  ) {
    this.contactForm = this.fb.group({
      gender: ['', Validators.required],
      firstName: ['', [Validators.required, Validators.minLength(2)]],
      lastName: ['', [Validators.required, Validators.minLength(2)]],
      email: ['', [Validators.required, Validators.email]],
      telephone: ['', [Validators.required, Validators.pattern('^\\+?[0-9]{7,15}$')]],
      birthdate: ['', Validators.required],
      adresse: ['', Validators.required],
      town: ['', Validators.required],
      postalCode: ['', [Validators.required, Validators.pattern('^[0-9]{4,6}$')]],
      introduction: ['', [Validators.required, Validators.maxLength(500)]],
    });
  }

  ngOnInit(): void {
    this.contactForm.statusChanges.subscribe(status => {
      if ((status = 'VALID')) {
        this.registrationService.teacherContactForm.next(this.contactForm);
      } else {
        this.registrationService.teacherContactForm.next(null);
      }
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
