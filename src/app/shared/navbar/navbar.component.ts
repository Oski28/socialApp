import {AfterViewInit, Component, Input, OnInit} from '@angular/core';
import {NgbDropdownConfig, NgbModal} from '@ng-bootstrap/ng-bootstrap';
import {TokenStorageService} from '../../service/token-storage.service';
import {AuthService} from '../../service/auth.service';
import {NavigationEnd, Router} from '@angular/router';
import {DomSanitizer, SafeResourceUrl} from '@angular/platform-browser';
import {UserService} from '../../service/user.service';

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
  public username: string;
  avatar: SafeResourceUrl;

  constructor(config: NgbDropdownConfig, private tokenService: TokenStorageService,
              private authService: AuthService, private router: Router, private sanitizer: DomSanitizer,
              private userService: UserService, private modalService: NgbModal) {
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
    this.router.navigate(['']);
  }

// toggle right sidebar
// toggleRightSidebar() {
//   document.querySelector('#right-sidebar').classList.toggle('open');
// }

  removeAccount() {
    this.userService.removeUser(this.tokenService.getUser().id).subscribe();
    this.logout();
  }

  openModal(removeModalContent) {
    this.modalService.open(removeModalContent);
  }
}
