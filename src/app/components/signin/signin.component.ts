import {Component, OnInit} from '@angular/core';
import {AuthService} from '../../service/auth.service';
import {TokenStorageService} from '../../service/token-storage.service';
import {ActivatedRoute, Params, Router} from '@angular/router';

@Component({
  selector: 'app-signin',
  templateUrl: './signin.component.html',
  styleUrls: ['./signin.component.scss']
})
export class SigninComponent implements OnInit {

  form: any = {
    username: null,
    password: null
  }
  isLoggedIn = false;
  isLoginFailed = false;
  errorMessage = '';
  roles: string[] = [];
  confirm = '';
  guard = null;
  username = '';

  constructor(private authService: AuthService, private tokenStorage: TokenStorageService,
              private activatedRoute: ActivatedRoute, private router: Router, private route: ActivatedRoute) {
  }

  ngOnInit(): void {
    if (this.tokenStorage.getJwtToken()) {
      this.isLoggedIn = true;
      this.username = this.tokenStorage.getUser().username;
      this.roles = this.tokenStorage.getUser().roles;
    }
    this.activatedRoute.queryParamMap.subscribe(params => {
      const token = params.get('token');
      if (token != null) {
        this.authService.confirmAccount(token).subscribe(
          data => {
            this.confirm = data.message;
          },
          error => {
            this.confirm = error.error.message;
            this.router.navigate(['signup', this.confirm]);
          }
        );
      }
      // tslint:disable-next-line:no-shadowed-variable
      this.route.params.subscribe((params: Params) => this.guard = params.guard);
    })
  }

  onSubmit(): void {
    const {username, password} = this.form;

    this.authService.login(username, password).subscribe(
      data => {
        this.tokenStorage.saveJwtToken(data.token);
        this.tokenStorage.saveRefreshToken(data.refreshToken);
        this.tokenStorage.saveUser(data);

        this.isLoginFailed = false;
        this.isLoggedIn = true;
        this.roles = this.tokenStorage.getUser().roles;
        this.router.navigate(['dashboard']);
      },
      err => {
        if (err.error.message === 'Bad credentials') {
          this.errorMessage = 'Niepoprawne dane uwierzytelniające';
        } else if (err.error.message === 'User is disabled') {
          this.errorMessage = 'Użytkownik zablokowany';
        } else {
          this.errorMessage = 'Błąd serwera';
        }
        console.log(this.errorMessage);
        this.isLoginFailed = true;
      }
    );
  }

  reloadPage(): void {
    window.location.reload();
  }
}
