import {Component, OnInit} from '@angular/core';
import {TokenStorageService} from '../../service/token-storage.service';
import {NavigationEnd, Router} from '@angular/router';
import {AuthService} from '../../service/auth.service';

@Component({
  selector: 'app-sidebar',
  templateUrl: './sidebar.component.html',
  styleUrls: ['./sidebar.component.scss']
})
export class SidebarComponent implements OnInit {
  public uiBasicCollapsed = false;
  public samplePagesCollapsed = false;
  public isUser: boolean;

  constructor(private tokenService: TokenStorageService, private router: Router) {
  }

  ngOnInit() {
    this.router.events.subscribe((evt) => {
      if ((evt instanceof NavigationEnd)) {
        this.isUser = this.tokenService.isLoggedIn();
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
