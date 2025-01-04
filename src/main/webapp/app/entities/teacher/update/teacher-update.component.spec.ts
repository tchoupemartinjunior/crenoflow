import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of, Subject, from } from 'rxjs';

import { IPerson } from 'app/entities/person/person.model';
import { PersonService } from 'app/entities/person/service/person.service';
import { IEducationLevel } from 'app/entities/education-level/education-level.model';
import { EducationLevelService } from 'app/entities/education-level/service/education-level.service';
import { IProfession } from 'app/entities/profession/profession.model';
import { ProfessionService } from 'app/entities/profession/service/profession.service';
import { ISpeciality } from 'app/entities/speciality/speciality.model';
import { SpecialityService } from 'app/entities/speciality/service/speciality.service';
import { ISubjectCycle } from 'app/entities/subject-cycle/subject-cycle.model';
import { SubjectCycleService } from 'app/entities/subject-cycle/service/subject-cycle.service';
import { ITeacher } from '../teacher.model';
import { TeacherService } from '../service/teacher.service';
import { TeacherFormService } from './teacher-form.service';

import { TeacherUpdateComponent } from './teacher-update.component';

describe('Teacher Management Update Component', () => {
  let comp: TeacherUpdateComponent;
  let fixture: ComponentFixture<TeacherUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let teacherFormService: TeacherFormService;
  let teacherService: TeacherService;
  let personService: PersonService;
  let educationLevelService: EducationLevelService;
  let professionService: ProfessionService;
  let specialityService: SpecialityService;
  let subjectCycleService: SubjectCycleService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([]), TeacherUpdateComponent],
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
      .overrideTemplate(TeacherUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(TeacherUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    teacherFormService = TestBed.inject(TeacherFormService);
    teacherService = TestBed.inject(TeacherService);
    personService = TestBed.inject(PersonService);
    educationLevelService = TestBed.inject(EducationLevelService);
    professionService = TestBed.inject(ProfessionService);
    specialityService = TestBed.inject(SpecialityService);
    subjectCycleService = TestBed.inject(SubjectCycleService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should call person query and add missing value', () => {
      const teacher: ITeacher = { id: 456 };
      const person: IPerson = { id: 6384 };
      teacher.person = person;

      const personCollection: IPerson[] = [{ id: 20061 }];
      jest.spyOn(personService, 'query').mockReturnValue(of(new HttpResponse({ body: personCollection })));
      const expectedCollection: IPerson[] = [person, ...personCollection];
      jest.spyOn(personService, 'addPersonToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ teacher });
      comp.ngOnInit();

      expect(personService.query).toHaveBeenCalled();
      expect(personService.addPersonToCollectionIfMissing).toHaveBeenCalledWith(personCollection, person);
      expect(comp.peopleCollection).toEqual(expectedCollection);
    });

    it('Should call EducationLevel query and add missing value', () => {
      const teacher: ITeacher = { id: 456 };
      const educationLevel: IEducationLevel = { id: 16965 };
      teacher.educationLevel = educationLevel;

      const educationLevelCollection: IEducationLevel[] = [{ id: 18842 }];
      jest.spyOn(educationLevelService, 'query').mockReturnValue(of(new HttpResponse({ body: educationLevelCollection })));
      const additionalEducationLevels = [educationLevel];
      const expectedCollection: IEducationLevel[] = [...additionalEducationLevels, ...educationLevelCollection];
      jest.spyOn(educationLevelService, 'addEducationLevelToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ teacher });
      comp.ngOnInit();

      expect(educationLevelService.query).toHaveBeenCalled();
      expect(educationLevelService.addEducationLevelToCollectionIfMissing).toHaveBeenCalledWith(
        educationLevelCollection,
        ...additionalEducationLevels.map(expect.objectContaining),
      );
      expect(comp.educationLevelsSharedCollection).toEqual(expectedCollection);
    });

    it('Should call Profession query and add missing value', () => {
      const teacher: ITeacher = { id: 456 };
      const profession: IProfession = { id: 28210 };
      teacher.profession = profession;

      const professionCollection: IProfession[] = [{ id: 25154 }];
      jest.spyOn(professionService, 'query').mockReturnValue(of(new HttpResponse({ body: professionCollection })));
      const additionalProfessions = [profession];
      const expectedCollection: IProfession[] = [...additionalProfessions, ...professionCollection];
      jest.spyOn(professionService, 'addProfessionToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ teacher });
      comp.ngOnInit();

      expect(professionService.query).toHaveBeenCalled();
      expect(professionService.addProfessionToCollectionIfMissing).toHaveBeenCalledWith(
        professionCollection,
        ...additionalProfessions.map(expect.objectContaining),
      );
      expect(comp.professionsSharedCollection).toEqual(expectedCollection);
    });

    it('Should call Speciality query and add missing value', () => {
      const teacher: ITeacher = { id: 456 };
      const speciality: ISpeciality = { id: 16918 };
      teacher.speciality = speciality;

      const specialityCollection: ISpeciality[] = [{ id: 2871 }];
      jest.spyOn(specialityService, 'query').mockReturnValue(of(new HttpResponse({ body: specialityCollection })));
      const additionalSpecialities = [speciality];
      const expectedCollection: ISpeciality[] = [...additionalSpecialities, ...specialityCollection];
      jest.spyOn(specialityService, 'addSpecialityToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ teacher });
      comp.ngOnInit();

      expect(specialityService.query).toHaveBeenCalled();
      expect(specialityService.addSpecialityToCollectionIfMissing).toHaveBeenCalledWith(
        specialityCollection,
        ...additionalSpecialities.map(expect.objectContaining),
      );
      expect(comp.specialitiesSharedCollection).toEqual(expectedCollection);
    });

    it('Should call SubjectCycle query and add missing value', () => {
      const teacher: ITeacher = { id: 456 };
      const subjectCycles: ISubjectCycle[] = [{ id: 13690 }];
      teacher.subjectCycles = subjectCycles;

      const subjectCycleCollection: ISubjectCycle[] = [{ id: 21294 }];
      jest.spyOn(subjectCycleService, 'query').mockReturnValue(of(new HttpResponse({ body: subjectCycleCollection })));
      const additionalSubjectCycles = [...subjectCycles];
      const expectedCollection: ISubjectCycle[] = [...additionalSubjectCycles, ...subjectCycleCollection];
      jest.spyOn(subjectCycleService, 'addSubjectCycleToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ teacher });
      comp.ngOnInit();

      expect(subjectCycleService.query).toHaveBeenCalled();
      expect(subjectCycleService.addSubjectCycleToCollectionIfMissing).toHaveBeenCalledWith(
        subjectCycleCollection,
        ...additionalSubjectCycles.map(expect.objectContaining),
      );
      expect(comp.subjectCyclesSharedCollection).toEqual(expectedCollection);
    });

    it('Should update editForm', () => {
      const teacher: ITeacher = { id: 456 };
      const person: IPerson = { id: 601 };
      teacher.person = person;
      const educationLevel: IEducationLevel = { id: 30838 };
      teacher.educationLevel = educationLevel;
      const profession: IProfession = { id: 13878 };
      teacher.profession = profession;
      const speciality: ISpeciality = { id: 27681 };
      teacher.speciality = speciality;
      const subjectCycle: ISubjectCycle = { id: 22850 };
      teacher.subjectCycles = [subjectCycle];

      activatedRoute.data = of({ teacher });
      comp.ngOnInit();

      expect(comp.peopleCollection).toContain(person);
      expect(comp.educationLevelsSharedCollection).toContain(educationLevel);
      expect(comp.professionsSharedCollection).toContain(profession);
      expect(comp.specialitiesSharedCollection).toContain(speciality);
      expect(comp.subjectCyclesSharedCollection).toContain(subjectCycle);
      expect(comp.teacher).toEqual(teacher);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<ITeacher>>();
      const teacher = { id: 123 };
      jest.spyOn(teacherFormService, 'getTeacher').mockReturnValue(teacher);
      jest.spyOn(teacherService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ teacher });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: teacher }));
      saveSubject.complete();

      // THEN
      expect(teacherFormService.getTeacher).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(teacherService.update).toHaveBeenCalledWith(expect.objectContaining(teacher));
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<ITeacher>>();
      const teacher = { id: 123 };
      jest.spyOn(teacherFormService, 'getTeacher').mockReturnValue({ id: null });
      jest.spyOn(teacherService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ teacher: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: teacher }));
      saveSubject.complete();

      // THEN
      expect(teacherFormService.getTeacher).toHaveBeenCalled();
      expect(teacherService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<ITeacher>>();
      const teacher = { id: 123 };
      jest.spyOn(teacherService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ teacher });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(teacherService.update).toHaveBeenCalled();
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

    describe('compareEducationLevel', () => {
      it('Should forward to educationLevelService', () => {
        const entity = { id: 123 };
        const entity2 = { id: 456 };
        jest.spyOn(educationLevelService, 'compareEducationLevel');
        comp.compareEducationLevel(entity, entity2);
        expect(educationLevelService.compareEducationLevel).toHaveBeenCalledWith(entity, entity2);
      });
    });

    describe('compareProfession', () => {
      it('Should forward to professionService', () => {
        const entity = { id: 123 };
        const entity2 = { id: 456 };
        jest.spyOn(professionService, 'compareProfession');
        comp.compareProfession(entity, entity2);
        expect(professionService.compareProfession).toHaveBeenCalledWith(entity, entity2);
      });
    });

    describe('compareSpeciality', () => {
      it('Should forward to specialityService', () => {
        const entity = { id: 123 };
        const entity2 = { id: 456 };
        jest.spyOn(specialityService, 'compareSpeciality');
        comp.compareSpeciality(entity, entity2);
        expect(specialityService.compareSpeciality).toHaveBeenCalledWith(entity, entity2);
      });
    });

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
