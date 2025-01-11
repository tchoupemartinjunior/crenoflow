import { Component } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { CustomStepperComponent } from '../../shared/custom-stepper/custom-stepper.component';
import TeacherSubjectsFormComponent from '../../shared/shared.module';
import { StepState } from '@angular/cdk/stepper';
import { TeacherRegistrationServiceService } from './teacher-registration-service.service';

@Component({
  selector: 'jhi-registration',
  templateUrl: './registration.component.html',
  styleUrls: ['./registration.component.scss'],
})
export class RegistrationComponent {
  isValid: StepState = 'edit';

  constructor(
    private fb: FormBuilder,
    private registrationService: TeacherRegistrationServiceService,
  ) {}

  ngOnInit(): void {
    this.registrationService.teacherContactForm$.subscribe(form => {
      this.isValid = form?.valid ? 'done' : 'edit';
      console.log('this.isValid ', this.isValid);
    });
  }
}
