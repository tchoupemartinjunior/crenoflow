import { TestBed } from '@angular/core/testing';
import { provideRouter, withComponentInputBinding } from '@angular/router';
import { RouterTestingHarness, RouterTestingModule } from '@angular/router/testing';
import { of } from 'rxjs';

import { ProfessionDetailComponent } from './profession-detail.component';

describe('Profession Management Detail Component', () => {
  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [ProfessionDetailComponent, RouterTestingModule.withRoutes([], { bindToComponentInputs: true })],
      providers: [
        provideRouter(
          [
            {
              path: '**',
              component: ProfessionDetailComponent,
              resolve: { profession: () => of({ id: 123 }) },
            },
          ],
          withComponentInputBinding(),
        ),
      ],
    })
      .overrideTemplate(ProfessionDetailComponent, '')
      .compileComponents();
  });

  describe('OnInit', () => {
    it('Should load profession on init', async () => {
      const harness = await RouterTestingHarness.create();
      const instance = await harness.navigateByUrl('/', ProfessionDetailComponent);

      // THEN
      expect(instance.profession).toEqual(expect.objectContaining({ id: 123 }));
    });
  });
});
