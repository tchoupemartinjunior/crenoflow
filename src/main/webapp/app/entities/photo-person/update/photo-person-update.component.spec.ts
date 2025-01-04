import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of, Subject, from } from 'rxjs';

import { IPerson } from 'app/entities/person/person.model';
import { PersonService } from 'app/entities/person/service/person.service';
import { PhotoPersonService } from '../service/photo-person.service';
import { IPhotoPerson } from '../photo-person.model';
import { PhotoPersonFormService } from './photo-person-form.service';

import { PhotoPersonUpdateComponent } from './photo-person-update.component';

describe('PhotoPerson Management Update Component', () => {
  let comp: PhotoPersonUpdateComponent;
  let fixture: ComponentFixture<PhotoPersonUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let photoPersonFormService: PhotoPersonFormService;
  let photoPersonService: PhotoPersonService;
  let personService: PersonService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([]), PhotoPersonUpdateComponent],
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
      .overrideTemplate(PhotoPersonUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(PhotoPersonUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    photoPersonFormService = TestBed.inject(PhotoPersonFormService);
    photoPersonService = TestBed.inject(PhotoPersonService);
    personService = TestBed.inject(PersonService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should call person query and add missing value', () => {
      const photoPerson: IPhotoPerson = { id: 456 };
      const person: IPerson = { id: 4517 };
      photoPerson.person = person;

      const personCollection: IPerson[] = [{ id: 32638 }];
      jest.spyOn(personService, 'query').mockReturnValue(of(new HttpResponse({ body: personCollection })));
      const expectedCollection: IPerson[] = [person, ...personCollection];
      jest.spyOn(personService, 'addPersonToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ photoPerson });
      comp.ngOnInit();

      expect(personService.query).toHaveBeenCalled();
      expect(personService.addPersonToCollectionIfMissing).toHaveBeenCalledWith(personCollection, person);
      expect(comp.peopleCollection).toEqual(expectedCollection);
    });

    it('Should update editForm', () => {
      const photoPerson: IPhotoPerson = { id: 456 };
      const person: IPerson = { id: 17566 };
      photoPerson.person = person;

      activatedRoute.data = of({ photoPerson });
      comp.ngOnInit();

      expect(comp.peopleCollection).toContain(person);
      expect(comp.photoPerson).toEqual(photoPerson);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IPhotoPerson>>();
      const photoPerson = { id: 123 };
      jest.spyOn(photoPersonFormService, 'getPhotoPerson').mockReturnValue(photoPerson);
      jest.spyOn(photoPersonService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ photoPerson });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: photoPerson }));
      saveSubject.complete();

      // THEN
      expect(photoPersonFormService.getPhotoPerson).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(photoPersonService.update).toHaveBeenCalledWith(expect.objectContaining(photoPerson));
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IPhotoPerson>>();
      const photoPerson = { id: 123 };
      jest.spyOn(photoPersonFormService, 'getPhotoPerson').mockReturnValue({ id: null });
      jest.spyOn(photoPersonService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ photoPerson: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: photoPerson }));
      saveSubject.complete();

      // THEN
      expect(photoPersonFormService.getPhotoPerson).toHaveBeenCalled();
      expect(photoPersonService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IPhotoPerson>>();
      const photoPerson = { id: 123 };
      jest.spyOn(photoPersonService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ photoPerson });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(photoPersonService.update).toHaveBeenCalled();
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
