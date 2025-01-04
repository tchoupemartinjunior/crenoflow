import { Injectable } from '@angular/core';
import { FormGroup, FormControl, Validators } from '@angular/forms';

import { IPerson, NewPerson } from '../person.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IPerson for edit and NewPersonFormGroupInput for create.
 */
type PersonFormGroupInput = IPerson | PartialWithRequiredKeyOf<NewPerson>;

type PersonFormDefaults = Pick<NewPerson, 'id'>;

type PersonFormGroupContent = {
  id: FormControl<IPerson['id'] | NewPerson['id']>;
  firstName: FormControl<IPerson['firstName']>;
  lastName: FormControl<IPerson['lastName']>;
  email: FormControl<IPerson['email']>;
  phoneNumber: FormControl<IPerson['phoneNumber']>;
  address: FormControl<IPerson['address']>;
  city: FormControl<IPerson['city']>;
  postalCode: FormControl<IPerson['postalCode']>;
  birthday: FormControl<IPerson['birthday']>;
  user: FormControl<IPerson['user']>;
  schoolLevel: FormControl<IPerson['schoolLevel']>;
};

export type PersonFormGroup = FormGroup<PersonFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class PersonFormService {
  createPersonFormGroup(person: PersonFormGroupInput = { id: null }): PersonFormGroup {
    const personRawValue = {
      ...this.getFormDefaults(),
      ...person,
    };
    return new FormGroup<PersonFormGroupContent>({
      id: new FormControl(
        { value: personRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        },
      ),
      firstName: new FormControl(personRawValue.firstName),
      lastName: new FormControl(personRawValue.lastName),
      email: new FormControl(personRawValue.email),
      phoneNumber: new FormControl(personRawValue.phoneNumber),
      address: new FormControl(personRawValue.address),
      city: new FormControl(personRawValue.city),
      postalCode: new FormControl(personRawValue.postalCode),
      birthday: new FormControl(personRawValue.birthday),
      user: new FormControl(personRawValue.user),
      schoolLevel: new FormControl(personRawValue.schoolLevel),
    });
  }

  getPerson(form: PersonFormGroup): IPerson | NewPerson {
    return form.getRawValue() as IPerson | NewPerson;
  }

  resetForm(form: PersonFormGroup, person: PersonFormGroupInput): void {
    const personRawValue = { ...this.getFormDefaults(), ...person };
    form.reset(
      {
        ...personRawValue,
        id: { value: personRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */,
    );
  }

  private getFormDefaults(): PersonFormDefaults {
    return {
      id: null,
    };
  }
}
