import {Component, OnInit} from '@angular/core';
import {ReportService} from '../../service/report.service';

@Component({
  selector: 'app-show-reports',
  templateUrl: './show-reports.component.html',
  styleUrls: ['./show-reports.component.scss']
})
export class ShowReportsComponent implements OnInit {

  reports: any[];
  currentPage = 0;
  totalPages = 0;
  totalElements = 0;
  pageSize = 10;

  constructor(private reportService: ReportService) {
  }

  ngOnInit(): void {
    this.getReports();
  }

  getReports() {
    this.reportService.getAll(this.currentPage.toString(), this.pageSize.toString()).subscribe(
      data => {
        // @ts-ignore
        this.reports = data.content;
        this.prepareDateTime(this.reports);
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
    this.getReports();
  }

  removeReport(id: number) {
    this.reportService.remove(id).subscribe(
      data => {
        this.getReports();
      }, err => {
        console.log(err);
      }
    )
  }

  setReceived(id: number) {
    this.reportService.received(id).subscribe(
      data => {
        this.getReports();
      }, err => {
        console.log(err);
      }
    )
  }

  private prepareDateTime(reports: any[]) {
    reports.forEach(report => report.addDate = report.addDate.substr(0, 10).concat(' - ').concat(report.addDate.substr(11, 5)));
  }
}
