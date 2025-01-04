import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of, Subject, from } from 'rxjs';

import { ICycle } from 'app/entities/cycle/cycle.model';
import { CycleService } from 'app/entities/cycle/service/cycle.service';
import { SchoolLevelService } from '../service/school-level.service';
import { ISchoolLevel } from '../school-level.model';
import { SchoolLevelFormService } from './school-level-form.service';

import { SchoolLevelUpdateComponent } from './school-level-update.component';

describe('SchoolLevel Management Update Component', () => {
  let comp: SchoolLevelUpdateComponent;
  let fixture: ComponentFixture<SchoolLevelUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let schoolLevelFormService: SchoolLevelFormService;
  let schoolLevelService: SchoolLevelService;
  let cycleService: CycleService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([]), SchoolLevelUpdateComponent],
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
      .overrideTemplate(SchoolLevelUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(SchoolLevelUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    schoolLevelFormService = TestBed.inject(SchoolLevelFormService);
    schoolLevelService = TestBed.inject(SchoolLevelService);
    cycleService = TestBed.inject(CycleService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should call Cycle query and add missing value', () => {
      const schoolLevel: ISchoolLevel = { id: 456 };
      const cycle: ICycle = { id: 22401 };
      schoolLevel.cycle = cycle;

      const cycleCollection: ICycle[] = [{ id: 22746 }];
      jest.spyOn(cycleService, 'query').mockReturnValue(of(new HttpResponse({ body: cycleCollection })));
      const additionalCycles = [cycle];
      const expectedCollection: ICycle[] = [...additionalCycles, ...cycleCollection];
      jest.spyOn(cycleService, 'addCycleToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ schoolLevel });
      comp.ngOnInit();

      expect(cycleService.query).toHaveBeenCalled();
      expect(cycleService.addCycleToCollectionIfMissing).toHaveBeenCalledWith(
        cycleCollection,
        ...additionalCycles.map(expect.objectContaining),
      );
      expect(comp.cyclesSharedCollection).toEqual(expectedCollection);
    });

    it('Should update editForm', () => {
      const schoolLevel: ISchoolLevel = { id: 456 };
      const cycle: ICycle = { id: 2248 };
      schoolLevel.cycle = cycle;

      activatedRoute.data = of({ schoolLevel });
      comp.ngOnInit();

      expect(comp.cyclesSharedCollection).toContain(cycle);
      expect(comp.schoolLevel).toEqual(schoolLevel);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<ISchoolLevel>>();
      const schoolLevel = { id: 123 };
      jest.spyOn(schoolLevelFormService, 'getSchoolLevel').mockReturnValue(schoolLevel);
      jest.spyOn(schoolLevelService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ schoolLevel });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: schoolLevel }));
      saveSubject.complete();

      // THEN
      expect(schoolLevelFormService.getSchoolLevel).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(schoolLevelService.update).toHaveBeenCalledWith(expect.objectContaining(schoolLevel));
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<ISchoolLevel>>();
      const schoolLevel = { id: 123 };
      jest.spyOn(schoolLevelFormService, 'getSchoolLevel').mockReturnValue({ id: null });
      jest.spyOn(schoolLevelService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ schoolLevel: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: schoolLevel }));
      saveSubject.complete();

      // THEN
      expect(schoolLevelFormService.getSchoolLevel).toHaveBeenCalled();
      expect(schoolLevelService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<ISchoolLevel>>();
      const schoolLevel = { id: 123 };
      jest.spyOn(schoolLevelService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ schoolLevel });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(schoolLevelService.update).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });

  describe('Compare relationships', () => {
    describe('compareCycle', () => {
      it('Should forward to cycleService', () => {
        const entity = { id: 123 };
        const entity2 = { id: 456 };
        jest.spyOn(cycleService, 'compareCycle');
        comp.compareCycle(entity, entity2);
        expect(cycleService.compareCycle).toHaveBeenCalledWith(entity, entity2);
      });
    });
  });
});
