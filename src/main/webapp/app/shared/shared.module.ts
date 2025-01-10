import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { NgbModule } from '@ng-bootstrap/ng-bootstrap';
import { FontAwesomeModule } from '@fortawesome/angular-fontawesome';
import { TranslateModule } from '@ngx-translate/core';

import FindLanguageFromKeyPipe from './language/find-language-from-key.pipe';
import TranslateDirective from './language/translate.directive';
import { AlertComponent } from './alert/alert.component';
import { AlertErrorComponent } from './alert/alert-error.component';
import { CustomStepperComponent } from './custom-stepper/custom-stepper.component';
import { CdkStepper } from '@angular/cdk/stepper';
import { TeacherContactFormComponent } from './teacherForms/teacher-contact-form/teacher-contact-form.component';
import { TeacherProfilFormComponent } from './teacherForms/teacher-profil-form/teacher-profil-form.component';
import { ReactiveFormsModule } from '@angular/forms';
import { TeacherSubjectsFormComponent } from './teacherForms/teacher-subjects-form/teacher-subjects-form.component';

/**
 * Application wide Module
 */
@NgModule({
  imports: [
    AlertComponent,
    AlertErrorComponent,
    FindLanguageFromKeyPipe,
    TranslateDirective,
    CommonModule,
    ReactiveFormsModule,
    FontAwesomeModule,
  ],
  exports: [
    CommonModule,
    NgbModule,
    FontAwesomeModule,
    AlertComponent,
    AlertErrorComponent,
    TranslateModule,
    FindLanguageFromKeyPipe,
    TranslateDirective,
    CustomStepperComponent,
    TeacherContactFormComponent,
    TeacherProfilFormComponent,
    TeacherSubjectsFormComponent,
  ],
  declarations: [CustomStepperComponent, TeacherContactFormComponent, TeacherProfilFormComponent, TeacherSubjectsFormComponent],
})
export default class SharedModule {}
