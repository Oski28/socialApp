import {AfterViewInit, Component, Input, OnInit} from '@angular/core';
import {TokenStorageService} from '../../service/token-storage.service';
import {NavigationEnd, Router} from '@angular/router';
import {AuthService} from '../../service/auth.service';
import {DomSanitizer, SafeResourceUrl} from '@angular/platform-browser';
import {UserService} from '../../service/user.service';

@Component({
  selector: 'app-sidebar',
  templateUrl: './sidebar.component.html',
  styleUrls: ['./sidebar.component.scss']
})
export class SidebarComponent implements AfterViewInit {
  public categoriesCollapsed = false;
  public eventsCollapsed = false;
  public samplePagesCollapsed = false;
  @Input() isUser: boolean;
  public isMod: boolean;
  public isAdmin: boolean;
  public user;
  avatar: SafeResourceUrl;

  constructor(private tokenService: TokenStorageService, private router: Router,
              private sanitizer: DomSanitizer, private userService: UserService) {
  }

  ngAfterViewInit() {
    this.router.events.subscribe((evt) => {
      if (!(evt instanceof NavigationEnd)) {
        return;
      }
      this.isUser = this.tokenService.isLoggedIn();
      if (this.isUser) {
        this.user = this.tokenService.getUser();
        this.isMod = this.tokenService.isMod();
        this.isAdmin = this.tokenService.isAdmin();
        this.userService.getAvatar(this.tokenService.getUser().id).subscribe(
          data => {
            if (data.avatar !== null) {
              this.avatar = this.sanitizer
                .bypassSecurityTrustResourceUrl('' + data.avatar.substr(0, data.avatar.indexOf(',') + 1)
                  + data.avatar.substr(data.avatar.indexOf(',') + 1));
            } else {
              this.avatar = 'assets\\images\\usericon.png';
            }
          }
        )
      }
    });

    const body = document.querySelector('body');

    // add class 'hover-open' to sidebar navitem while hover in sidebar-icon-only menu
    // tslint:disable-next-line:only-arrow-functions
    document.querySelectorAll('.sidebar .nav-item').forEach(function (el) {
      // tslint:disable-next-line:only-arrow-functions
      el.addEventListener('mouseover', function () {
        if (body.classList.contains('sidebar-icon-only')) {
          el.classList.add('hover-open');
        }
      });
      // tslint:disable-next-line:only-arrow-functions
      el.addEventListener('mouseout', function () {
        if (body.classList.contains('sidebar-icon-only')) {
          el.classList.remove('hover-open');
        }
      });
    });
  }

}
