import {AfterViewInit, Component} from '@angular/core';
import {EventService} from '../../service/event.service';
import {RequestToJoinService} from '../../service/request-to-join.service';
import {NgbModal} from '@ng-bootstrap/ng-bootstrap';
import {AbstractControl, FormBuilder, FormGroup, Validators} from '@angular/forms';
import {ReportService} from '../../service/report.service';
import {AuthService} from '../../service/auth.service';
import {Router} from '@angular/router';

@Component({
  selector: 'app-find-event',
  templateUrl: './find-event.component.html',
  styleUrls: ['./find-event.component.scss']
})
export class FindEventComponent implements AfterViewInit {

  events = [];
  currentPage = 0;
  totalPages = 0;
  totalElements = 0;

  input = '';
  pageSize = 5;
  column = 'id';
  direction = 'ASC';
  activeDate = 'true';
  correctMessage = '';
  errorMessage = '';

  form!: FormGroup;
  submitted = false;
  eventId = null;
  userId = null;

  constructor(private eventService: EventService, private requestToJoinService: RequestToJoinService,
              private modalService: NgbModal, private formBuilder: FormBuilder,
              private reportService: ReportService, private router: Router) {
  }

  ngAfterViewInit(): void {
    this.correctMessage = '';
    this.errorMessage = '';
    this.getEvents();
    this.form = this.formBuilder.group(
      {
        reason: ['', [Validators.required, Validators.minLength(1)
          , Validators.maxLength(300)]]
      }, {}
    );
  }

  getEvents() {
    this.eventService.getAllNonParticipate(this.column, this.direction, this.input,
      this.currentPage.toString(), this.pageSize.toString(), this.activeDate).subscribe(
      data => {
        this.events = data.content;
        this.totalPages = data.totalPages
        this.totalElements = data.totalElements
      },
      err => {
        console.log(err);
      }
    );
  }

  loadPage(page: number) {
    this.currentPage = page - 1;
    this.getEvents();
  }

  changeSize(value: any) {
    this.pageSize = value;
    this.getEvents();
  }

  changeColumn(value: any) {
    switch (value) {
      case 'Data utworzenia konta':
        this.column = 'id';
        break;
      case 'Data wydarzenia':
        this.column = 'date';
        break;
      case 'Limit wiekowy':
        this.column = 'ageLimit';
        break;
    }
    this.getEvents()
  }

  changeDirection(value: any) {
    switch (value) {
      case 'Rosnąco':
        this.direction = 'ASC';
        break;
      case 'Malejąco':
        this.direction = 'DESC';
        break;
    }
    this.getEvents();
  }

  changeInput(value: string) {
    this.input = value;
    this.getEvents();
  }

  changeStatus(value: any) {
    switch (value) {
      case 'Aktywne':
        this.activeDate = 'true';
        break;
      case 'Nieaktywne':
        this.activeDate = 'false';
        break;
    }
    this.getEvents();
  }

  join(id: number) {
    this.eventService.join(id).subscribe(
      data => {
        this.correctMessage = 'Zostałeś uczestnikiem wydarzenia';
        this.errorMessage = '';
        this.getEvents();
        this.router.navigate(['event/'.concat(id.toString())]);
      }, err => {
        this.correctMessage = '';
        this.errorMessage = err.error.message;
      }
    )
  }

  request(id: any) {
    this.requestToJoinService.request(id).subscribe(
      data => {
        this.correctMessage = 'Wysłano prośbę o dołączenie';
        this.errorMessage = '';
        this.getEvents();
        this.router.navigate(['showRequest']);
      }, err => {
        this.correctMessage = '';
        this.errorMessage = err.error.message;
      }
    )
  }

  openModalReport(reportModal, eventId: number, userId: number) {
    this.eventId = eventId;
    this.userId = userId;
    this.modalService.open(reportModal);
  }

  openModalInfo(infoModal) {
    this.modalService.open(infoModal);
  }

  get f(): { [key: string]: AbstractControl } {
    return this.form.controls;
  }

  onSubmit() {
    this.submitted = true;
    if (this.form.invalid || this.form.controls.reason.pristine) {
      this.errorMessage = 'Nieprawidłowe dane by wysłać zgłoszenie';
      this.correctMessage = '';
      return;
    } else {
      this.modalService.dismissAll();
      this.reportService.create(this.form.controls.reason.value, this.userId, this.eventId).subscribe(
        data => {
          this.submitted = false;
          this.form.reset();
          this.correctMessage = 'Twoje zgłoszenie zostało wysłane administracji';
          this.errorMessage = '';
        },
        err => {
          this.submitted = false;
          this.form.reset()
          this.correctMessage = '';
          this.errorMessage = err.error.message;
        }
      );
    }
  }
}
