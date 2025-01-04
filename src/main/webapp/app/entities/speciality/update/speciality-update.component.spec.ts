import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of, Subject, from } from 'rxjs';

import { SpecialityService } from '../service/speciality.service';
import { ISpeciality } from '../speciality.model';
import { SpecialityFormService } from './speciality-form.service';

import { SpecialityUpdateComponent } from './speciality-update.component';

describe('Speciality Management Update Component', () => {
  let comp: SpecialityUpdateComponent;
  let fixture: ComponentFixture<SpecialityUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let specialityFormService: SpecialityFormService;
  let specialityService: SpecialityService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([]), SpecialityUpdateComponent],
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
      .overrideTemplate(SpecialityUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(SpecialityUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    specialityFormService = TestBed.inject(SpecialityFormService);
    specialityService = TestBed.inject(SpecialityService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should update editForm', () => {
      const speciality: ISpeciality = { id: 456 };

      activatedRoute.data = of({ speciality });
      comp.ngOnInit();

      expect(comp.speciality).toEqual(speciality);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<ISpeciality>>();
      const speciality = { id: 123 };
      jest.spyOn(specialityFormService, 'getSpeciality').mockReturnValue(speciality);
      jest.spyOn(specialityService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ speciality });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: speciality }));
      saveSubject.complete();

      // THEN
      expect(specialityFormService.getSpeciality).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(specialityService.update).toHaveBeenCalledWith(expect.objectContaining(speciality));
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<ISpeciality>>();
      const speciality = { id: 123 };
      jest.spyOn(specialityFormService, 'getSpeciality').mockReturnValue({ id: null });
      jest.spyOn(specialityService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ speciality: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: speciality }));
      saveSubject.complete();

      // THEN
      expect(specialityFormService.getSpeciality).toHaveBeenCalled();
      expect(specialityService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<ISpeciality>>();
      const speciality = { id: 123 };
      jest.spyOn(specialityService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ speciality });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(specialityService.update).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });
});
