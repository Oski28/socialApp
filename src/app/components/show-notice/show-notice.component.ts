import {Component, OnInit} from '@angular/core';
import {ActivatedRoute, NavigationEnd, Params, Router} from '@angular/router';
import {NoticeService} from '../../service/notice.service';
import {date} from '@rxweb/reactive-form-validators';

@Component({
  selector: 'app-show-notice',
  templateUrl: './show-notice.component.html',
  styleUrls: ['./show-notice.component.scss']
})
export class ShowNoticeComponent implements OnInit {

  id: number = null;
  content: string = null;

  constructor(private  activatedRoute: ActivatedRoute, private noticeService: NoticeService, private router: Router) {
  }

  ngOnInit(): void {
    this.router.events.subscribe((evt) => {
      if (!(evt instanceof NavigationEnd)) {
        return;
      }
    })
    this.activatedRoute.params.subscribe((params: Params) => this.id = params.id);
    this.noticeService.getOne(this.id).subscribe(
      data => {
        this.content = data.content;
      }, err => {
        console.log(err);
        this.content = 'Brak uprawień do pobrania powiadomienia o id ' + this.id;
      }
    )
  }
}
