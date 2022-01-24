import {Component, ElementRef, OnInit, ViewChild} from '@angular/core';
import {AbstractControl, FormBuilder, FormGroup, Validators} from '@angular/forms';
import {AuthService} from '../../service/auth.service';
import {RxwebValidators} from '@rxweb/reactive-form-validators';
import ValidationService from '../../service/validation.service';
import {ActivatedRoute, Params} from '@angular/router';

@Component({
  selector: 'app-signup',
  templateUrl: './signup.component.html',
  styleUrls: ['./signup.component.scss']
})
export class SignupComponent implements OnInit {

  form!: FormGroup;
  submitted = false;
  isSuccessful = false;
  isSignUpFailed = false;
  errorMessage = '';
  @ViewChild('img') imgInput: ElementRef;
  confirm = null;
  private max: Date;

  constructor(private authService: AuthService, private formBuilder: FormBuilder, private route: ActivatedRoute) {
  }

  ngOnInit(): void {
    this.max = new Date();
    this.max.setFullYear(this.max.getFullYear() - 12);
    this.route.params.subscribe((params: Params) => this.confirm = params.confirm);
    this.form = this.formBuilder.group(
      {
        avatar: ['', [RxwebValidators.image({minHeight: 200, minWidth: 200, maxHeight: 600, maxWidth: 600}),
          RxwebValidators.extension({extensions: ['jpeg', 'png', 'jpg']}),
          RxwebValidators.fileSize({maxSize: 15000000}),
          RxwebValidators.file({maxFiles: 1})]],
        firstname: ['', [Validators.required, Validators.minLength(3), Validators.maxLength(50)]],
        lastname: ['', [Validators.required, Validators.minLength(3), Validators.maxLength(50)]],
        username: ['', [Validators.required, Validators.minLength(3), Validators.maxLength(50)]],
        email: ['', [Validators.required, Validators.email]],
        password: ['', [Validators.required, Validators.minLength(6),
          Validators.pattern('^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z]).{6,}$')]],
        confirmPassword: ['', [Validators.required]],
        dateOfBirth: ['', [Validators.required, ValidationService.dateValidator(new Date(1920, 1, 1), this.max)]],
        acceptTerms: ['', Validators.requiredTrue]
      },
      {
        validators: [ValidationService.match('password', 'confirmPassword')]
      }
    );
  }

  get f(): { [key: string]: AbstractControl } {
    return this.form.controls;
  }

  onSubmit(): void {
    this.submitted = true;
    if (this.form.invalid || this.form.controls.firstname.pristine || this.form.controls.lastname.pristine ||
      this.form.controls.username.pristine || this.form.controls.email.pristine ||
      this.form.controls.password.pristine || this.form.controls.dateOfBirth.pristine ||
      this.form.controls.confirmPassword.pristine || this.form.controls.acceptTerms.pristine) {
      return;
    } else {
      this.authService.register(this.form.controls.avatar.value, this.form.controls.firstname.value,
        this.form.controls.lastname.value, this.form.controls.username.value,
        this.form.controls.email.value, this.form.controls.password.value, this.form.controls.dateOfBirth.value).subscribe(
        data => {
          this.isSuccessful = true;
          this.isSignUpFailed = false;
          this.onReset();
        },
        err => {
          this.errorMessage = err.error.message;
          this.isSignUpFailed = true;
          this.onReset();
        }
      );
    }
  }

  onReset(): void {
    this.submitted = false;
    this.form.reset();
    this.imgInput.nativeElement.value = '';
    this.confirm = null;
  }

  public change(img: any) {
    if (img.files && img.files[0]) {
      const reader = new FileReader();
      const setImg = () => this.form.controls.avatar.setValue(reader.result);
      reader.addEventListener('load', setImg.bind(this), false);

      reader.readAsDataURL(img.files[0]);
    }
  }
}
