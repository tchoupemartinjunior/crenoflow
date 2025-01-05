import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { TeacherRoutingModule } from './teacher-routing.module';
import { RegistrationComponent } from './registration/registration.component';

@NgModule({
  declarations: [RegistrationComponent],
  imports: [CommonModule, TeacherRoutingModule],
})
export class TeacherModule {}
