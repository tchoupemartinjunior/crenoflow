import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';

@NgModule({
  imports: [
    RouterModule.forChild([
      {
        path: 'person',
        data: { pageTitle: 'crenoFlowApp.person.home.title' },
        loadChildren: () => import('./person/person.routes'),
      },
      {
        path: 'teacher',
        data: { pageTitle: 'crenoFlowApp.teacher.home.title' },
        loadChildren: () => import('./teacher/teacher.routes'),
      },
      {
        path: 'profession',
        data: { pageTitle: 'crenoFlowApp.profession.home.title' },
        loadChildren: () => import('./profession/profession.routes'),
      },
      {
        path: 'speciality',
        data: { pageTitle: 'crenoFlowApp.speciality.home.title' },
        loadChildren: () => import('./speciality/speciality.routes'),
      },
      {
        path: 'education-level',
        data: { pageTitle: 'crenoFlowApp.educationLevel.home.title' },
        loadChildren: () => import('./education-level/education-level.routes'),
      },
      {
        path: 'photo-person',
        data: { pageTitle: 'crenoFlowApp.photoPerson.home.title' },
        loadChildren: () => import('./photo-person/photo-person.routes'),
      },
      {
        path: 'subject',
        data: { pageTitle: 'crenoFlowApp.subject.home.title' },
        loadChildren: () => import('./subject/subject.routes'),
      },
      {
        path: 'school-level',
        data: { pageTitle: 'crenoFlowApp.schoolLevel.home.title' },
        loadChildren: () => import('./school-level/school-level.routes'),
      },
      {
        path: 'cycle',
        data: { pageTitle: 'crenoFlowApp.cycle.home.title' },
        loadChildren: () => import('./cycle/cycle.routes'),
      },
      {
        path: 'availability',
        data: { pageTitle: 'crenoFlowApp.availability.home.title' },
        loadChildren: () => import('./availability/availability.routes'),
      },
      {
        path: 'subject-cycle',
        data: { pageTitle: 'crenoFlowApp.subjectCycle.home.title' },
        loadChildren: () => import('./subject-cycle/subject-cycle.routes'),
      },
      {
        path: 'course',
        data: { pageTitle: 'crenoFlowApp.course.home.title' },
        loadChildren: () => import('./course/course.routes'),
      },
      {
        path: 'booking',
        data: { pageTitle: 'crenoFlowApp.booking.home.title' },
        loadChildren: () => import('./booking/booking.routes'),
      },
      {
        path: 'course-location',
        data: { pageTitle: 'crenoFlowApp.courseLocation.home.title' },
        loadChildren: () => import('./course-location/course-location.routes'),
      },
      /* jhipster-needle-add-entity-route - JHipster will add entity modules routes here */
    ]),
  ],
})
export class EntityRoutingModule {}
