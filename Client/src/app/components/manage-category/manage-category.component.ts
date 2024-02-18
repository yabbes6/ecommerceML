import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { MatTableDataSource } from '@angular/material/table';
import { Router } from '@angular/router';
import { NgxUiLoaderService } from 'ngx-ui-loader';
import { CategoryService } from 'src/app/services/category.service';
import { SendMessageService } from 'src/app/services/send-message.service';
import { GlobalConstants } from 'src/app/shared/global-constants';

@Component({
  selector: 'app-manage-category',
  templateUrl: './manage-category.component.html',
  styleUrls: ['./manage-category.component.scss']
})
export class ManageCategoryComponent implements OnInit {


  displayColumns: string[] = ['name', 'edit']
  datasource: any;
  responseMessage: any;
  nameForm: any = FormGroup;
  selectId: any;
  nameselect: any;


  constructor(
    private categoryService: CategoryService,
    private ngxService: NgxUiLoaderService,
    private sendMessage: SendMessageService,
    private router: Router, private formBuilder: FormBuilder) { }

  ngOnInit(): void {
    this.tableData();
    this.ngxService.start();

    this.nameForm = this.formBuilder.group({
      name: [null, [Validators.required]],
    });
  }

  tableData() {
    this.categoryService.getCategories().subscribe((response: any) => {
      this.ngxService.stop();
      this.datasource = new MatTableDataSource(response);
    }, (error: any) => {
      this.ngxService.stop();
      console.log(error.error?.message);
      if (error.error?.message) {
        this.responseMessage = error.error?.message;
      }
      else {
        this.responseMessage = GlobalConstants.genericErreor;
      }
      this.sendMessage.openSnackBar(this.responseMessage, GlobalConstants.error);
    })
  }

  applyFilter(event: Event) {
    const filterValue = (event.target as HTMLInputElement).value;
    this.datasource.filter = filterValue.trim().toLowerCase();
  }

  handleAddAction() {
    var formData = this.nameForm.value;
    var data = {
      name: formData.name,
    }
    this.categoryService.add(data).subscribe((response: any) => {
      this.tableData();
      this.ngxService.stop();
      this.responseMessage = response?.message;
      this.nameForm.reset();
      this.sendMessage.openSnackBar(this.responseMessage, 'success');
    }, (error: any) => {
      this.ngxService.stop();
      if (error.error?.message) {
        this.responseMessage = error.error?.message;
      } else this.responseMessage = GlobalConstants.genericErreor;

      this.sendMessage.openSnackBar(this.responseMessage, GlobalConstants.error)
    })
  }

  selectCategory(categoryId: any, name: any) {
    this.selectId = categoryId;
    this.nameselect = name
  }

  handleEditAction() {
    var formData = this.nameForm.value;
    var dat = {
      id: this.selectId,
      name: formData.name,
    }
    console.log(dat.id, dat.name)


    this.categoryService.update(dat).subscribe((response: any) => {
      this.ngxService.stop();
      this.responseMessage = response?.message;
      this.tableData();
      this.nameForm.reset();
      this.sendMessage.openSnackBar(this.responseMessage, 'updated successfuly');
    }, (error: any) => {
      this.ngxService.stop();
      if (error.error?.message) {
        this.responseMessage = error.error?.message;
      } else this.responseMessage = GlobalConstants.genericErreor;

      this.sendMessage.openSnackBar(this.responseMessage, GlobalConstants.error)
    })
  }

  validation() {
    if (this.nameForm.controls["name"].value === null || this.nameForm.controls["name"].value === "")
      return false;
    else
      return true;
  }

}
