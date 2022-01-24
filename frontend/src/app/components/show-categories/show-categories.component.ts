import {AfterViewInit, Component} from '@angular/core';
import {CategoryService} from '../../service/category.service';
import {NgbModal} from '@ng-bootstrap/ng-bootstrap';
import {AbstractControl, FormBuilder, FormGroup, Validators} from '@angular/forms';

@Component({
  selector: 'app-show-categories',
  templateUrl: './show-categories.component.html',
  styleUrls: ['./show-categories.component.scss']
})
export class ShowCategoriesComponent implements AfterViewInit {
  error = '';

  categories = [];
  removeName = '';
  removeId = null;
  editId = null;

  form!: FormGroup;
  submitted = false;

  input = '';

  constructor(private categoryService: CategoryService, private modalService: NgbModal,
              private formBuilder: FormBuilder) {
  }

  getCategories() {
    this.categoryService.getAll(this.input).subscribe(data => {
        this.categories = data;
      }
    )
  }

  changeInput(value: string) {
    this.input = value;
    this.getCategories()
  }

  ngAfterViewInit(): void {
    this.getCategories()
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

  openModalRemove(removeModalContent, name: string, id: number) {
    this.removeName = name;
    this.removeId = id;
    this.modalService.open(removeModalContent);
  }

  openModalEdit(editModalContent, id: number) {
    this.editId = id;
    this.modalService.open(editModalContent);
  }

  removeCategory() {
    this.categoryService.delete(this.removeId).subscribe(
      data => {
        console.log(data);
        this.ngAfterViewInit();
      }, err => {
        console.log(err);
      }
    );
  }

  onSubmit() {
    this.submitted = true;
    if (this.form.invalid || this.form.controls.name.pristine) {
      return;
    } else {
      this.modalService.dismissAll();
      this.categoryService.update(this.form.controls.name.value, this.editId).subscribe(
        data => {
          this.submitted = false;
          this.form.reset()
          this.ngAfterViewInit();
        },
        err => {
          this.submitted = false;
          this.form.reset()
          this.ngAfterViewInit();
          this.error = err.error.message;
        }
      );
    }
  }
}
