import { TestBed } from '@angular/core/testing';
import { provideRouter, withComponentInputBinding } from '@angular/router';
import { RouterTestingHarness, RouterTestingModule } from '@angular/router/testing';
import { of } from 'rxjs';

import { SubjectCycleDetailComponent } from './subject-cycle-detail.component';

describe('SubjectCycle Management Detail Component', () => {
  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [SubjectCycleDetailComponent, RouterTestingModule.withRoutes([], { bindToComponentInputs: true })],
      providers: [
        provideRouter(
          [
            {
              path: '**',
              component: SubjectCycleDetailComponent,
              resolve: { subjectCycle: () => of({ id: 123 }) },
            },
          ],
          withComponentInputBinding(),
        ),
      ],
    })
      .overrideTemplate(SubjectCycleDetailComponent, '')
      .compileComponents();
  });

  describe('OnInit', () => {
    it('Should load subjectCycle on init', async () => {
      const harness = await RouterTestingHarness.create();
      const instance = await harness.navigateByUrl('/', SubjectCycleDetailComponent);

      // THEN
      expect(instance.subjectCycle).toEqual(expect.objectContaining({ id: 123 }));
    });
  });
});
