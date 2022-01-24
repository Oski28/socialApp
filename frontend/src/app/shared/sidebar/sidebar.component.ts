import {AfterViewInit, Component, Input, OnInit} from '@angular/core';
import {TokenStorageService} from '../../service/token-storage.service';
import {NavigationEnd, Router} from '@angular/router';
import {SafeResourceUrl} from '@angular/platform-browser';
import {UserService} from '../../service/user.service';
import {FileService} from '../../service/file-service';

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
              private fileService: FileService, private userService: UserService) {
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
            this.avatar = this.fileService.preparePhoto(data.avatar);
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
