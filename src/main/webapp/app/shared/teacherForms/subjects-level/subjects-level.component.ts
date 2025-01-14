import { Component } from '@angular/core';
import { FormGroup, FormBuilder, FormArray, FormControl } from '@angular/forms';
import { CycleService } from '../../../entities/cycle/service/cycle.service';
import { ICycle } from '../../../entities/cycle/cycle.model';
import { TeacherRegistrationServiceService } from 'app/teacher/registration/teacher-registration-service.service';
import { ISubject } from 'app/entities/subject/subject.model';

@Component({
  selector: 'jhi-subjects-level',
  templateUrl: './subjects-level.component.html',
  styleUrls: ['./subjects-level.component.scss'],
})
export class SubjectsLevelComponent {
  cyclesForm: FormGroup;
  cycles: ICycle[] = [];
  matieres = ['matiere1', 'matiere1', 'matiere1', 'matiere1', 'matiere1'];
  constructor(
    private fb: FormBuilder,
    private cycleService: CycleService,
    private registrationService: TeacherRegistrationServiceService,
  ) {
    this.cyclesForm = fb.group({
      subjectsCyclesArray: fb.array([]),
    });
  }

  get subjectsCyclesArray(): FormArray {
    return this.cyclesForm.get('subjectsCyclesArray') as FormArray;
  }
  get subjectsCyclesArrayControls(): FormGroup[] {
    return this.subjectsCyclesArray.controls as FormGroup[];
  }

  initCycleForm(subjects: ISubject[] | null): void {
    console.log('subjects ', subjects);
    this.subjectsCyclesArray.clear();
    if (subjects) {
      subjects.forEach(subject => {
        const subjectCycle = this.fb.group({
          subject: [subject.label],
          selectedCycles: this.fb.array([]),
        });
        this.subjectsCyclesArray.push(subjectCycle);
      });
    }
  }

  ngOnInit(): void {
    this.cycleService.query().subscribe(response => {
      this.cycles = response?.body || [];
    });

    this.registrationService.teacherSubjectsForm$.subscribe(subjects => {
      this.initCycleForm(subjects?.get('selectedSubjects')?.value);
    });
  }
  onCheckboxChange(event: Event, cycle: ICycle, subject: string): void {
    const isChecked = (event.target as HTMLInputElement).checked;

    // Trouvez le FormGroup correspondant au sujet
    const subjectControl = this.subjectsCyclesArrayControls.find(control => control.get('subject')?.value === subject);

    if (subjectControl) {
      const selectedCycles = subjectControl.get('selectedCycles') as FormArray;

      if (isChecked) {
        // Ajoutez le cycle au FormArray si coché
        selectedCycles.push(this.fb.control(cycle));
      } else {
        // Supprimez le cycle du FormArray si décoché
        const index = selectedCycles.controls.findIndex(ctrl => ctrl.value.id === cycle.id);
        if (index !== -1) {
          selectedCycles.removeAt(index);
        }
      }
    }

    console.log('Updated FormArray for subject:', subject, subjectControl);
  }

  get selectedCycles(): FormArray {
    return this.cyclesForm.get('selectedCycles') as FormArray;
  }
}
