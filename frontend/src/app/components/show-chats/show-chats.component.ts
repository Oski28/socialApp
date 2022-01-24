import {AfterViewInit, Component} from '@angular/core';
import {ChatService} from '../../service/chat.service';

@Component({
  selector: 'app-show-chats',
  templateUrl: './show-chats.component.html',
  styleUrls: ['./show-chats.component.scss']
})
export class ShowChatsComponent implements AfterViewInit {

  chats = [];
  currentPage = 0;
  totalPages = 0;
  totalElements = 0;

  input = '';
  pageSize = 20;
  column = 'id';
  direction = 'ASC';

  constructor(private chatService: ChatService) {
  }

  ngAfterViewInit(): void {
    this.getChats();
  }

  loadPage(page: number) {
    this.currentPage = page - 1;
    this.getChats();
  }

  private getChats() {
    this.chatService.getAll(this.column, this.direction, this.input, this.currentPage.toString(), this.pageSize.toString()).subscribe(
      data => {
        // @ts-ignore
        this.chats = data.content;
        // @ts-ignore
        this.totalPages = data.totalPages;
        // @ts-ignore
        this.totalElements = data.totalElements;
        this.prepareDate(this.chats);
      }
    )
  }

  private prepareDate(chats: any[]) {
    chats.forEach(chat => {
      if (chat.lastMessage !== null) {
        chat.lastMessage = chat.lastMessage.substr(0, 10).concat(' - ').concat(chat.lastMessage.substr(11, 8));
      }
    })
  }
}
