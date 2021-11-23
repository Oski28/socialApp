import {AfterViewInit, Component} from '@angular/core';
import {EventService} from '../../service/event.service';
import {NgbModal} from '@ng-bootstrap/ng-bootstrap';

@Component({
  selector: 'app-show-my-events',
  templateUrl: './show-my-events.component.html',
  styleUrls: ['./show-my-events.component.scss']
})
export class ShowMyEventsComponent implements AfterViewInit {

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

  removeName = '';
  removeId = null;

  constructor(private eventService: EventService, private modalService: NgbModal) {
  }

  ngAfterViewInit(): void {
    this.correctMessage = '';
    this.errorMessage = '';
    this.getEvents();
  }

  getEvents() {
    this.eventService.getAllForAuthUserCreate(this.column, this.direction, this.input,
      this.currentPage.toString(), this.pageSize.toString(), this.activeDate).subscribe(
      data => {
        this.events = data['content'];
        this.totalPages = data['totalPages']
        this.totalElements = data['totalElements']
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
        this.getEvents();
      }, err => {
        this.correctMessage = '';
        this.errorMessage = err.error.message;
      }
    );
  }

  openModalInfo(infoModal) {
    this.modalService.open(infoModal);
  }
}
