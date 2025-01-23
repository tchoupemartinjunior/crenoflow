import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { TeacherRoutingModule } from './teacher-routing.module';
import { RegistrationComponent } from './registration/registration.component';
import SharedModule from '../shared/shared.module';
import { CdkStepperModule } from '@angular/cdk/stepper';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { ManagementComponent } from './management/management.component';
import { CalendarModule, DateAdapter } from 'angular-calendar';
import { TeachersPlanningComponent } from './teachers-planning/teachers-planning.component';
import { adapterFactory } from 'angular-calendar/date-adapters/date-fns';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';
@NgModule({
  declarations: [RegistrationComponent, ManagementComponent, TeachersPlanningComponent],
  imports: [
    CommonModule,
    TeacherRoutingModule,
    SharedModule,
    CdkStepperModule,
    ReactiveFormsModule,
    FormsModule,
    BrowserAnimationsModule,
    CalendarModule.forRoot({
      provide: DateAdapter,
      useFactory: adapterFactory,
    }),
  ],
})
export class TeacherModule {}
