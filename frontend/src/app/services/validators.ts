import { AbstractControl, ValidationErrors, ValidatorFn, AsyncValidatorFn } from '@angular/forms';

export class CustomValidators {
  /**
   * Validates that a field is not empty or whitespace only
   */
  static required(control: AbstractControl): ValidationErrors | null {
    if (!control.value || (typeof control.value === 'string' && !control.value.trim())) {
      return { required: true };
    }
    return null;
  }

  /**
   * Validates minimum length
   */
  static minLength(min: number): ValidatorFn {
    return (control: AbstractControl): ValidationErrors | null => {
      if (!control.value) return null;
      if (control.value.length < min) {
        return { minlength: { requiredLength: min, actualLength: control.value.length } };
      }
      return null;
    };
  }

  /**
   * Validates maximum length
   */
  static maxLength(max: number): ValidatorFn {
    return (control: AbstractControl): ValidationErrors | null => {
      if (!control.value) return null;
      if (control.value.length > max) {
        return { maxlength: { requiredLength: max, actualLength: control.value.length } };
      }
      return null;
    };
  }

  /**
   * Validates a number is greater than a minimum value
   */
  static minValue(min: number): ValidatorFn {
    return (control: AbstractControl): ValidationErrors | null => {
      if (!control.value) return null;
      const value = Number(control.value);
      if (isNaN(value) || value < min) {
        return { minvalue: { min, actual: value } };
      }
      return null;
    };
  }

  /**
   * Validates a number is less than a maximum value
   */
  static maxValue(max: number): ValidatorFn {
    return (control: AbstractControl): ValidationErrors | null => {
      if (!control.value) return null;
      const value = Number(control.value);
      if (isNaN(value) || value > max) {
        return { maxvalue: { max, actual: value } };
      }
      return null;
    };
  }

  /**
   * Validates date format (YYYY-MM-DD)
   */
  static dateFormat(control: AbstractControl): ValidationErrors | null {
    if (!control.value) return null;
    const dateRegex = /^\d{4}-\d{2}-\d{2}$/;
    if (!dateRegex.test(control.value)) {
      return { dateformat: true };
    }
    const date = new Date(control.value);
    if (isNaN(date.getTime())) {
      return { dateformat: true };
    }
    return null;
  }

  /**
   * Validates that a date is not in the past
   */
  static futureDateValidator(control: AbstractControl): ValidationErrors | null {
    if (!control.value) return null;
    const selectedDate = new Date(control.value);
    const today = new Date();
    today.setHours(0, 0, 0, 0);
    if (selectedDate < today) {
      return { pastdate: true };
    }
    return null;
  }

  /**
   * Validates that a date is not in the future
   */
  static pastDateValidator(control: AbstractControl): ValidationErrors | null {
    if (!control.value) return null;
    const selectedDate = new Date(control.value);
    const today = new Date();
    today.setHours(23, 59, 59, 999);
    if (selectedDate > today) {
      return { futuredate: true };
    }
    return null;
  }

  /**
   * Validates number format
   */
  static numeric(control: AbstractControl): ValidationErrors | null {
    if (!control.value) return null;
    const value = Number(control.value);
    if (isNaN(value)) {
      return { numeric: true };
    }
    return null;
  }

  /**
   * Validates email format
   */
  static email(control: AbstractControl): ValidationErrors | null {
    if (!control.value) return null;
    const emailRegex = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;
    if (!emailRegex.test(control.value)) {
      return { email: true };
    }
    return null;
  }

  /**
   * Validates URL format
   */
  static url(control: AbstractControl): ValidationErrors | null {
    if (!control.value) return null;
    try {
      new URL(control.value);
      return null;
    } catch {
      return { url: true };
    }
  }

  /**
   * Validates that two fields match
   */
  static match(fieldName: string): ValidatorFn {
    return (control: AbstractControl): ValidationErrors | null => {
      if (!control.parent) return null;
      const matchControl = control.parent.get(fieldName);
      if (!matchControl) return null;
      if (control.value !== matchControl.value) {
        return { match: true };
      }
      return null;
    };
  }

  /**
   * Pattern validator
   */
  static pattern(regex: RegExp | string): ValidatorFn {
    return (control: AbstractControl): ValidationErrors | null => {
      if (!control.value) return null;
      const regexObj = typeof regex === 'string' ? new RegExp(regex) : regex;
      if (!regexObj.test(control.value)) {
        return { pattern: true };
      }
      return null;
    };
  }
}
