import { TestBed } from '@angular/core/testing';
import { provideRouter, withComponentInputBinding } from '@angular/router';
import { RouterTestingHarness, RouterTestingModule } from '@angular/router/testing';
import { of } from 'rxjs';

import { CourseLocationDetailComponent } from './course-location-detail.component';

describe('CourseLocation Management Detail Component', () => {
  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [CourseLocationDetailComponent, RouterTestingModule.withRoutes([], { bindToComponentInputs: true })],
      providers: [
        provideRouter(
          [
            {
              path: '**',
              component: CourseLocationDetailComponent,
              resolve: { courseLocation: () => of({ id: 123 }) },
            },
          ],
          withComponentInputBinding(),
        ),
      ],
    })
      .overrideTemplate(CourseLocationDetailComponent, '')
      .compileComponents();
  });

  describe('OnInit', () => {
    it('Should load courseLocation on init', async () => {
      const harness = await RouterTestingHarness.create();
      const instance = await harness.navigateByUrl('/', CourseLocationDetailComponent);

      // THEN
      expect(instance.courseLocation).toEqual(expect.objectContaining({ id: 123 }));
    });
  });
});
