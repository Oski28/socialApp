import {Component, OnInit} from '@angular/core';
import {ActivatedRoute, Params, Router} from '@angular/router';
import {EventService} from '../../service/event.service';
import {NgbModal} from '@ng-bootstrap/ng-bootstrap';
import {DomSanitizer} from '@angular/platform-browser';
import {AbstractControl, FormBuilder, FormControl, FormGroup, Validators} from '@angular/forms';
import ValidationService from '../../service/validation.service';
import {CategoryService} from '../../service/category.service';
import {RequestToJoinService} from '../../service/request-to-join.service';
import {UserService} from '../../service/user.service';
import {TokenStorageService} from '../../service/token-storage.service';
import {FileService} from '../../service/file-service';

@Component({
  selector: 'app-edit-event',
  templateUrl: './edit-event.component.html',
  styleUrls: ['./edit-event.component.scss']
})
export class EditEventComponent implements OnInit {

  id: number = null;
  name: string;
  ageLimit: number;
  maxUsers: number;
  date = '';
  freeJoin: boolean
  location: string;
  description: string;
  categories: [];

  correctMessage = '';
  errorMessage = '';

  correctMessageModal = '';
  errorMessageModal = '';

  removeUsername = '';
  removeId = null;

  form!: FormGroup;
  submitted = false;

  formNumber!: FormGroup;
  submittedNumber = false;

  formDate!: FormGroup;
  submittedDate = false;

  formCategories!: FormGroup;
  submittedCategories = false;
  categoriesData = [];
  newCategories = [];

  users: [];
  currentPageUsers = 0;
  totalElementsUsers = 0;
  pageSizeUsers = 10;

  requestToJoin = [];
  currentPageRequest = 0;
  totalElementsRequest = 0;
  pageSizeRequest = 10;

  constructor(private  activatedRoute: ActivatedRoute, private eventService: EventService,
              private modalService: NgbModal, private fileService: FileService, private formBuilder: FormBuilder,
              private categoryService: CategoryService, private requestToJoinService: RequestToJoinService,
              private userService: UserService, private tokenService: TokenStorageService,
              private router: Router) {
  }

  ngOnInit(): void {
    this.activatedRoute.params.subscribe((params: Params) => this.id = params.id);
    this.getUsers();
    this.getRequestToJoin();
    this.getEvent();
    this.getCategories();

    this.form = this.formBuilder.group(
      {
        name: ['', [Validators.required, Validators.minLength(3), Validators.maxLength(50)]],
        description: ['', [Validators.required, Validators.minLength(1), Validators.maxLength(300)]],
        location: ['', [Validators.required, Validators.minLength(1), Validators.maxLength(200)]],
        freeJoin: new FormControl()
      },
      {}
    );
    this.formNumber = this.formBuilder.group(
      {
        maxNumberOfParticipant: ['', [Validators.required, Validators.min(2), Validators.max(149)]],
      },
      {}
    );
    this.formDate = this.formBuilder.group(
      {
        date: ['', [Validators.required, ValidationService.future()]],
      },
      {}
    );
    this.formCategories = this.formBuilder.group(
      {},
      {}
    );
    this.form.controls.freeJoin.setValue('true');
  }

  getEvent() {
    this.eventService.getOne(this.id).subscribe(
      data => {
        if (data.organizerId !== this.tokenService.getUser().id && !this.tokenService.isMod()) {
          this.router.navigate(['forbidden']);
        }
        this.name = data.name;
        this.ageLimit = data.ageLimit;
        this.maxUsers = data.maxNumberOfParticipant;
        this.date = data.date;
        this.freeJoin = data.freeJoin;
        this.location = data.location;
        this.categories = data.categories;
        this.description = data.description;
      }, err => {
        console.log(err);
      }
    );
  }

  getUsers() {
    this.userService.getAllForEvent(this.currentPageUsers.toString(), this.pageSizeUsers.toString(), this.id).subscribe(
      data => {
        this.users = data.content;
        this.prepareAvatar(this.users);
        this.totalElementsUsers = data.totalElements;
      }
    )
  }

  getCategories() {
    this.categoryService.getAll('').subscribe(
      data => {
        this.categoriesData = data;
      }
    );
  }

  getRequestToJoin() {
    this.requestToJoinService.getAllForEvent(this.currentPageRequest.toString(), this.pageSizeRequest.toString(), this.id).subscribe(
      data => {
        this.requestToJoin = data.content;
        this.prepareAvatar(this.requestToJoin);
        this.totalElementsRequest = data.totalElements;
      }
    )
  }

  changeAgeLimit(value: number) {
    this.ageLimit = value;
  }

  get f(): { [key: string]: AbstractControl } {
    return this.form.controls;
  }

  get fNumber(): { [key: string]: AbstractControl } {
    return this.formNumber.controls;
  }

