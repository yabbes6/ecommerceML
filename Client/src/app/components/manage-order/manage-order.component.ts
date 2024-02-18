import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import * as saveAs from 'file-saver';
import { NgxUiLoaderService } from 'ngx-ui-loader';
import { BillService } from 'src/app/services/bill.service';
import { CategoryService } from 'src/app/services/category.service';
import { ProductService } from 'src/app/services/product.service';
import { SendMessageService } from 'src/app/services/send-message.service';
import { GlobalConstants } from 'src/app/shared/global-constants';

@Component({
  selector: 'app-manage-order',
  templateUrl: './manage-order.component.html',
  styleUrls: ['./manage-order.component.scss']
})
export class ManageOrderComponent implements OnInit {

  displayColums: string[] = ["name", "category", "price", "Quantity", "total", "edit"]
  dataSource: any = []
  manageOrderForm: any = FormGroup
  categories: any = []
  products: any = []
  price: any = []
  totalAmount: number = 0
  responseMessage: any;

  constructor(private formBuilder: FormBuilder,
    private catgoryService: CategoryService,
    private productService: ProductService,
    private sendMessage: SendMessageService,
    private billService: BillService,
    private ngxService: NgxUiLoaderService,
  ) { }

  ngOnInit(): void {
    this.ngxService.start();
    this.getCategory();
    this.manageOrderForm = this.formBuilder.group({
      name: [null, [Validators.required, Validators.pattern(GlobalConstants.nameRegex)]],
      email: [null, [Validators.required, Validators.pattern(GlobalConstants.emailRegex)]],
      contactNumber: [null, [Validators.required, Validators.pattern(GlobalConstants.contactNumberRegex)]],
      paymentMethod: [null, [Validators.required]],
      product: [null, [Validators.required]],
      category: [null, [Validators.required]],
      quantity: [null, [Validators.required]],
      price: [null, [Validators.required]],
      total: [0, [Validators.required]]
    })
  };


  getCategory() {
    this.catgoryService.getFilterCategories().subscribe((response: any) => {
      this.ngxService.stop();
      this.categories = response;

    }, (error: any) => {
      this.ngxService.stop();
      console.log(error);
      if (error.error?.message) {
        this.responseMessage = error.error?.message;
      } else {
        this.responseMessage = GlobalConstants.genericErreor
      }
      this.sendMessage.openSnackBar(this.responseMessage, GlobalConstants.error)
    })
  }

  getProductByCategory(value: any) {
    this.productService.getProductsByCategory(value.id).subscribe((response: any) => {
      this.products = response;
      this.manageOrderForm.controls['price'].setValue('');
      this.manageOrderForm.controls['quantity'].setValue('');
      this.manageOrderForm.controls['total'].setValue(0);

    }, (error: any) => {
      console.log(error);
      if (error.error?.message) {
        this.responseMessage = error.error?.message;

      } else {
        this.responseMessage = GlobalConstants.genericErreor;
      }
      this.sendMessage.openSnackBar(this.responseMessage, GlobalConstants.error);
    })
  }

  getProductDetails(value: any) {
    this.productService.getById(value.id).subscribe((response: any) => {
      this.price = response.price;
      this.manageOrderForm.controls['price'].setValue(response.price);
      this.manageOrderForm.controls['quantity'].setValue('1');
      this.manageOrderForm.controls['total'].setValue(this.price * 1);

    }, (error) => {
      console.log(error)
      if (error.error?.message) {
        this.responseMessage = error.error?.message;
      } else {
        this.responseMessage = GlobalConstants.genericErreor
      }
      this.sendMessage.openSnackBar(this.responseMessage, GlobalConstants.error);
    })
  }

  setQuantity(value: any) {
    var temp = this.manageOrderForm.controls['quantity'].value;
    if (temp > 0) {
      this.manageOrderForm.controls['total'].setValue(this.manageOrderForm.controls['quantity'].value * this.manageOrderForm.controls['price'].value);

    } else if (temp != '') {
      this.manageOrderForm.controls['price'].setValue("1");
      this.manageOrderForm.controls['total'].setValue(this.manageOrderForm.controls['quantity'].value * this.manageOrderForm.controls['price'].value);
    }
  }

  validateProductAdd() {
    if (this.manageOrderForm.controls['total'].value === 0 || this.manageOrderForm.controls['total'].value === null || this.manageOrderForm.controls['quantity'].value <= 0) {
      return true;
    } else return false;
  }

  validateSubmit() {
    if (this.totalAmount === 0 || this.manageOrderForm.conrols['name'].value === null || this.manageOrderForm.conrols['email'].value === null || this.manageOrderForm.conrols['contactNumber'].value === null || this.manageOrderForm.conrols['paymentMethod'].value === null) {
      return true;
    } else return false;
  }

  add() {
    let formData = this.manageOrderForm.value;
    let productName = this.dataSource.find((e: { id: number }) => e.id === formData.product.id);
    if (productName === undefined) {
      this.totalAmount = this.totalAmount + formData.total;
      this.dataSource.push({ id: formData.product.id, name: formData.product.name, category: formData.category.name, quantity: formData.quantity, price: formData.price, total: formData.total });
      this.dataSource = [...this.dataSource];
      this.sendMessage.openSnackBar(GlobalConstants.productAdded, "success");
    } else {
      this.sendMessage.openSnackBar(GlobalConstants.productExistError, GlobalConstants.error);
    }
  }

  handleDeleteAction(value: any, element: any) {
    this.totalAmount = this.totalAmount - element.total;
    this.dataSource.splice(value, 1);
    this.dataSource = [...this.dataSource];
  }

  submitAction() {
    var formData = this.manageOrderForm.value
    var data = {
      name: formData.name,
      email: formData.email,
      contactNumber: formData.contactNumber,
      paymentMethod: formData.paymentMethod,
      totalAmount: this.totalAmount.toString(),
      productDetails: JSON.stringify(this.dataSource)
    }
    this.ngxService.start();
    this.billService.generateReport(data).subscribe((response: any) => {
      this.downloadFile(response?.uuid)
      this.manageOrderForm.reset();
      this.dataSource = []
      this.totalAmount = 0;
    }, (error) => {
      console.log(error)
      if (error.error?.message) {
        this.responseMessage = error.error?.message;
      } else {
        this.responseMessage = GlobalConstants.genericErreor
      }
      this.sendMessage.openSnackBar(this.responseMessage, GlobalConstants.error);
    }
    )
  }

  downloadFile(fileName: string) {
    var data = {
      uuid : fileName
    }
    this.billService.getPdf(data).subscribe((response:any)=>{
      saveAs(response,fileName + '.pdf');
      this.ngxService.stop()
    })
   }

}
