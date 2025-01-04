import { TestBed } from '@angular/core/testing';
import { provideRouter, withComponentInputBinding } from '@angular/router';
import { RouterTestingHarness, RouterTestingModule } from '@angular/router/testing';
import { of } from 'rxjs';

import { CycleDetailComponent } from './cycle-detail.component';

describe('Cycle Management Detail Component', () => {
  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [CycleDetailComponent, RouterTestingModule.withRoutes([], { bindToComponentInputs: true })],
      providers: [
        provideRouter(
          [
            {
              path: '**',
              component: CycleDetailComponent,
              resolve: { cycle: () => of({ id: 123 }) },
            },
          ],
          withComponentInputBinding(),
        ),
      ],
    })
      .overrideTemplate(CycleDetailComponent, '')
      .compileComponents();
  });

  describe('OnInit', () => {
    it('Should load cycle on init', async () => {
      const harness = await RouterTestingHarness.create();
      const instance = await harness.navigateByUrl('/', CycleDetailComponent);

      // THEN
      expect(instance.cycle).toEqual(expect.objectContaining({ id: 123 }));
    });
  });
});
