import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { MatTableDataSource } from '@angular/material/table';
import { Router } from '@angular/router';
import { NgxUiLoaderService } from 'ngx-ui-loader';
import { CategoryService } from 'src/app/services/category.service';
import { ProductService } from 'src/app/services/product.service';
import { SendMessageService } from 'src/app/services/send-message.service';
import { GlobalConstants } from 'src/app/shared/global-constants';

@Component({
  selector: 'app-manage-product',
  templateUrl: './manage-product.component.html',
  styleUrls: ['./manage-product.component.scss']
})
export class ManageProductComponent implements OnInit {
  datasource: any = [];
  categories:any = [];
  productForm: any = FormGroup;
  responseMessage: any;
  selectId: any;


  constructor(private productService: ProductService,
    private categoryService:CategoryService,
    private ngxService: NgxUiLoaderService,
    private sendMessage: SendMessageService,
    private router: Router, private formBuilder: FormBuilder
  ) { }

  ngOnInit(): void {

    
    this.ngxService.start();

    this.getCategories();
    this.getProducts();

    this.productForm = this.formBuilder.group({
      name: [null, [Validators.required, Validators.pattern(GlobalConstants.nameRegex)]],
      categoryId: [null, [Validators.required]],
      price: [null, [Validators.required]],
      description: [null, [Validators.required]],
      status:[true,[Validators.required]]
    });

  }

  getCategories() {
    this.categoryService.getCategories().subscribe((response: any) => {
      this.ngxService.stop();
      this.categories = response;
    }, (error: any) => {
      this.ngxService.stop();
      console.log(error.error?.message);
      if (error.error?.message) {
        this.responseMessage = error.error?.message;
      } else {
        this.responseMessage = GlobalConstants.genericErreor;
      }
      this.sendMessage.openSnackBar(this.responseMessage, GlobalConstants.error);
    })
  }

  getProducts() {
    this.productService.getProducts().subscribe((response: any) => {
      this.ngxService.stop();
      this.datasource = response;
    }, (error: any) => {
      this.ngxService.stop();
      console.log(error.error?.message);
      if (error.error?.message) {
        this.responseMessage = error.error?.message;
      } else {
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
    let formData = this.productForm.value;
    let data = {
      name: formData.name,
      categoryId: formData.categoryId,
      price: formData.price,
      description: formData.description,
      status:formData.status,
    }
    this.productService.add(data).subscribe((response: any) => {
      this.responseMessage = response.message;
      this.getProducts();
      this.sendMessage.openSnackBar(this.responseMessage, "Success");
      this.productForm.reset();
    }, (error) => {
      console.log(error);
      if (error.error?.message) {
        this.responseMessage = error.error?.message;
      } else {
        this.responseMessage = GlobalConstants.genericErreor;
      }
      this.sendMessage.openSnackBar(this.responseMessage, GlobalConstants.genericErreor);
    })
  }

  selectProduct(productId: any) {
    this.selectId = productId;
  }
  handleEditAction() {
    let formData = this.productForm.value;
    let data = {
      id: this.selectId,
      name: formData.name,
      categoryId: formData.categoryId,
      price: formData.price,
      description: formData.description,
      status:formData.status
    }
    this.productService.update(data).subscribe((response: any) => {
      this.responseMessage = response.message;
      this.getProducts();
      this.sendMessage.openSnackBar(this.responseMessage, "Success")
    }, (error) => {
      console.log(error);
      if (error.error?.message) {
        this.responseMessage = error.error?.message;
      } else {
        this.responseMessage = GlobalConstants.genericErreor;
      }
      this.sendMessage.openSnackBar(this.responseMessage, GlobalConstants.genericErreor);
    })
  }

  handleDeleteAction() { 
    let id = this.selectId

    console.log(this.selectId)
    this.productService.delete(this.selectId).subscribe((response:any)=>{
      this.responseMessage = response.message;
      this.getProducts();
      this.sendMessage.openSnackBar(this.responseMessage,"Deleted successfuly");
    },(error)=>{
      console.log(error);
      if(error.error?.message){
        this.responseMessage = error.error?.message;
      }else{
        this.responseMessage = error.error?.message;
      }
      this.sendMessage.openSnackBar(this.responseMessage,GlobalConstants.genericErreor);
    })
  }

  onChange(status: any) {
    this.ngxService.start();
    var data = {
      status :status.toString(),
      id : this.selectId,
    }

    this.productService.updateStatus(data).subscribe((response:any)=>{
      this.ngxService.stop();
      this.responseMessage = response?.message;
      this.sendMessage.openSnackBar(this.responseMessage,"Success")
    })
   }



}
