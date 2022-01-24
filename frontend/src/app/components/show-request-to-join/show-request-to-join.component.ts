import {AfterViewInit, Component} from '@angular/core';
import {RequestToJoinService} from '../../service/request-to-join.service';

@Component({
  selector: 'app-show-request-to-join',
  templateUrl: './show-request-to-join.component.html',
  styleUrls: ['./show-request-to-join.component.scss']
})
export class ShowRequestToJoinComponent implements AfterViewInit {

  correctMessage = '';
  errorMessage = '';

  request = [];
  currentPage = 0;
  totalPages = 0;
  totalElements = 0;

  pageSize = 20;
  column = 'id';
  direction = 'ASC';

  constructor(private requestService: RequestToJoinService) {
  }

  getRequest() {
    this.requestService.getAllForUser(this.column, this.direction,
      this.currentPage.toString(), this.pageSize.toString()).subscribe(data => {
        this.request = data.content;
        this.totalPages = data.totalPages;
        this.totalElements = data.totalElements;
        this.prepareStatus(this.request);
      }
    )
  }

  prepareStatus(requests: any[]) {
    requests.forEach(request => {
      switch (request.status) {
        case 'WAITING' :
          request.status = 'Oczekująca';
          break;
        case 'ACCEPTED' :
          request.status = 'Zaakceptowana';
          break;
        case 'REJECTED' :
          request.status = 'Odrzucona';
          break;
      }
    })
  }

  ngAfterViewInit(): void {
    this.getRequest();
  }

  loadPage(page: number) {
    this.currentPage = page - 1;
    this.getRequest();
  }

  changeSize(value: any) {
    this.pageSize = value;
    this.getRequest();
  }

  changeColumn(value: any) {
    switch (value) {
      case 'Data wysłania':
        this.column = 'addDate';
        break;
      case 'Status':
        this.column = 'status';
        break;
    }
    this.getRequest()
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
    this.getRequest();
  }

  resend(id: number) {
    this.requestService.resend(id).subscribe(
      data => {
        this.correctMessage = 'Ponowno wysłano prośbę o dołączenie';
        this.errorMessage = '';
        this.getRequest();
      }, err => {
        this.correctMessage = '';
        this.errorMessage = err.error.errorMessage;
      }
    )
  }
}
