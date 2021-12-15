import {Component, ElementRef, OnDestroy, OnInit, ViewChild} from '@angular/core';
import {MessageService} from '../../service/message.service';
import {ChatService} from '../../service/chat.service';
import {ActivatedRoute, Params} from '@angular/router';
import {DomSanitizer} from '@angular/platform-browser';
import {TokenStorageService} from '../../service/token-storage.service';
import {Subject} from 'rxjs';
import {takeUntil} from 'rxjs/operators';
import {AbstractControl, FormBuilder, FormGroup, Validators} from '@angular/forms';

@Component({
  selector: 'app-show-chat',
  templateUrl: './show-chat.component.html',
  styleUrls: ['./show-chat.component.scss']
})
export class ShowChatComponent implements OnInit, OnDestroy {
  userId: number;
  top = true;
  empty = false;

  chatId: number;
  messages = [];
  page = 0;

  form!: FormGroup;
  submitted = false;
  bothNull = true;
  @ViewChild('file') fileInput: ElementRef;

  audio = new Audio('assets\\sounds\\message_sound.mp3');

  private unsubscribeSubject: Subject<void> = new Subject<void>();

  constructor(private  activatedRoute: ActivatedRoute, private messageService: MessageService,
              private sanitizer: DomSanitizer, private chatService: ChatService,
              private tokenService: TokenStorageService, private formBuilder: FormBuilder) {
  }

  ngOnInit(): void {
    this.activatedRoute.params.subscribe((params: Params) => this.chatId = params.id);
    this.userId = this.tokenService.getUser().id;
    this.getMessages();
    /* this.messageService
       .findAll('28', this.chatId)
       .subscribe(messages => {
         console.log(messages)
         this.mess = messages
       });*/
    this.messageService
      .onPost(this.chatId)
      .pipe(takeUntil(this.unsubscribeSubject))
      .subscribe(message => {
        this.prepareDate([message]);
        this.messages.push(message);
        this.audio.play();

      });
    this.form = this.formBuilder.group({
      file: ['', []],
      message: ['', [Validators.required, Validators.maxLength(300)]]
    }, {});
  }

  onReset(): void {
    this.submitted = false;
    this.form.reset();
    this.fileInput.nativeElement.value = '';
  }

  ngOnDestroy(): void {
    this.unsubscribeSubject.next();
    this.unsubscribeSubject.complete();
  }

  private getMessages() {
    this.messageService.getAll(this.page.toString(), this.chatId).subscribe(
      data => {
        this.messages = data.content;
        this.prepareAvatar(this.messages);
        this.prepareDate(this.messages);
      }, err => {
        console.log(err);
      }
    )
  }

  private getMoreMessages() {
    this.messageService.getAll(this.page.toString(), this.chatId).subscribe(
      data => {
        if (data.empty) {
          this.empty = true;
        } else {
          this.prepareAvatar(data.content.reverse());
          this.prepareDate(data.content.reverse());
          data.content.reverse().forEach(obj => {
            this.messages.unshift(obj);
          })
        }
      }, err => {
        console.log(err);
      }
    )
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

  private prepareDate(messages: any[]) {
    messages.forEach(message => {
      if (message.deleteDate !== null) {
        message.deleteDate = message.deleteDate.substr(0, 10).concat(' - ').concat(message.deleteDate.substr(11, 8));
      }
      message.writeDate = message.writeDate.substr(0, 10).concat(' - ').concat(message.writeDate.substr(11, 8));
    })
  }

  removeMessage(id: number) {
    this.messageService.delete(id).subscribe(
      data => {
        this.page = 0;
        this.empty = false;
        this.getMessages();
      }, err => {
        console.log(err);
      }
    )
  }

  loadData(scroll: HTMLDivElement) {
    if (scroll.scrollTop === 0 && !this.empty) {
      this.top = false;
      this.page++;
      this.getMoreMessages();
    }
  }

  public change(file: any) {
    if (file.files && file.files[0]) {
      const reader = new FileReader();
      const setFile = () => this.form.controls.file.setValue(reader.result);
      reader.addEventListener('load', setFile.bind(this), false);

      reader.readAsDataURL(file.files[0]);
    }
  }

  get f(): { [key: string]: AbstractControl } {
    return this.form.controls;
  }

  onSubmit() {
    this.submitted = true;
    if (this.form.invalid || (this.form.controls.file.pristine && this.form.controls.message.pristine)) {
      return;
    } else {
      this.bothNull = false;
      this.messageService.save(this.form.controls.message.value, this.form.controls.file.value, this.chatId, this.userId);
      this.onReset();
    }
  }
}
