import {Component, ElementRef, OnInit, ViewChild} from '@angular/core';
import {UserService} from '../../service/user.service';
import {TokenStorageService} from '../../service/token-storage.service';
import {DomSanitizer, SafeResourceUrl} from '@angular/platform-browser';
import {AbstractControl, FormBuilder, FormGroup, Validators} from '@angular/forms';
import {RxwebValidators} from '@rxweb/reactive-form-validators';
import ValidationService from '../../service/validation.service';
import {AuthService} from '../../service/auth.service';
import {Router} from '@angular/router';
import {NgbModal} from '@ng-bootstrap/ng-bootstrap';
import {FileService} from '../../service/file-service';

@Component({
  selector: 'app-profil',
  templateUrl: './profile.component.html',
  styleUrls: ['./profile.component.scss']
})
export class ProfileComponent implements OnInit {

  guard = '';
  error = '';

  id: number;
  username: string;
  avatar: SafeResourceUrl;
  firstname: string;
  lastname: string;
  email: string;
  dateOfBirth: string;
  roles: [];
  addDate = '';

  formAvatar!: FormGroup;
  submittedAvatar = false;
  @ViewChild('img') imgInput: ElementRef;

  formData!: FormGroup;
  submittedData = false;
  private max: Date;

  formPassword!: FormGroup
  submittedPassword = false;

  form!: FormGroup;
  submitted = false;
  userId = null;

  constructor(private userService: UserService, private tokenService: TokenStorageService,
              private fileService: FileService, private formBuilder: FormBuilder,
              private authService: AuthService,
              private router: Router, private modalService: NgbModal) {
  }

  ngOnInit(): void {
    this.userService.getOne(this.tokenService.getUser().id).subscribe(data => {
        this.id = data.id;
        this.username = data.username;
        this.avatar = this.fileService.preparePhoto(data.avatar);
        this.firstname = data.firstname;
        this.lastname = data.lastname;
        this.username = data.username;
        this.email = data.email;
        this.dateOfBirth = data.dateOfBirth;
        this.roles = data.roles;
        this.addDate = data.addDate;
      }
    );

    this.formAvatar = this.formBuilder.group({
      avatar: ['', [RxwebValidators.image({minHeight: 200, minWidth: 200, maxHeight: 600, maxWidth: 600}),
        RxwebValidators.extension({extensions: ['jpeg', 'png', 'jpg']}),
        RxwebValidators.fileSize({maxSize: 15000000}),
        RxwebValidators.file({maxFiles: 1})]]
    }, {});

    this.max = new Date();
    this.max.setFullYear(this.max.getFullYear() - 12);
    this.formData = this.formBuilder.group(
      {
        firstname: ['', [Validators.required, Validators.minLength(3), Validators.maxLength(50)]],
        lastname: ['', [Validators.required, Validators.minLength(3), Validators.maxLength(50)]],
        username: ['', [Validators.required, Validators.minLength(3), Validators.maxLength(50)]],
        email: ['', [Validators.required, Validators.email]],
        dateOfBirth: ['', [Validators.required, ValidationService.dateValidator(new Date(1920, 1, 1), this.max)]]
      }, {});

    this.formPassword = this.formBuilder.group(
      {
        oldPassword: ['', [Validators.required, Validators.minLength(6),
          Validators.pattern('^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z]).{6,}$')]],
        newPassword: ['', [Validators.required, Validators.minLength(6),
          Validators.pattern('^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z]).{6,}$')]],
        confirmPassword: ['', [Validators.required]],
      }, {validators: [ValidationService.match('newPassword', 'confirmPassword')]}
    );
  }

  onSubmitAvatar(): void {
    this.submittedAvatar = true;
    if (this.formAvatar.invalid || this.formAvatar.controls.avatar.pristine) {
      return;
    } else {
      this.userService.updateAvatar(this.formAvatar.controls.avatar.value, this.id).subscribe(
        data => {
          this.onReset();
          window.location.reload();
        },
        error => {
          this.error = error.error.message;
          this.onReset();
          this.ngOnInit();
        }
      );
    }
  }

  onSubmitData(): void {
    this.submittedData = true;
    if (this.formData.invalid || this.formData.controls.firstname.pristine || this.formData.controls.lastname.pristine
      || this.formData.controls.username.pristine || this.formData.controls.email.pristine
      || this.formData.controls.dateOfBirth.pristine) {
      return;
    } else {
      this.userService.updateUser(this.formData.controls.firstname.value, this.formData.controls.lastname.value,
        this.formData.controls.username.value, this.formData.controls.email.value,
        this.formData.controls.dateOfBirth.value, this.id).subscribe(
        data => {
          this.onReset();
          this.logout();
        },
        error => {
          this.onReset();
          this.ngOnInit();
          this.error = error.error.message;
        }
      );
    }
  }

  onSubmitPassword(): void {
    this.submittedPassword = true;
    if (this.formPassword.invalid || this.formPassword.controls.oldPassword.pristine
      || this.formPassword.controls.newPassword.pristine || this.formPassword.controls.confirmPassword.pristine) {
      return;
    } else {
      this.userService.updatePassword(this.formPassword.controls.oldPassword.value,
        this.formPassword.controls.newPassword.value, this.id).subscribe(
        data => {
          this.onReset();
          this.logout();
        },
        error => {
          this.onReset();
          this.ngOnInit();
          this.error = error.error.message;
        }
      );
    }
  }

  onReset(): void {
    this.submittedAvatar = false;
    this.formAvatar.reset();
    this.imgInput.nativeElement.value = '';
    this.submittedData = false;
    this.formData.reset();
    this.submittedPassword = false;
    this.formPassword.reset();
  }

  public change(img: any) {
    if (img.files && img.files[0]) {
      const reader = new FileReader();
      const setImg = () => this.formAvatar.controls.avatar.setValue(reader.result);
      reader.addEventListener('load', setImg.bind(this), false);

      reader.readAsDataURL(img.files[0]);
    }
  }

  get fAvatar(): { [key: string]: AbstractControl } {
    return this.formAvatar.controls;
  }

  get fData(): { [key: string]: AbstractControl } {
    return this.formData.controls;
  }

  get fPassword(): { [key: string]: AbstractControl } {
    return this.formPassword.controls;
  }

  logout(message = 'Zmieniono dane logowania. Wymagane ponowne zalogowanie do serwisu'): void {
    let userId: number;
    let token: string | null;
    userId = this.tokenService.getUser().id;
    token = this.tokenService.getJwtToken();
    this.authService.logout(userId, token).subscribe(data => {
      },
      error => {
      });

    this.tokenService.signOut();
    this.guard = message;
    this.router.navigate(['signin', this.guard]);
  }

  removeAccount() {
    this.userService.removeUser(this.tokenService.getUser().id).subscribe();
    this.logout('Usuni??to konto. Aby je odzyska?? skorzystaj z linku wys??anego na adres email.');
  }

  openModal(removeModalContent) {
    this.modalService.open(removeModalContent);
  }
}
