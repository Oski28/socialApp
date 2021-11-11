import {Component, OnInit} from '@angular/core';
import {Router, NavigationEnd, NavigationStart, RouteConfigLoadStart, RouteConfigLoadEnd} from '@angular/router';
import {SafeResourceUrl} from '@angular/platform-browser';
import {TokenStorageService} from './service/token-storage.service';

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.scss'],
})
export class AppComponent implements OnInit {
  title = 'Rivia';

  showSidebar = true;
  showNavbar = true;
  showFooter = true;
  isLoading: boolean;
  isUser = false;
  avatar: SafeResourceUrl;

  constructor(private router: Router, private tokenService: TokenStorageService) {

    // Removing Sidebar, Navbar, Footer for Special Pages
    router.events.forEach((event) => {
      if (event instanceof NavigationStart) {
        this.isUser = this.tokenService.isLoggedIn();
        if ((event.url === '/error-pages/404') || (event.url === '/error-pages/500')) {
          document.querySelector('.main-panel').classList.add('w-100');
          document.querySelector('.page-body-wrapper').classList.add('full-page-wrapper');
          document.querySelector('.content-wrapper').classList.remove('auth', 'auth-img-bg',);
          document.querySelector('.content-wrapper').classList.remove('auth', 'lock-full-bg');
          if ((event.url === '/error-pages/404') || (event.url === '/error-pages/500')) {
            document.querySelector('.content-wrapper').classList.add('p-0');
          }
        } else if ((event.url === '') || (event.url === '/signup') || (event.url === '/signin') ||
          (event.url.includes('/signin')) || (event.url.includes('/signup'))
          || (event.url === '/home') || (event.url === '/about') || (event.url === '/faq')) {
          document.querySelector('.main-panel').classList.add('w-100');
          document.querySelector('.content-wrapper').classList.remove('auth', 'auth-img-bg',);
          document.querySelector('.content-wrapper').classList.remove('auth', 'lock-full-bg');
        } else {
          document.querySelector('.main-panel').classList.remove('w-100');
          document.querySelector('.page-body-wrapper').classList.remove('full-page-wrapper');
          document.querySelector('.content-wrapper').classList.remove('auth', 'auth-img-bg');
          document.querySelector('.content-wrapper').classList.remove('p-0');
        }
      }
    });

    // Spinner for lazyload modules
    router.events.forEach((event) => {
      if (event instanceof RouteConfigLoadStart) {
        this.isLoading = true;
      } else if (event instanceof RouteConfigLoadEnd) {
        this.isLoading = false;
      }
    });
  }


  ngOnInit() {
    // Scroll to top after route change
    this.router.events.subscribe((evt) => {
      if (!(evt instanceof NavigationEnd)) {
        return;
      }
      window.scrollTo(0, 0);
    });
  }
}
