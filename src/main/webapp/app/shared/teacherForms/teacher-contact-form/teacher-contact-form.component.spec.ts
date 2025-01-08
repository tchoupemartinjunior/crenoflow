import { ComponentFixture, TestBed } from '@angular/core/testing';

import { TeacherContactFormComponent } from './teacher-contact-form.component';

describe('TeacherContactFormComponent', () => {
  let component: TeacherContactFormComponent;
  let fixture: ComponentFixture<TeacherContactFormComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [TeacherContactFormComponent],
    });
    fixture = TestBed.createComponent(TeacherContactFormComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
