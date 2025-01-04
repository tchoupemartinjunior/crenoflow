import { TestBed } from '@angular/core/testing';
import { provideRouter, withComponentInputBinding } from '@angular/router';
import { RouterTestingHarness, RouterTestingModule } from '@angular/router/testing';
import { of } from 'rxjs';

import { SchoolLevelDetailComponent } from './school-level-detail.component';

describe('SchoolLevel Management Detail Component', () => {
  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [SchoolLevelDetailComponent, RouterTestingModule.withRoutes([], { bindToComponentInputs: true })],
      providers: [
        provideRouter(
          [
            {
              path: '**',
              component: SchoolLevelDetailComponent,
              resolve: { schoolLevel: () => of({ id: 123 }) },
            },
          ],
          withComponentInputBinding(),
        ),
      ],
    })
      .overrideTemplate(SchoolLevelDetailComponent, '')
      .compileComponents();
  });

  describe('OnInit', () => {
    it('Should load schoolLevel on init', async () => {
      const harness = await RouterTestingHarness.create();
      const instance = await harness.navigateByUrl('/', SchoolLevelDetailComponent);

      // THEN
      expect(instance.schoolLevel).toEqual(expect.objectContaining({ id: 123 }));
    });
  });
});
