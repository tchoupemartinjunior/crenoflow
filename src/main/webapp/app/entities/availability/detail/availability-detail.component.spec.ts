import { TestBed } from '@angular/core/testing';
import { provideRouter, withComponentInputBinding } from '@angular/router';
import { RouterTestingHarness, RouterTestingModule } from '@angular/router/testing';
import { of } from 'rxjs';

import { AvailabilityDetailComponent } from './availability-detail.component';

describe('Availability Management Detail Component', () => {
  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [AvailabilityDetailComponent, RouterTestingModule.withRoutes([], { bindToComponentInputs: true })],
      providers: [
        provideRouter(
          [
            {
              path: '**',
              component: AvailabilityDetailComponent,
              resolve: { availability: () => of({ id: 123 }) },
            },
          ],
          withComponentInputBinding(),
        ),
      ],
    })
      .overrideTemplate(AvailabilityDetailComponent, '')
      .compileComponents();
  });

  describe('OnInit', () => {
    it('Should load availability on init', async () => {
      const harness = await RouterTestingHarness.create();
      const instance = await harness.navigateByUrl('/', AvailabilityDetailComponent);

      // THEN
      expect(instance.availability).toEqual(expect.objectContaining({ id: 123 }));
    });
  });
});
