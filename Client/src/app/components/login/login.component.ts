import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { MatDialog, MatDialogRef } from '@angular/material/dialog';
import { Router } from '@angular/router';
import { NgxUiLoaderService } from 'ngx-ui-loader';
import { SendMessageService } from 'src/app/services/send-message.service';
import { UserService } from 'src/app/services/user.service';
import { GlobalConstants } from 'src/app/shared/global-constants';

@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.scss']
})
export class LoginComponent implements OnInit {

  hide = true
  loginForm:any = FormGroup
  responseMessage:any;
  constructor(
    private formBuilder:FormBuilder,
    private router:Router,
    private userService:UserService,
    //public dialogRef:MatDialogRef<LoginComponent>,
    private ngxService:NgxUiLoaderService,
    private sendMessage:SendMessageService
    ) { }

  ngOnInit(): void {
    this.loginForm = this.formBuilder.group({
      email:[null,[Validators.required,Validators.pattern(GlobalConstants.emailRegex)]],
      password:[null,[Validators.required]]
    })
    this.ngxService.stop();

  }

  handleSubmit(){
    this.ngxService.start();
    var formData = this.loginForm.value;
    var data = {
      email:formData.email,
      password:formData.password,
    }

    this.userService.login(data).subscribe((response:any)=>{
      this.ngxService.stop();
      localStorage.setItem('token',response.token);
      this.router.navigate(['/dashboard']);
    },(error)=>{
      this.ngxService.stop();
      if(error.error?.message){
        this.ngxService.stop();
        this.responseMessage = error.error?.message;
      }
      else{
        this.ngxService.stop();
        this.responseMessage = GlobalConstants.genericErreor;
      }
      this.sendMessage.openSnackBar(this.responseMessage,GlobalConstants.error)
    })

  }

  cancelLogin(){
    this.router.navigate([''])
  }

}
