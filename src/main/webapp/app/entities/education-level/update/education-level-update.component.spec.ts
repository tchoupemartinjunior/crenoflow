import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of, Subject, from } from 'rxjs';

import { EducationLevelService } from '../service/education-level.service';
import { IEducationLevel } from '../education-level.model';
import { EducationLevelFormService } from './education-level-form.service';

import { EducationLevelUpdateComponent } from './education-level-update.component';

describe('EducationLevel Management Update Component', () => {
  let comp: EducationLevelUpdateComponent;
  let fixture: ComponentFixture<EducationLevelUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let educationLevelFormService: EducationLevelFormService;
  let educationLevelService: EducationLevelService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([]), EducationLevelUpdateComponent],
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
      .overrideTemplate(EducationLevelUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(EducationLevelUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    educationLevelFormService = TestBed.inject(EducationLevelFormService);
    educationLevelService = TestBed.inject(EducationLevelService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should update editForm', () => {
      const educationLevel: IEducationLevel = { id: 456 };

      activatedRoute.data = of({ educationLevel });
      comp.ngOnInit();

      expect(comp.educationLevel).toEqual(educationLevel);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IEducationLevel>>();
      const educationLevel = { id: 123 };
      jest.spyOn(educationLevelFormService, 'getEducationLevel').mockReturnValue(educationLevel);
      jest.spyOn(educationLevelService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ educationLevel });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: educationLevel }));
      saveSubject.complete();

      // THEN
      expect(educationLevelFormService.getEducationLevel).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(educationLevelService.update).toHaveBeenCalledWith(expect.objectContaining(educationLevel));
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IEducationLevel>>();
      const educationLevel = { id: 123 };
      jest.spyOn(educationLevelFormService, 'getEducationLevel').mockReturnValue({ id: null });
      jest.spyOn(educationLevelService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ educationLevel: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: educationLevel }));
      saveSubject.complete();

      // THEN
      expect(educationLevelFormService.getEducationLevel).toHaveBeenCalled();
      expect(educationLevelService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IEducationLevel>>();
      const educationLevel = { id: 123 };
      jest.spyOn(educationLevelService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ educationLevel });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(educationLevelService.update).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });
});
