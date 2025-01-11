import { Component } from '@angular/core';
import { FormGroup, FormBuilder, FormArray } from '@angular/forms';
import { CycleService } from '../../../entities/cycle/service/cycle.service';
import { ICycle } from '../../../entities/cycle/cycle.model';

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
  ) {
    this.cyclesForm = fb.group({
      selectedCycles: new FormArray([]),
    });
  }

  ngOnInit(): void {
    this.cycleService.query().subscribe(response => {
      this.cycles = response?.body || [];
    });
  }
  onCheckboxChange(event: Event, cycle: ICycle): void {
    const isChecked = (event.target as HTMLInputElement).checked;
    if (isChecked) {
      this.selectedCycles.push(this.fb.control(cycle));
    } else {
      const index = this.selectedCycles.controls.findIndex(ctrl => ctrl.value.id === cycle.id);
      if (index !== -1) {
        this.selectedCycles.removeAt(index);
      }
    }
    console.log(this.selectedCycles);
  }

  get selectedCycles(): FormArray {
    return this.cyclesForm.get('selectedCycles') as FormArray;
  }
}