  get fDate(): { [key: string]: AbstractControl } {
    return this.formDate.controls;
  }

  private prepareAvatar(users: any[]) {
    users.forEach(user => {
      user.avatar = this.fileService.preparePhoto(user.avatar);
    })
  }

  onSubmit() {
    this.submitted = true;
    if (!(!this.form.invalid && !this.form.controls.name.pristine
      && !this.form.controls.description.pristine && !this.form.controls.location.pristine)) {
      return;
    } else {
      this.eventService.update(this.id, this.form.controls.name.value, this.form.controls.description.value, this.ageLimit,
        this.form.controls.location.value, this.form.controls.freeJoin.value).subscribe(
        data => {
          this.correctMessage = 'Poprawnie zmieniono dane';
          this.errorMessage = '';
          this.onReset();
          this.ngOnInit();
        },
        err => {
          this.correctMessage = '';
          this.errorMessage = err.error.message;
          this.onReset();
        }
      );
    }
  }

  onReset(): void {
    this.submitted = false;
    this.submittedNumber = false;
    this.submittedDate = false;
    this.form.reset();
    this.formNumber.reset();
    this.formDate.reset();
  }

  openModalInfo(infoModal) {
    this.modalService.open(infoModal);
  }

  openModalRemove(removeModalContent, name: string, id: number) {
    this.removeUsername = name;
    this.removeId = id;
    this.modalService.open(removeModalContent);
  }

  removeUser() {
    this.eventService.removeUserFromEvent(this.id, this.removeId).subscribe(
      data => {
        this.correctMessageModal = 'Usunięto użytkownika z wydarzenia';
        this.errorMessageModal = '';
        this.ngOnInit();
      }, err => {
        this.correctMessageModal = '';
        this.errorMessageModal = err.error.message;
      }
    )
  }

  onSubmitNumber() {
    this.submittedNumber = true;
    if (this.formNumber.invalid || this.formNumber.controls.maxNumberOfParticipant.pristine) {
      return;
    } else {
      this.eventService.updateNumber(this.id, this.formNumber.controls.maxNumberOfParticipant.value).subscribe(
        data => {
          this.correctMessage = 'Poprawnie zmieniono dane';
          this.errorMessage = '';
          this.onReset();
          this.ngOnInit();
        },
        err => {
          this.correctMessage = '';
          this.errorMessage = err.error.message;
          this.onReset();
        }
      );
    }
  }

  onSubmitDate() {
    this.submittedDate = true;
    if (this.formDate.invalid || this.formDate.controls.date.pristine) {
      return;
    } else {
      this.eventService.updateDate(this.id, this.formDate.controls.date.value).subscribe(
        data => {
          this.correctMessage = 'Poprawnie zmieniono date wydarzenia';
          this.errorMessage = '';
          this.onReset();
          this.ngOnInit();
        },
        err => {
          this.correctMessage = '';
          this.errorMessage = err.error.message;
          this.onReset();
        }
      );
    }
  }

  changeCheckbox(value: any) {
    if (value.target.checked === true) {
      this.newCategories.push(value.target.value);
    } else if (value.target.checked === false) {
      const index = this.newCategories.indexOf(value.target.value);
      if (index !== -1) {
        this.newCategories.splice(index, 1);
      }
    }
  }

  onSubmitCategories() {
    this.submittedCategories = true;
    if (this.formCategories.invalid) {
      return;
    } else {
      this.eventService.updateCategories(this.id, this.newCategories).subscribe(
        data => {
          this.correctMessage = 'Poprawnie zmieniono kategorie wydarzenia';
          this.errorMessage = '';
          this.onReset();
          this.ngOnInit();
        },
        err => {
          this.correctMessage = '';
          this.errorMessage = err.error.message;
          this.onReset();
        }
      );
    }
  }

  loadPageRequest(page: number) {
    this.currentPageRequest = page - 1;
    this.getRequestToJoin();
  }

  loadPageUsers(page: number) {
    this.currentPageUsers = page - 1;
    this.getUsers();
  }

  acceptRequest(id: number) {
    status = 'ACCEPTED';
    this.requestToJoinService.edit(id, status).subscribe(
      data => {
        this.correctMessage = 'Zmieniono status prośby';
        this.errorMessage = '';
        this.onReset();
        this.ngOnInit();
      },
      err => {
        this.correctMessage = '';
        this.errorMessage = err.error.message;
        this.onReset();
      }
    )
  }

  rejectRequest(id: number) {
    status = 'REJECTED';
    this.requestToJoinService.edit(id, status).subscribe(
      data => {
        this.correctMessage = 'Zmieniono status prośby';
        this.errorMessage = '';
        this.onReset();
        this.ngOnInit();
      },
      err => {
        this.correctMessage = '';
        this.errorMessage = err.error.message;
        this.onReset();
      }
    )
  }
}
