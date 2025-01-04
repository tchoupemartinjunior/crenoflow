import { TestBed } from '@angular/core/testing';
import { provideRouter, withComponentInputBinding } from '@angular/router';
import { RouterTestingHarness, RouterTestingModule } from '@angular/router/testing';
import { of } from 'rxjs';

import { SpecialityDetailComponent } from './speciality-detail.component';

describe('Speciality Management Detail Component', () => {
  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [SpecialityDetailComponent, RouterTestingModule.withRoutes([], { bindToComponentInputs: true })],
      providers: [
        provideRouter(
          [
            {
              path: '**',
              component: SpecialityDetailComponent,
              resolve: { speciality: () => of({ id: 123 }) },
            },
          ],
          withComponentInputBinding(),
        ),
      ],
    })
      .overrideTemplate(SpecialityDetailComponent, '')
      .compileComponents();
  });

  describe('OnInit', () => {
    it('Should load speciality on init', async () => {
      const harness = await RouterTestingHarness.create();
      const instance = await harness.navigateByUrl('/', SpecialityDetailComponent);

      // THEN
      expect(instance.speciality).toEqual(expect.objectContaining({ id: 123 }));
    });
  });
});
