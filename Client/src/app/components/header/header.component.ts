import { Component, OnInit } from '@angular/core';
import { MatDialog, MatDialogConfig } from '@angular/material/dialog';
import { Router } from '@angular/router';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { UserService } from 'src/app/services/user.service';
import { NgxUiLoaderService } from 'ngx-ui-loader';
import { SendMessageService } from 'src/app/services/send-message.service';
import { GlobalConstants } from 'src/app/shared/global-constants';
import { jwtDecode } from 'jwt-decode';
import { MenuItems } from 'src/app/shared/menu-items';

@Component({
  selector: 'app-header',
  templateUrl: './header.component.html',
  styleUrls: ['./header.component.scss']
})
export class HeaderComponent implements OnInit {

  role: any;
  data = {
    message: 'logout',
    confirmation: true,
  };

  oldPassword = true;
  newPassword = true;
  confirmPassword = true;
  changePasswordForm: any = FormGroup;
  responseMessage: any;


  userRole: any;
  token: any = localStorage.getItem('token');
  tokenPayload: any;


  constructor(
    private router: Router,
    private formBuilder: FormBuilder,
    private userService: UserService,
    private ngxService: NgxUiLoaderService,
    private sendMessage: SendMessageService,public menuItems:MenuItems
  ) {
    this.tokenPayload = jwtDecode(this.token);
    this.userRole = this.tokenPayload?.role;
  }

  ngOnInit(): void {
    this.changePasswordForm = this.formBuilder.group({
      oldPassword: [null, Validators.required],
      newPassword: [null, Validators.required],
      confirmPassword: [null, Validators.required],
    });

  }

  validatorSubmit() {
    if (this.changePasswordForm.controls['newPassword'].value != this.changePasswordForm.controls['confirmPassword'].value) {
      return true;
    } else {
      return false;
    }
  }

  handlePasswordChangeSubmit() {
    this.ngxService.start();
    var formData = this.changePasswordForm.value;
    var data = {
      oldPassword: formData.oldPassword,
      newPassword: formData.newPassword,
      confirmPassword: formData.confirmPassword
    }
    this.userService.changePassword(data).subscribe((response: any) => {
      this.ngxService.stop();
      this.responseMessage = response?.message;
      this.sendMessage.openSnackBar(this.responseMessage, 'success');
    }, error => {
      console.log(error);
      this.ngxService.stop();
      if (error.error?.message) {
        this.responseMessage = error.error?.message;
      } else this.responseMessage = GlobalConstants.genericErreor;

      this.sendMessage.openSnackBar(this.responseMessage, GlobalConstants.error)
    })
  }

  logout() {
    localStorage.clear();
    this.router.navigate(['/'])
  }


}
