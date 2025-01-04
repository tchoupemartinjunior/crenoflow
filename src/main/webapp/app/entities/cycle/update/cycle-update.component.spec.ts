import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of, Subject, from } from 'rxjs';

import { ISubjectCycle } from 'app/entities/subject-cycle/subject-cycle.model';
import { SubjectCycleService } from 'app/entities/subject-cycle/service/subject-cycle.service';
import { CycleService } from '../service/cycle.service';
import { ICycle } from '../cycle.model';
import { CycleFormService } from './cycle-form.service';

import { CycleUpdateComponent } from './cycle-update.component';

describe('Cycle Management Update Component', () => {
  let comp: CycleUpdateComponent;
  let fixture: ComponentFixture<CycleUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let cycleFormService: CycleFormService;
  let cycleService: CycleService;
  let subjectCycleService: SubjectCycleService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([]), CycleUpdateComponent],
      providers: [
        FormBuilder,
        {
          provide: ActivatedRoute,
          useValue: {
            params: from([{}]),
          },
        },
      ],
    })
      .overrideTemplate(CycleUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(CycleUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    cycleFormService = TestBed.inject(CycleFormService);
    cycleService = TestBed.inject(CycleService);
    subjectCycleService = TestBed.inject(SubjectCycleService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should call SubjectCycle query and add missing value', () => {
      const cycle: ICycle = { id: 456 };
      const subjectCycles: ISubjectCycle[] = [{ id: 32319 }];
      cycle.subjectCycles = subjectCycles;

      const subjectCycleCollection: ISubjectCycle[] = [{ id: 29231 }];
      jest.spyOn(subjectCycleService, 'query').mockReturnValue(of(new HttpResponse({ body: subjectCycleCollection })));
      const additionalSubjectCycles = [...subjectCycles];
      const expectedCollection: ISubjectCycle[] = [...additionalSubjectCycles, ...subjectCycleCollection];
      jest.spyOn(subjectCycleService, 'addSubjectCycleToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ cycle });
      comp.ngOnInit();

      expect(subjectCycleService.query).toHaveBeenCalled();
      expect(subjectCycleService.addSubjectCycleToCollectionIfMissing).toHaveBeenCalledWith(
        subjectCycleCollection,
        ...additionalSubjectCycles.map(expect.objectContaining),
      );
      expect(comp.subjectCyclesSharedCollection).toEqual(expectedCollection);
    });

    it('Should update editForm', () => {
      const cycle: ICycle = { id: 456 };
      const subjectCycle: ISubjectCycle = { id: 25951 };
      cycle.subjectCycles = [subjectCycle];

      activatedRoute.data = of({ cycle });
      comp.ngOnInit();

      expect(comp.subjectCyclesSharedCollection).toContain(subjectCycle);
      expect(comp.cycle).toEqual(cycle);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<ICycle>>();
      const cycle = { id: 123 };
      jest.spyOn(cycleFormService, 'getCycle').mockReturnValue(cycle);
      jest.spyOn(cycleService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ cycle });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: cycle }));
      saveSubject.complete();

      // THEN
      expect(cycleFormService.getCycle).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(cycleService.update).toHaveBeenCalledWith(expect.objectContaining(cycle));
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<ICycle>>();
      const cycle = { id: 123 };
      jest.spyOn(cycleFormService, 'getCycle').mockReturnValue({ id: null });
      jest.spyOn(cycleService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ cycle: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: cycle }));
      saveSubject.complete();

      // THEN
      expect(cycleFormService.getCycle).toHaveBeenCalled();
      expect(cycleService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<ICycle>>();
      const cycle = { id: 123 };
      jest.spyOn(cycleService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ cycle });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(cycleService.update).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });

  describe('Compare relationships', () => {
    describe('compareSubjectCycle', () => {
      it('Should forward to subjectCycleService', () => {
        const entity = { id: 123 };
        const entity2 = { id: 456 };
        jest.spyOn(subjectCycleService, 'compareSubjectCycle');
        comp.compareSubjectCycle(entity, entity2);
        expect(subjectCycleService.compareSubjectCycle).toHaveBeenCalledWith(entity, entity2);
      });
    });
  });
});
