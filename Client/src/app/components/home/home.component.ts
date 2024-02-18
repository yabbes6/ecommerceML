import { Component, OnInit } from '@angular/core';
import { MatDialog, MatDialogConfig } from '@angular/material/dialog';
import { SignupComponent } from '../signup/signup.component';
import { UserService } from 'src/app/services/user.service';
import { Router } from '@angular/router';
import { ProductService } from 'src/app/services/product.service';
import { NgxUiLoaderService } from 'ngx-ui-loader';
import { SendMessageService } from 'src/app/services/send-message.service';
import { GlobalConstants } from 'src/app/shared/global-constants';

@Component({
  selector: 'app-home',
  templateUrl: './home.component.html',
  styleUrls: ['./home.component.scss']
})
export class HomeComponent implements OnInit {

  products:any
  responseMessage:any

  constructor(
    private dialog: MatDialog,
    private userServices:UserService,
    private router:Router,
    private productService:ProductService,
    private ngxService:NgxUiLoaderService,
    private sendMessage:SendMessageService
    ) { }

  ngOnInit(): void {
    this.userServices.checkToken().subscribe((response:any)=>{
      this.router.navigate(['/dashboard']);
    },(error:any)=>{
      console.log(error);
    });

    this.productService.getProducts().subscribe((response: any) => {
      this.ngxService.stop();
      this.products = response;
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


  handleSignupAction(){
    const dialogConfig = new MatDialogConfig();
    dialogConfig.width = "550px";
    dialogConfig.position = { top: '15%', left: '35%' };
    this.dialog.open(SignupComponent,dialogConfig);
  }
}
