import {AfterViewInit, Component} from '@angular/core';
import {UserService} from '../../service/user.service';
import {DomSanitizer} from '@angular/platform-browser';

@Component({
  selector: 'app-find-user',
  templateUrl: './find-user.component.html',
  styleUrls: ['./find-user.component.scss']
})
export class FindUserComponent implements AfterViewInit {

  // tslint:disable-next-line:ban-types
  users = [];
  currentPage = 0;
  totalPages = 0;
  totalElements = 0;

  input = '';
  pageSize = 20;
  column = 'id';
  direction = 'ASC';

  constructor(private userService: UserService, private sanitizer: DomSanitizer) {
  }

  getUsers() {
    this.userService.getAll(this.column, this.direction, this.input,
      this.currentPage.toString(), this.pageSize.toString()).subscribe(data => {
        this.users = data['content'];
        this.totalPages = data['totalPages']
        this.totalElements = data['totalElements']
        this.prepareAvatar(this.users);
      }
    )
  }

  changeInput(value: string) {
    this.input = value;
    this.getUsers();
  }

  private prepareAvatar(users: any[]) {
    users.forEach(user => {
      if (user.avatar !== null) {
        user.avatar = this.sanitizer
          .bypassSecurityTrustResourceUrl('' + user.avatar.substr(0, user.avatar.indexOf(',') + 1)
            + user.avatar.substr(user.avatar.indexOf(',') + 1));
      } else {
        user.avatar = 'assets\\images\\usericon.png';
      }
    })
  }

  ngAfterViewInit(): void {
    this.getUsers();
  }

  loadPage(page: number) {
    this.currentPage = page - 1;
    this.getUsers();
  }

  changeSize(value: any) {
    this.pageSize = value;
    this.getUsers();
  }

  changeColumn(value: any) {
    switch (value) {
      case 'Data utworzenia konta':
        this.column = 'id';
        break;
      case 'Imie':
        this.column = 'firstname';
        break;
      case 'Nazwisko':
        this.column = 'lastname';
        break;
      case 'Nazwa użytkownika':
        this.column = 'username';
        break;
      case 'Email':
        this.column = 'email';
        break;
      case 'Data urodzenia':
        this.column = 'dateOfBirth';
        break;
    }
    this.getUsers()
  }

  changeDirection(value: any) {
    switch (value) {
      case 'Rosnąco':
        this.direction = 'ASC';
        break;
      case 'Malejąco':
        this.direction = 'DESC';
        break;
    }
    this.getUsers();
  }
}
