import {Injectable} from '@angular/core';
import {AbstractControl, ValidatorFn} from '@angular/forms';

@Injectable({
  providedIn: 'root'
})
export default class ValidationService {

  constructor() {
  }

  static match(controlName: string, checkControlName: string): ValidatorFn {
    return (controls: AbstractControl) => {
      const control = controls.get(controlName);
      const checkControl = controls.get(checkControlName);

      // @ts-ignore
      if (checkControl.errors && !checkControl.errors.matching) {
        return null;
      }

      // @ts-ignore
      if (control.value !== checkControl.value) {
        // @ts-ignore
        controls.get(checkControlName).setErrors({matching: true});
        return {matching: true};
      } else {
        return null;
      }
    };
  }

  static dateValidator(min: Date, max: Date): ValidatorFn {
    return (control: AbstractControl): { [key: string]: boolean } | null => {
      const userDate = new Date(control.value);
      if (userDate.getTime() < min.getTime() || userDate.getTime() > max.getTime()) {
        return {dateRange: true};
      }
      return null;
    };
  }

  static future():ValidatorFn {
    return (control: AbstractControl): { [key: string]: boolean } | null => {
      const userDate = new Date(control.value);
      const now = new Date();
      if (now.getTime() > userDate.getTime()) {
        return {future: true};
      }
      return null;
    };
  }
}
