import {Injectable} from '@angular/core';
import {DomSanitizer} from '@angular/platform-browser';

@Injectable({
  providedIn: 'root'
})
export class FileService {

  constructor(private sanitizer: DomSanitizer) {
  }

  preparePhoto(photo: string) {
    if (photo !== null) {
      return this.sanitizer
        .bypassSecurityTrustResourceUrl('' + photo.substr(0, photo.indexOf(',') + 1)
          + photo.substr(photo.indexOf(',') + 1));
    } else {
      return 'assets\\images\\usericon.png';
    }
  }

  prepareFile(file: string) {
    return this.sanitizer
      .bypassSecurityTrustResourceUrl('' + file.substr(0, file.indexOf(',') + 1)
        + file.substr(file.indexOf(',') + 1));
  }
}
