import {Component, OnInit} from '@angular/core';
import {NoticeService} from '../../service/notice.service';

@Component({
  selector: 'app-show-notices',
  templateUrl: './show-notices.component.html',
  styleUrls: ['./show-notices.component.scss']
})
export class ShowNoticesComponent implements OnInit {

  notices: any[];
  currentPage = 0;
  totalPages = 0;
  totalElements = 0;
  pageSize = 10;

  constructor(private noticeService: NoticeService) {
  }

  ngOnInit(): void {
    this.getNotices();
  }

  getNotices() {
    this.noticeService.getAllForUser(this.currentPage.toString(), this.pageSize.toString()).subscribe(
      data => {
        console.log(data);
        // @ts-ignore
        this.notices = data.content;
        // @ts-ignore
        this.totalPages = data.totalPages;
        // @ts-ignore
        this.totalElements = data.totalElements;
      }, err => {
        console.log(err);
      }
    )
  }

  loadPage(page: number) {
    this.currentPage = page - 1;
    this.getNotices();
  }

}
