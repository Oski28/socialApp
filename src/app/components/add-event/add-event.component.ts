import {AfterViewInit, Component, OnInit} from '@angular/core';
import {AbstractControl, FormBuilder, FormControl, FormGroup, Validators} from '@angular/forms';
import ValidationService from '../../service/validation.service';
import {EventService} from '../../service/event.service';
import {CategoryService} from '../../service/category.service';

@Component({
  selector: 'app-add-event',
  templateUrl: './add-event.component.html',
  styleUrls: ['./add-event.component.scss']
})
export class AddEventComponent implements OnInit {
  form!: FormGroup;
  submitted = false;
  isSuccessful = false;
  isCreateFailed = false;
  errorMessage = '';
  ageLimit = 18;
  categoriesData = [];
  categories = [];


  constructor(private eventService: EventService, private categoryService: CategoryService, private formBuilder: FormBuilder) {
  }

  ngOnInit(): void {
    this.categoryService.getAll('').subscribe(
      data => {
        this.categoriesData = data;
      }
    );
    this.form = this.formBuilder.group(
      {
        name: ['', [Validators.required, Validators.minLength(3), Validators.maxLength(50)]],
        description: ['', [Validators.required, Validators.minLength(1), Validators.maxLength(300)]],
        maxNumberOfParticipant: ['', [Validators.required, Validators.min(2), Validators.max(149)]],
        location: ['', [Validators.required, Validators.minLength(1), Validators.maxLength(200)]],
        date: ['', [Validators.required, ValidationService.future()]],
        chatName: ['', [Validators.required, Validators.minLength(3), Validators.maxLength(50)]],
        freeJoin: new FormControl()
      },
      {}
    );
    this.form.controls.freeJoin.setValue('true');
  }

  get f(): { [key: string]: AbstractControl } {
    return this.form.controls;
  }

  onSubmit(): void {
    this.submitted = true;
    if (this.form.invalid || this.form.controls.name.pristine || this.form.controls.description.pristine ||
      this.form.controls.maxNumberOfParticipant.pristine || this.form.controls.location.pristine ||
      this.form.controls.date.pristine || this.form.controls.chatName.pristine) {
      return;
    } else {
      this.eventService.create(this.form.controls.name.value, this.form.controls.description.value, this.ageLimit,
        this.form.controls.maxNumberOfParticipant.value, this.form.controls.location.value,
        this.form.controls.date.value, this.form.controls.freeJoin.value, this.categories,
        this.form.controls.chatName.value
      ).subscribe(
        data => {
          this.isSuccessful = true;
          this.isCreateFailed = false;
          this.onReset();
        },
        err => {
          this.errorMessage = err.error.message;
          this.isCreateFailed = true;
          this.onReset();
        }
      );
    }
  }

  onReset(): void {
    this.submitted = false;
    this.form.reset();
  }

  changeAgeLimit(value: number) {
    this.ageLimit = value;
  }

  changeCheckbox(value: any) {
    if (value.target.checked === true) {
      this.categories.push(value.target.value);
    } else if (value.target.checked === false) {
      const index = this.categories.indexOf(value.target.value);
      if (index !== -1) {
        this.categories.splice(index, 1);
      }
    }
  }
}
