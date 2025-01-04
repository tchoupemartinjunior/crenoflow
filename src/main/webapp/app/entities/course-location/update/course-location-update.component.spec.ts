import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of, Subject, from } from 'rxjs';

import { IPerson } from 'app/entities/person/person.model';
import { PersonService } from 'app/entities/person/service/person.service';
import { CourseLocationService } from '../service/course-location.service';
import { ICourseLocation } from '../course-location.model';
import { CourseLocationFormService } from './course-location-form.service';

import { CourseLocationUpdateComponent } from './course-location-update.component';

describe('CourseLocation Management Update Component', () => {
  let comp: CourseLocationUpdateComponent;
  let fixture: ComponentFixture<CourseLocationUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let courseLocationFormService: CourseLocationFormService;
  let courseLocationService: CourseLocationService;
  let personService: PersonService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([]), CourseLocationUpdateComponent],
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
      .overrideTemplate(CourseLocationUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(CourseLocationUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    courseLocationFormService = TestBed.inject(CourseLocationFormService);
    courseLocationService = TestBed.inject(CourseLocationService);
    personService = TestBed.inject(PersonService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should call Person query and add missing value', () => {
      const courseLocation: ICourseLocation = { id: 456 };
      const manager: IPerson = { id: 30213 };
      courseLocation.manager = manager;

      const personCollection: IPerson[] = [{ id: 30173 }];
      jest.spyOn(personService, 'query').mockReturnValue(of(new HttpResponse({ body: personCollection })));
      const additionalPeople = [manager];
      const expectedCollection: IPerson[] = [...additionalPeople, ...personCollection];
      jest.spyOn(personService, 'addPersonToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ courseLocation });
      comp.ngOnInit();

      expect(personService.query).toHaveBeenCalled();
      expect(personService.addPersonToCollectionIfMissing).toHaveBeenCalledWith(
        personCollection,
        ...additionalPeople.map(expect.objectContaining),
      );
      expect(comp.peopleSharedCollection).toEqual(expectedCollection);
    });

    it('Should update editForm', () => {
      const courseLocation: ICourseLocation = { id: 456 };
      const manager: IPerson = { id: 1876 };
      courseLocation.manager = manager;

      activatedRoute.data = of({ courseLocation });
      comp.ngOnInit();

      expect(comp.peopleSharedCollection).toContain(manager);
      expect(comp.courseLocation).toEqual(courseLocation);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<ICourseLocation>>();
      const courseLocation = { id: 123 };
      jest.spyOn(courseLocationFormService, 'getCourseLocation').mockReturnValue(courseLocation);
      jest.spyOn(courseLocationService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ courseLocation });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: courseLocation }));
      saveSubject.complete();

      // THEN
      expect(courseLocationFormService.getCourseLocation).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(courseLocationService.update).toHaveBeenCalledWith(expect.objectContaining(courseLocation));
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<ICourseLocation>>();
      const courseLocation = { id: 123 };
      jest.spyOn(courseLocationFormService, 'getCourseLocation').mockReturnValue({ id: null });
      jest.spyOn(courseLocationService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ courseLocation: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: courseLocation }));
      saveSubject.complete();

      // THEN
      expect(courseLocationFormService.getCourseLocation).toHaveBeenCalled();
      expect(courseLocationService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<ICourseLocation>>();
      const courseLocation = { id: 123 };
      jest.spyOn(courseLocationService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ courseLocation });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(courseLocationService.update).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });

  describe('Compare relationships', () => {
    describe('comparePerson', () => {
      it('Should forward to personService', () => {
        const entity = { id: 123 };
        const entity2 = { id: 456 };
        jest.spyOn(personService, 'comparePerson');
        comp.comparePerson(entity, entity2);
        expect(personService.comparePerson).toHaveBeenCalledWith(entity, entity2);
      });
    });
  });
});
