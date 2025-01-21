import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { TeacherRoutingModule } from './teacher-routing.module';
import { RegistrationComponent } from './registration/registration.component';
import SharedModule from '../shared/shared.module';
import { CdkStepperModule } from '@angular/cdk/stepper';
import { ReactiveFormsModule } from '@angular/forms';
import { ManagementComponent } from './management/management.component';

@NgModule({
  declarations: [RegistrationComponent, ManagementComponent],
  imports: [CommonModule, TeacherRoutingModule, SharedModule, CdkStepperModule, ReactiveFormsModule],
})
export class TeacherModule {}
