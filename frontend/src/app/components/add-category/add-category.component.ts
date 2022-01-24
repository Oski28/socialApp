import {Component, OnInit} from '@angular/core';
import {AbstractControl, FormBuilder, FormGroup, Validators} from '@angular/forms';
import {CategoryService} from '../../service/category.service';

@Component({
  selector: 'app-add-category',
  templateUrl: './add-category.component.html',
  styleUrls: ['./add-category.component.scss']
})
export class AddCategoryComponent implements OnInit {
  error = '';
  form!: FormGroup;
  submitted = false;
  isSuccessful = false;

  constructor(private categoryService: CategoryService,
              private formBuilder: FormBuilder) {
  }

  ngOnInit(): void {
    this.form = this.formBuilder.group(
      {
        name: ['', [Validators.required, Validators.minLength(1)
          , Validators.maxLength(100)]]
      }, {}
    );
  }

  get f(): { [key: string]: AbstractControl } {
    return this.form.controls;
  }

  onSubmit() {
    this.submitted = true;
    if (this.form.invalid || this.form.controls.name.pristine) {
      return;
    } else {
      this.categoryService.create(this.form.controls.name.value).subscribe(
        data => {
          this.onReset()
          this.isSuccessful = true;
        },
        err => {
          this.onReset()
          this.isSuccessful = false;
          this.error = err.error.message;
        }
      );
    }
  }

  onReset(): void {
    this.submitted = false;
    this.form.reset();
  }

}
