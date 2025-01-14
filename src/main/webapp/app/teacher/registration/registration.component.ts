import { Component } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { CustomStepperComponent } from '../../shared/custom-stepper/custom-stepper.component';
import TeacherSubjectsFormComponent from '../../shared/shared.module';
import { StepState } from '@angular/cdk/stepper';
import { TeacherRegistrationServiceService } from './teacher-registration-service.service';
import { Observable } from 'rxjs';

@Component({
  selector: 'jhi-registration',
  templateUrl: './registration.component.html',
  styleUrls: ['./registration.component.scss'],
})
export class RegistrationComponent {
  isValidContactForm: StepState = 'edit';
  isValidProfilForm: StepState = 'edit';
  isValidSubjectsForm: StepState = 'edit';
  isValidSubjectsLevelForm: StepState = 'edit';

  constructor(
    private fb: FormBuilder,
    private registrationService: TeacherRegistrationServiceService,
  ) {}

  ngOnInit(): void {
    this.subscribeToFormValidation(this.registrationService.teacherContactForm$, 'teacherContactForm');
    this.subscribeToFormValidation(this.registrationService.teacherProfilForm$, 'teacherProfilForm');
    this.subscribeToFormValidation(this.registrationService.teacherSubjectsForm$, 'teacherSubjectsForm');
    this.subscribeToFormValidation(this.registrationService.subjectsLevelForm$, 'subjectsLevelForm');
  }

  private subscribeToFormValidation(form$: Observable<FormGroup | null>, formName: string): void {
    form$.subscribe(form => {
      const isValid = form?.valid ? 'done' : 'edit';
      if (formName === 'teacherContactForm') {
        this.isValidContactForm = isValid;
      } else if (formName === 'teacherProfilForm') {
        this.isValidProfilForm = isValid;
      } else if (formName === 'teacherSubjectsForm') {
        this.isValidSubjectsForm = isValid;
      } else if (formName === 'subjectsLevelForm') {
        this.isValidSubjectsLevelForm = isValid;
      }
      console.log(`${formName} isValid:`, isValid);
    });
  }
}
