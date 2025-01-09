import { CdkStepper } from '@angular/cdk/stepper';
import { Component, Input } from '@angular/core';

@Component({
  selector: 'jhi-custom-stepper',
  templateUrl: './custom-stepper.component.html',
  styleUrls: ['./custom-stepper.component.scss'],
  providers: [{ provide: CdkStepper, useExisting: CustomStepperComponent }],
})
export class CustomStepperComponent extends CdkStepper {
  @Input() linearModeSelected = true;

  onClick(index: number): void {
    this.selectedIndex = index;
  }
  nextStep() {
    if (this.selectedIndex < this.steps.length - 1) {
      this.selectedIndex++;
    }
  }

  previousStep() {
    if (this.selectedIndex > 0) {
      this.selectedIndex--;
    }
  }
}
