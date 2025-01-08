import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { TeacherRoutingModule } from './teacher-routing.module';
import { RegistrationComponent } from './registration/registration.component';
import SharedModule from '../shared/shared.module';
import { CdkStepperModule } from '@angular/cdk/stepper';
import { ReactiveFormsModule } from '@angular/forms';

@NgModule({
  declarations: [RegistrationComponent],
  imports: [CommonModule, TeacherRoutingModule, SharedModule, CdkStepperModule, ReactiveFormsModule],
})
export class TeacherModule {}
