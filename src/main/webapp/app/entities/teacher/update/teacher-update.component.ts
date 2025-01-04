import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import SharedModule from 'app/shared/shared.module';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';

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
import { TeacherService } from '../service/teacher.service';
import { ITeacher } from '../teacher.model';
import { TeacherFormService, TeacherFormGroup } from './teacher-form.service';

@Component({
  standalone: true,
  selector: 'jhi-teacher-update',
  templateUrl: './teacher-update.component.html',
  imports: [SharedModule, FormsModule, ReactiveFormsModule],
})
export class TeacherUpdateComponent implements OnInit {
  isSaving = false;
  teacher: ITeacher | null = null;

  peopleCollection: IPerson[] = [];
  educationLevelsSharedCollection: IEducationLevel[] = [];
  professionsSharedCollection: IProfession[] = [];
  specialitiesSharedCollection: ISpeciality[] = [];
  subjectCyclesSharedCollection: ISubjectCycle[] = [];

  editForm: TeacherFormGroup = this.teacherFormService.createTeacherFormGroup();

  constructor(
    protected teacherService: TeacherService,
    protected teacherFormService: TeacherFormService,
    protected personService: PersonService,
    protected educationLevelService: EducationLevelService,
    protected professionService: ProfessionService,
    protected specialityService: SpecialityService,
    protected subjectCycleService: SubjectCycleService,
    protected activatedRoute: ActivatedRoute,
  ) {}

  comparePerson = (o1: IPerson | null, o2: IPerson | null): boolean => this.personService.comparePerson(o1, o2);

  compareEducationLevel = (o1: IEducationLevel | null, o2: IEducationLevel | null): boolean =>
    this.educationLevelService.compareEducationLevel(o1, o2);

  compareProfession = (o1: IProfession | null, o2: IProfession | null): boolean => this.professionService.compareProfession(o1, o2);

  compareSpeciality = (o1: ISpeciality | null, o2: ISpeciality | null): boolean => this.specialityService.compareSpeciality(o1, o2);

  compareSubjectCycle = (o1: ISubjectCycle | null, o2: ISubjectCycle | null): boolean =>
    this.subjectCycleService.compareSubjectCycle(o1, o2);

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ teacher }) => {
      this.teacher = teacher;
      if (teacher) {
        this.updateForm(teacher);
      }

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const teacher = this.teacherFormService.getTeacher(this.editForm);
    if (teacher.id !== null) {
      this.subscribeToSaveResponse(this.teacherService.update(teacher));
    } else {
      this.subscribeToSaveResponse(this.teacherService.create(teacher));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<ITeacher>>): void {
    result.pipe(finalize(() => this.onSaveFinalize())).subscribe({
      next: () => this.onSaveSuccess(),
      error: () => this.onSaveError(),
    });
  }

  protected onSaveSuccess(): void {
    this.previousState();
  }

  protected onSaveError(): void {
    // Api for inheritance.
  }

  protected onSaveFinalize(): void {
    this.isSaving = false;
  }

  protected updateForm(teacher: ITeacher): void {
    this.teacher = teacher;
    this.teacherFormService.resetForm(this.editForm, teacher);

    this.peopleCollection = this.personService.addPersonToCollectionIfMissing<IPerson>(this.peopleCollection, teacher.person);
    this.educationLevelsSharedCollection = this.educationLevelService.addEducationLevelToCollectionIfMissing<IEducationLevel>(
      this.educationLevelsSharedCollection,
      teacher.educationLevel,
    );
    this.professionsSharedCollection = this.professionService.addProfessionToCollectionIfMissing<IProfession>(
      this.professionsSharedCollection,
      teacher.profession,
    );
    this.specialitiesSharedCollection = this.specialityService.addSpecialityToCollectionIfMissing<ISpeciality>(
      this.specialitiesSharedCollection,
      teacher.speciality,
    );
    this.subjectCyclesSharedCollection = this.subjectCycleService.addSubjectCycleToCollectionIfMissing<ISubjectCycle>(
      this.subjectCyclesSharedCollection,
      ...(teacher.subjectCycles ?? []),
    );
  }

  protected loadRelationshipsOptions(): void {
    this.personService
      .query({ 'teacherId.specified': 'false' })
      .pipe(map((res: HttpResponse<IPerson[]>) => res.body ?? []))
      .pipe(map((people: IPerson[]) => this.personService.addPersonToCollectionIfMissing<IPerson>(people, this.teacher?.person)))
      .subscribe((people: IPerson[]) => (this.peopleCollection = people));

    this.educationLevelService
      .query()
      .pipe(map((res: HttpResponse<IEducationLevel[]>) => res.body ?? []))
      .pipe(
        map((educationLevels: IEducationLevel[]) =>
          this.educationLevelService.addEducationLevelToCollectionIfMissing<IEducationLevel>(educationLevels, this.teacher?.educationLevel),
        ),
      )
      .subscribe((educationLevels: IEducationLevel[]) => (this.educationLevelsSharedCollection = educationLevels));

    this.professionService
      .query()
      .pipe(map((res: HttpResponse<IProfession[]>) => res.body ?? []))
      .pipe(
        map((professions: IProfession[]) =>
          this.professionService.addProfessionToCollectionIfMissing<IProfession>(professions, this.teacher?.profession),
        ),
      )
      .subscribe((professions: IProfession[]) => (this.professionsSharedCollection = professions));

    this.specialityService
      .query()
      .pipe(map((res: HttpResponse<ISpeciality[]>) => res.body ?? []))
      .pipe(
        map((specialities: ISpeciality[]) =>
          this.specialityService.addSpecialityToCollectionIfMissing<ISpeciality>(specialities, this.teacher?.speciality),
        ),
      )
      .subscribe((specialities: ISpeciality[]) => (this.specialitiesSharedCollection = specialities));

    this.subjectCycleService
      .query()
      .pipe(map((res: HttpResponse<ISubjectCycle[]>) => res.body ?? []))
      .pipe(
        map((subjectCycles: ISubjectCycle[]) =>
          this.subjectCycleService.addSubjectCycleToCollectionIfMissing<ISubjectCycle>(
            subjectCycles,
            ...(this.teacher?.subjectCycles ?? []),
          ),
        ),
      )
      .subscribe((subjectCycles: ISubjectCycle[]) => (this.subjectCyclesSharedCollection = subjectCycles));
  }
}
