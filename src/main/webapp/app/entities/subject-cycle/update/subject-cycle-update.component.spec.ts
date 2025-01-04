import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of, Subject, from } from 'rxjs';

import { SubjectCycleService } from '../service/subject-cycle.service';
import { ISubjectCycle } from '../subject-cycle.model';
import { SubjectCycleFormService } from './subject-cycle-form.service';

import { SubjectCycleUpdateComponent } from './subject-cycle-update.component';

describe('SubjectCycle Management Update Component', () => {
  let comp: SubjectCycleUpdateComponent;
  let fixture: ComponentFixture<SubjectCycleUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let subjectCycleFormService: SubjectCycleFormService;
  let subjectCycleService: SubjectCycleService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([]), SubjectCycleUpdateComponent],
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
      .overrideTemplate(SubjectCycleUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(SubjectCycleUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    subjectCycleFormService = TestBed.inject(SubjectCycleFormService);
    subjectCycleService = TestBed.inject(SubjectCycleService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should update editForm', () => {
      const subjectCycle: ISubjectCycle = { id: 456 };

      activatedRoute.data = of({ subjectCycle });
      comp.ngOnInit();

      expect(comp.subjectCycle).toEqual(subjectCycle);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<ISubjectCycle>>();
      const subjectCycle = { id: 123 };
      jest.spyOn(subjectCycleFormService, 'getSubjectCycle').mockReturnValue(subjectCycle);
      jest.spyOn(subjectCycleService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ subjectCycle });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: subjectCycle }));
      saveSubject.complete();

      // THEN
      expect(subjectCycleFormService.getSubjectCycle).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(subjectCycleService.update).toHaveBeenCalledWith(expect.objectContaining(subjectCycle));
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<ISubjectCycle>>();
      const subjectCycle = { id: 123 };
      jest.spyOn(subjectCycleFormService, 'getSubjectCycle').mockReturnValue({ id: null });
      jest.spyOn(subjectCycleService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ subjectCycle: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: subjectCycle }));
      saveSubject.complete();

      // THEN
      expect(subjectCycleFormService.getSubjectCycle).toHaveBeenCalled();
      expect(subjectCycleService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<ISubjectCycle>>();
      const subjectCycle = { id: 123 };
      jest.spyOn(subjectCycleService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ subjectCycle });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(subjectCycleService.update).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });
});
