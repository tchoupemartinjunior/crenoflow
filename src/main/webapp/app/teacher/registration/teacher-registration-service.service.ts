import { Injectable } from '@angular/core';
import { FormGroup } from '@angular/forms';
import { BehaviorSubject, Observable, of } from 'rxjs';

@Injectable({
  providedIn: 'root',
})
export class TeacherRegistrationServiceService {
  teacherContactForm: BehaviorSubject<FormGroup | null> = new BehaviorSubject<FormGroup | null>(null);
  teacherProfilForm: BehaviorSubject<FormGroup | null> = new BehaviorSubject<FormGroup | null>(null);
  teacherSubjectsContactForm: BehaviorSubject<FormGroup | null> = new BehaviorSubject<FormGroup | null>(null);
  subjectsLevelForm: BehaviorSubject<FormGroup | null> = new BehaviorSubject<FormGroup | null>(null);

  teacherContactForm$: Observable<FormGroup | null>;
  teacherProfilForm$: Observable<FormGroup | null>;
  teacherSubjectsContactForm$: Observable<FormGroup | null>;
  subjectsLevelForm$: Observable<FormGroup | null>;

  constructor() {
    this.teacherContactForm$ = this.teacherContactForm.asObservable();
    this.teacherProfilForm$ = this.teacherProfilForm.asObservable();
    this.teacherSubjectsContactForm$ = this.teacherSubjectsContactForm.asObservable();
    this.subjectsLevelForm$ = this.subjectsLevelForm.asObservable();

    this.teacherContactForm.subscribe(form => {
      if (form) {
        console.log('Valid form:', form.value);
      } else {
        console.log('Form is invalid or not yet available.');
      }
    });
  }
}
