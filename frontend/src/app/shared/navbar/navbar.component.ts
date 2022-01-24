import {AfterViewInit, Component, Input} from '@angular/core';
import {NgbDropdownConfig} from '@ng-bootstrap/ng-bootstrap';
import {TokenStorageService} from '../../service/token-storage.service';
import {AuthService} from '../../service/auth.service';
import {NavigationEnd, Router} from '@angular/router';
import {SafeResourceUrl} from '@angular/platform-browser';
import {UserService} from '../../service/user.service';
import {NoticeService} from '../../service/notice.service';
import {ReportService} from '../../service/report.service';
import {FileService} from '../../service/file-service';

@Component({
  selector: 'app-navbar',
  templateUrl: './navbar.component.html',
  styleUrls: ['./navbar.component.scss'],
  providers: [NgbDropdownConfig]
})
export class NavbarComponent implements AfterViewInit {
  public iconOnlyToggled = false;
  public sidebarToggled = false;
  @Input() isUser: boolean;
  @Input() isMod: boolean;
  public username: string;
  avatar: SafeResourceUrl;

  notices: any[];
  reports: any[];

  constructor(config: NgbDropdownConfig, private tokenService: TokenStorageService,
              private authService: AuthService, private router: Router, private fileService: FileService,
              private userService: UserService, private noticeService: NoticeService,
              private reportService: ReportService) {
    config.placement = 'bottom-right';
  }

  ngAfterViewInit() {
    this.router.events.subscribe((evt) => {
        if (!(evt instanceof NavigationEnd)) {
          return;
        }
        this.isUser = this.tokenService.isLoggedIn();
        this.username = this.tokenService.getUser().username;
        if (this.isUser) {
          this.isMod = this.tokenService.isMod();
          this.userService.getAvatar(this.tokenService.getUser().id).subscribe(
            data => {
              this.avatar = this.fileService.preparePhoto(data.avatar);
            }
          )
          this.noticeService.getAllNotReceived().subscribe(
            data => {
              this.notices = data.content;
            }
          )
        }
        if (this.isMod) {
          this.reportService.getAllNotReceived().subscribe(
            data => {
              this.reports = data.content;
            }
          )
        }
      }
    );
  }

// toggle sidebar in small devices
  toggleOffcanvas() {
    document.querySelector('.sidebar-offcanvas').classList.toggle('active');
  }

// toggle sidebar
  toggleSidebar() {
    let body = document.querySelector('body');
    if ((!body.classList.contains('sidebar-toggle-display')) && (!body.classList.contains('sidebar-absolute'))) {
      this.iconOnlyToggled = !this.iconOnlyToggled;
      if (this.iconOnlyToggled) {
        body.classList.add('sidebar-icon-only');
      } else {
        body.classList.remove('sidebar-icon-only');
      }
    } else {
      this.sidebarToggled = !this.sidebarToggled;
      if (this.sidebarToggled) {
        body.classList.add('sidebar-hidden');
      } else {
        body.classList.remove('sidebar-hidden');
      }
    }
  }

  logout(): void {
    let userId: number;
    let token: string | null;
    userId = this.tokenService.getUser().id;
    token = this.tokenService.getJwtToken();
    this.authService.logout(userId, token).subscribe(data => {
        console.log(data);
      },
      error => {
        console.error(error.message);
      });

    this.tokenService.signOut();
    this.isUser = false;
    this.isMod = false;
    this.router.navigate(['']);
  }

// toggle right sidebar
// toggleRightSidebar() {
//   document.querySelector('#right-sidebar').classList.toggle('open');
// }
}
