import {Component, OnInit} from '@angular/core';
import {ActivatedRoute, Params, Router} from '@angular/router';
import {EventService} from '../../service/event.service';
import {DomSanitizer} from '@angular/platform-browser';
import {UserService} from '../../service/user.service';
import {ChatService} from '../../service/chat.service';
import {TokenStorageService} from '../../service/token-storage.service';
import {NgbModal} from '@ng-bootstrap/ng-bootstrap';
import {RequestToJoinService} from '../../service/request-to-join.service';
import {AbstractControl, FormBuilder, FormGroup, Validators} from '@angular/forms';
import {ReportService} from '../../service/report.service';

@Component({
  selector: 'app-show-event',
  templateUrl: './show-event.component.html',
  styleUrls: ['./show-event.component.scss']
})
export class ShowEventComponent implements OnInit {
  id: number = null;
  chatId: number = null;
  ageLimit: string;
  categories: any[];
  date: string;
  description: string;
  freeJoin: boolean;
  location: string;
  maxNumber: number;
  name: string;
  organizer: string;
  organizerId: number;
  users: any[];

  currentPageUsers = 0;
  totalElementsUsers = 0;
  pageSizeUsers = 10;

  isOrganizer: boolean;
  isParticipant = false;
  isMod: boolean;

  correctMessage = '';
  errorMessage = '';

  form!: FormGroup;
  submitted = false;
  eventId = null;
  userId = null;

  removeName = '';
  removeId = null;

  divorceName = '';
  divorceId = null;

  constructor(private  activatedRoute: ActivatedRoute, private eventService: EventService,
              private sanitizer: DomSanitizer, private userService: UserService,
              private chatService: ChatService, private router: Router,
              private tokenService: TokenStorageService, private modalService: NgbModal,
              private requestToJoinService: RequestToJoinService,
              private reportService: ReportService, private formBuilder: FormBuilder) {
  }

  ngOnInit(): void {
    this.activatedRoute.params.subscribe((params: Params) => this.id = params.id);
    this.getUsers();
    this.getEvent();
    this.isMod = this.tokenService.isMod();
    this.form = this.formBuilder.group(
      {
        reason: ['', [Validators.required, Validators.minLength(1)
          , Validators.maxLength(300)]]
      }, {}
    );
  }

  getEvent() {
    this.eventService.getOne(this.id).subscribe(
      data => {
        console.log(data)
        this.name = data.name;
        this.ageLimit = data.ageLimit;
        this.maxNumber = data.maxNumberOfParticipant;
        this.date = data.date;
        this.freeJoin = data.freeJoin;
        this.location = data.location;
        this.categories = data.categories;
        this.description = data.description;
        this.chatId = data.chatId;
        this.organizerId = data.organizerId;
        this.isOrganizer = data.organizerId === this.tokenService.getUser().id;
        this.organizer = data.organizer;
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
        this.users.forEach(user => {
          if (user.id === this.tokenService.getUser().id)
            this.isParticipant = true;
        })
        this.totalElementsUsers = data.totalElements;
      }
    )
  }

  private prepareAvatar(users: any[]) {
    users.forEach(user => {
      if (user.avatar !== null) {
        user.avatar = this.sanitizer
          .bypassSecurityTrustResourceUrl('' + user.avatar.substr(0, user.avatar.indexOf(',') + 1)
            + user.avatar.substr(user.avatar.indexOf(',') + 1));
      } else {
        user.avatar = 'assets\\images\\usericon.png';
      }
    })
  }

  loadPageUsers(page: number) {
    this.currentPageUsers = page - 1;
    this.getUsers();
  }

  message(id: any) {
    this.chatService.create(id).subscribe(
      response => {
        this.router.navigate(['chat/'.concat(response.toString())]);
      }, error => {
        console.log(error);
      }
    )
  }

  chat(chatId: number) {
    this.router.navigate(['chat/'.concat(chatId.toString())]);
  }

  join(id: number) {
    this.eventService.join(id).subscribe(
      data => {
        this.correctMessage = 'Zostałeś uczestnikiem wydarzenia';
        this.errorMessage = '';
        this.ngOnInit();
      }, err => {
        this.correctMessage = '';
        this.errorMessage = err.error.message;
      }
    )
  }

  openModalInfo(infoModal) {
    this.modalService.open(infoModal);
  }

  requestToJoin(id: number) {
    this.requestToJoinService.request(id).subscribe(
      data => {
        this.correctMessage = 'Wysłano prośbę o dołączenie';
        this.errorMessage = '';
        this.ngOnInit()
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

  edit() {
    this.router.navigate(['editEvent/'.concat(this.id.toString())]);
  }

  openModalRemove(removeModalContent, name: string, id: number) {
    this.removeName = name;
    this.removeId = id;
    this.modalService.open(removeModalContent);
  }

  removeEvent() {
    this.eventService.delete(this.removeId).subscribe(
      data => {
        this.correctMessage = 'Wydarzenie zostało usunięte';
        this.errorMessage = '';
        this.router.navigate(['showMyEvents']);
      }, err => {
        this.correctMessage = '';
        this.errorMessage = err.error.message;
      }
    );
  }

  openModalDivorce(divorceModalContent, name: string, id: string) {
    this.divorceName = name;
    this.divorceId = id;
    this.modalService.open(divorceModalContent);
  }

  divorceEvent() {
    this.eventService.divorce(this.divorceId).subscribe(
      data => {
        this.correctMessage = 'Opuściłeś wydarzenie';
        this.errorMessage = '';
        window.location.reload();
      }, err => {
        this.correctMessage = '';
        this.errorMessage = err.error.message;
      }
    )
  }
}
