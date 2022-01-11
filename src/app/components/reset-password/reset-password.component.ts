import {Component, OnInit} from '@angular/core';
import {AuthService} from '../../service/auth.service';
import {TokenStorageService} from '../../service/token-storage.service';
import {ActivatedRoute, Params, Router} from '@angular/router';
import {AbstractControl, FormBuilder, FormGroup, Validators} from '@angular/forms';
import ValidationService from '../../service/validation.service';

@Component({
  selector: 'app-reset-password',
  templateUrl: './reset-password.component.html',
  styleUrls: ['./reset-password.component.scss']
})
export class ResetPasswordComponent implements OnInit {

  form: any = {
    username: null,
    email: null,
  }
  isLoggedIn = false;
  confirm = '';
  error = '';
  token = null;

  formPassword!: FormGroup
  submittedPassword = false;

  constructor(private authService: AuthService, private tokenStorage: TokenStorageService,
              private activatedRoute: ActivatedRoute, private formBuilder: FormBuilder, private router: Router) {
  }

  ngOnInit(): void {
    if (this.tokenStorage.getJwtToken()) {
      this.isLoggedIn = true;
    }
    this.activatedRoute.queryParamMap.subscribe(params => {
      this.token = params.get('token');
    });
    this.formPassword = this.formBuilder.group(
      {
        password: ['', [Validators.required, Validators.minLength(6),
          Validators.pattern('^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z]).{6,}$')]]
      }, {}
    );
  }

  onReset(): void {
    this.submittedPassword = false;
    this.formPassword.reset();
  }

  get fPassword(): { [key: string]: AbstractControl } {
    return this.formPassword.controls;
  }

  onSubmit(): void {
    const {username, email} = this.form;

    this.authService.resetPassword(username, email).subscribe(
      data => {
        // @ts-ignore
        this.confirm = data.message;
      },
      err => {
        this.error = err;
      }
    );
    this.onReset();
  }

  onSubmitPassword() {
    this.submittedPassword = true;
    if (this.formPassword.invalid || this.formPassword.controls.password.pristine) {
      return;
    } else {
      this.authService.changePassword(this.token,this.formPassword.controls.password.value).subscribe(
        data => {
          // @ts-ignore
          this.confirm = data.message;
        },
        error => {
          this.error = error.error.message;
        }
      );
      this.onReset();
    }
  }
}
