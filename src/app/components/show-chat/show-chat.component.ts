import {Component, ElementRef, OnDestroy, OnInit, ViewChild} from '@angular/core';
import {MessageService} from '../../service/message.service';
import {ChatService} from '../../service/chat.service';
import {ActivatedRoute, Params} from '@angular/router';
import {DomSanitizer} from '@angular/platform-browser';
import {TokenStorageService} from '../../service/token-storage.service';
import {Subject} from 'rxjs';
import {takeUntil} from 'rxjs/operators';
import {AbstractControl, FormBuilder, FormGroup, Validators} from '@angular/forms';
import {NgbModal} from '@ng-bootstrap/ng-bootstrap';
import {saveAs} from 'file-saver';
import {RxwebValidators} from '@rxweb/reactive-form-validators';

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
  chat: any;
  messages = [];
  page = 0;

  form!: FormGroup;
  submitted = false;
  bothNull = true;
  @ViewChild('file') fileInput: ElementRef;

  audio = new Audio('assets\\sounds\\message_sound.mp3');

  formEdit!: FormGroup;
  submittedEdit = false;
  fileName = null;

  error = '';

  private unsubscribeSubject: Subject<void> = new Subject<void>();

  constructor(private  activatedRoute: ActivatedRoute, private messageService: MessageService,
              private sanitizer: DomSanitizer, private chatService: ChatService,
              private tokenService: TokenStorageService, private formBuilder: FormBuilder,
              private modalService: NgbModal) {
  }

  ngOnInit(): void {
    this.activatedRoute.params.subscribe((params: Params) => this.chatId = params.id);
    this.userId = this.tokenService.getUser().id;
    this.getMessages();
    this.chatService.getOne(this.chatId).subscribe(
      data => {
        this.chat = data;
      }, err => {
        this.error = 'Brak uprawień do pobrania danych czatu o id' + this.chatId;
        console.log(err);
      }
    )
    this.messageService
      .onPost(this.chatId)
      .pipe(takeUntil(this.unsubscribeSubject))
      .subscribe(message => {
        this.prepareDate([message]);
        this.prepareAvatar([message]);
        this.prepareFile([message]);
        this.messages.push(message);
        if (message.senderId !== this.userId) {
          this.audio.play();
        }
      });
    this.form = this.formBuilder.group({
      file: ['', [RxwebValidators.fileSize({maxSize: 3100000})]],
      message: ['', [Validators.required, Validators.maxLength(300)]]
    }, {});
    this.formEdit = this.formBuilder.group(
      {
        name: ['', [Validators.required, Validators.minLength(3)
          , Validators.maxLength(50)]]
      }, {}
    );
    this.form.controls.file.setValue(null);
  }

  onReset(): void {
    this.submitted = false;
    this.form.reset();
    this.fileInput.nativeElement.value = '';
    this.submittedEdit = false;
    this.formEdit.reset();
    this.modalService.dismissAll();
  }

  ngOnDestroy(): void {
    this.unsubscribeSubject.next();
    this.unsubscribeSubject.complete();
  }

  private getMessages() {
    this.messageService.getAll(this.page.toString(), this.chatId).subscribe(
      data => {
        this.messages = data.content;
        this.prepareFile(this.messages);
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
      this.fileName = file.files[0].name;
    }
  }

  get f(): { [key: string]: AbstractControl } {
    return this.form.controls;
  }

  get fEdit(): { [key: string]: AbstractControl } {
    return this.formEdit.controls;
  }

  onSubmit() {
    this.submitted = true;
    if (this.form.invalid || (this.form.controls.file.pristine && this.form.controls.message.pristine)) {
      return;
    } else {
      this.bothNull = false;
      this.messageService.save(this.form.controls.message.value, this.form.controls.file.value, this.fileName, this.chatId, this.userId);
      this.onReset();
    }
  }

  openModalEdit(editChatName) {
    this.modalService.open(editChatName);
  }

  onSubmitEdit() {
    this.submittedEdit = true;
    if (this.formEdit.invalid || this.formEdit.controls.name.pristine) {
      return;
    } else {
      this.chatService.editName(this.formEdit.controls.name.value, this.chat.id).subscribe(
        data => {
          this.ngOnInit();
        }, err => {
          console.log(err);
        }
      );
      this.onReset();
    }
  }

  private prepareFile(messages: any[]) {
    messages.forEach(message => {
      if (!message.fileToDownload && message.file !== null) {
        message.file = this.sanitizer
          .bypassSecurityTrustResourceUrl('' + message.file.substr(0, message.file.indexOf(',') + 1)
            + message.file.substr(message.file.indexOf(',') + 1));
      }
    })
  }

  downloadFile(file: any) {
    this.messageService.downloadFile(file).subscribe(blob => {
      this.messageService.getFileName(file.concat('/name')).subscribe(
        data => {
          saveAs(blob, data);
        }, error => {
          console.log(error)
        }
      );
    });
  }
}
