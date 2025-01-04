import { TestBed } from '@angular/core/testing';
import { provideRouter, withComponentInputBinding } from '@angular/router';
import { RouterTestingHarness, RouterTestingModule } from '@angular/router/testing';
import { of } from 'rxjs';

import { EducationLevelDetailComponent } from './education-level-detail.component';

describe('EducationLevel Management Detail Component', () => {
  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [EducationLevelDetailComponent, RouterTestingModule.withRoutes([], { bindToComponentInputs: true })],
      providers: [
        provideRouter(
          [
            {
              path: '**',
              component: EducationLevelDetailComponent,
              resolve: { educationLevel: () => of({ id: 123 }) },
            },
          ],
          withComponentInputBinding(),
        ),
      ],
    })
      .overrideTemplate(EducationLevelDetailComponent, '')
      .compileComponents();
  });

  describe('OnInit', () => {
    it('Should load educationLevel on init', async () => {
      const harness = await RouterTestingHarness.create();
      const instance = await harness.navigateByUrl('/', EducationLevelDetailComponent);

      // THEN
      expect(instance.educationLevel).toEqual(expect.objectContaining({ id: 123 }));
    });
  });
});
