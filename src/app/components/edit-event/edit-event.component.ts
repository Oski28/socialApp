import {Component, OnInit} from '@angular/core';
import {ActivatedRoute, Params} from '@angular/router';
import {EventService} from '../../service/event.service';
import {NgbModal} from '@ng-bootstrap/ng-bootstrap';
import {DomSanitizer} from '@angular/platform-browser';

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
  categories: [];
  users: [];

  removeUsername = '';
  removeId = null;

  constructor(private  activatedRoute: ActivatedRoute, private eventService: EventService,
              private modalService: NgbModal, private sanitizer: DomSanitizer) {
  }

  ngOnInit(): void {
    this.activatedRoute.params.subscribe((params: Params) => this.id = params.id);
    this.eventService.getOne(this.id).subscribe(
      data => {
        this.name = data.name;
        this.ageLimit = data.ageLimit;
        this.maxUsers = data.maxNumberOfParticipant;
        this.date = data.date;
        this.freeJoin = data.freeJoin;
        this.location = data.location;
        this.categories = data.categories;
        this.users = data.participateUsers;
        this.prepareAvatar(this.users);
      }, err => {
        console.log(err);
      }
    )
    // formy
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

  onSubmit() {

  }

  openModalRemove(removeModalContent, name: string, id: number) {
    this.removeUsername = name;
    this.removeId = id;
    this.modalService.open(removeModalContent);
  }

  removeUser() {
    this.eventService.removeUserFromEvent(this.id, this.removeId).subscribe(
      data => {
        // info
        console.log(data)
        this.ngOnInit();
      }, err => {
        // Wyświetlić błąd
        console.log(err);
      }
    )
  }
}
