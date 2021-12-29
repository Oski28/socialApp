import {Component, OnInit} from '@angular/core';
import {ActivatedRoute, Params, Router} from '@angular/router';
import {DomSanitizer, SafeResourceUrl} from '@angular/platform-browser';
import {UserService} from '../../service/user.service';
import {TokenStorageService} from '../../service/token-storage.service';
import {AbstractControl, FormBuilder, FormControl, FormGroup, Validators} from '@angular/forms';
import ValidationService from '../../service/validation.service';
import {NgbModal} from '@ng-bootstrap/ng-bootstrap';
import {ReportService} from '../../service/report.service';
import {ChatService} from '../../service/chat.service';

@Component({
  selector: 'app-user-show',
  templateUrl: './user-show.component.html',
  styleUrls: ['./user-show.component.scss']
})
export class UserShowComponent implements OnInit {

  id: number = null;
  username: string;
  avatar: SafeResourceUrl;
  firstname: string;
  lastname: string;
  email: string;
  dateOfBirth: string;
  roles: [];
  addDate = '';
  blocked: boolean;
  banDate: string;

  isAuthMod: boolean;
  isAuthAdmin: boolean;

  form!: FormGroup;
  formRole!: FormGroup;
  submitted = false;
  isUserMod = false;
  isUserAdmin = false;

  correctMessage = '';
  errorMessage = '';
  formReport!: FormGroup;
  submittedReport = false;
  userId = null;

  constructor(private  activatedRoute: ActivatedRoute, private userService: UserService,
              private sanitizer: DomSanitizer, private tokenService: TokenStorageService,
              private formBuilder: FormBuilder, private modalService: NgbModal,
              private reportService: ReportService, private chatService: ChatService,
              private router: Router) {
  }

  ngOnInit(): void {
    this.activatedRoute.params.subscribe((params: Params) => this.id = params.id);
    window.scrollTo(0, 0);
    this.userService.getOne(this.id).subscribe(data => {
        this.id = data.id;
        this.username = data.username;
        if (data.avatar !== null) {
          this.avatar = this.sanitizer
            .bypassSecurityTrustResourceUrl('' + data.avatar.substr(0, data.avatar.indexOf(',') + 1)
              + data.avatar.substr(data.avatar.indexOf(',') + 1));
        } else {
          this.avatar = 'assets\\images\\usericon.png';
        }
        this.firstname = data.firstname;
        this.lastname = data.lastname;
        this.username = data.username;
        this.email = data.email;
        this.dateOfBirth = data.dateOfBirth;
        this.roles = data.roles;
        this.roles.forEach(value => {
          // @ts-ignore
          if (value.role === 'ROLE_MODERATOR')
            this.isUserMod = true;
          // @ts-ignore
          if (value.role === 'ROLE_ADMIN')
            this.isUserAdmin = true;
        })
        this.blocked = data.blocked;
        this.addDate = data.addDate;
        this.banDate = data.banExpirationDate;
        this.isAuthMod = this.tokenService.isMod();
        this.isAuthAdmin = this.tokenService.isAdmin();
      }
    );
    this.form = this.formBuilder.group(
      {
        banExpirationDate: ['', [Validators.required,
          ValidationService.dateValidator(new Date(), new Date(2100, 1, 1))]]
      });
    this.formRole = this.formBuilder.group({
      role: new FormControl()
    })
    this.formReport = this.formBuilder.group(
      {
        reason: ['', [Validators.required, Validators.minLength(1)
          , Validators.maxLength(300)]]
      }, {}
    );
    this.formRole.controls.role.setValue('USER');
  }

  get f(): { [key: string]: AbstractControl } {
    return this.form.controls;
  }

  onSubmit() {
    this.submitted = true;
    if (this.form.invalid || this.form.controls.banExpirationDate.pristine) {
      return;
    } else {
      this.userService.ban(this.form.controls.banExpirationDate.value, this.id).subscribe(
        data => {
          this.submitted = false;
          this.form.reset();
          this.ngOnInit();
        },
        err => {
          console.log(err)
          this.submitted = false;
          this.form.reset();
        }
      );
    }
  }

  get fReport(): { [key: string]: AbstractControl } {
    return this.formReport.controls;
  }

  onSubmitReport() {
    this.submittedReport = true;
    if (this.formReport.invalid || this.formReport.controls.reason.pristine) {
      this.errorMessage = 'Nieprawidłowe dane by wysłać zgłoszenie';
      this.correctMessage = '';
      return;
    } else {
      this.modalService.dismissAll();
      this.reportService.createForUser(this.formReport.controls.reason.value, this.userId).subscribe(
        data => {
          this.submittedReport = false;
          this.formReport.reset();
          this.correctMessage = 'Twoje zgłoszenie zostało wysłane administracji';
          this.errorMessage = '';
        },
        err => {
          this.submittedReport = false;
          this.formReport.reset()
          this.correctMessage = '';
          this.errorMessage = err.error.message;
        }
      );
    }
  }

  unban() {
    this.userService.unban(this.id).subscribe(
      data => {
        this.ngOnInit();
      },
      err => {
        console.log(err)
      });
  }

  onSubmitRole() {
    this.userService.updateRole(this.formRole.controls.role.value, this.id).subscribe(
      data => {
        window.location.reload();
      },
      err => {
        console.log(err)
        this.formRole.reset();
      }
    );
  }

  openModalReport(reportModal, userId: number) {
    this.userId = userId;
    this.modalService.open(reportModal);
  }

  openModalInfo(infoModal) {
    this.modalService.open(infoModal);
  }

  createChatIfNotExist() {
    this.chatService.create(this.id).subscribe(
      response => {
        this.router.navigate(['chat/'.concat(response.toString())]);
      }, error => {
        console.log(error);
      }
    )
  }
}
