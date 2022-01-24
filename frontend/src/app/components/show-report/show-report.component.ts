import {Component, OnInit} from '@angular/core';
import {ActivatedRoute, NavigationEnd, Params, Router} from '@angular/router';
import {ReportService} from '../../service/report.service';

@Component({
  selector: 'app-show-report',
  templateUrl: './show-report.component.html',
  styleUrls: ['./show-report.component.scss']
})
export class ShowReportComponent implements OnInit {

  id: number = null;
  userId: number = null;
  eventId: number = null;
  date: string = null;
  time: string = null;
  reason: string = null;

  constructor(private  activatedRoute: ActivatedRoute, private reportService: ReportService, private router: Router) {
  }

  ngOnInit(): void {
    this.router.events.subscribe((evt) => {
      if (!(evt instanceof NavigationEnd)) {
        return;
      }
    })
    this.activatedRoute.params.subscribe((params: Params) => this.id = params.id);
    this.reportService.getOne(this.id).subscribe(
      data => {
        console.log(data);
        this.reason = data.reason;
        this.date = data.addDate.substr(0, 10);
        this.time = data.addDate.substr(11, 5);
        this.userId = data.userId;
        this.eventId = data.eventId;
      }, err => {
        console.log(err);
        this.reason = err.error.message;
      }
    )
  }
}
