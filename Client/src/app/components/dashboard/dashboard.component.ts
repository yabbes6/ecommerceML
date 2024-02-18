import { AfterViewInit, Component, OnInit } from '@angular/core';
import { NgxUiLoaderService } from 'ngx-ui-loader';
import { DashboardService } from 'src/app/services/dashboard.service';
import { SendMessageService } from 'src/app/services/send-message.service';
import { GlobalConstants } from 'src/app/shared/global-constants';

@Component({
  selector: 'app-dashboard',
  templateUrl: './dashboard.component.html',
  styleUrls: ['./dashboard.component.scss']
})
export class DashboardComponent implements AfterViewInit {

  responseMessage: any;
  data: any;

  
  ngAfterViewInit() {}

  constructor(private dashboardService: DashboardService,
    private ngxService: NgxUiLoaderService,
    private sendmessage: SendMessageService,) {
    this.ngxService.start();
    this.dashboardData();
  }


  dashboardData() {
    this.dashboardService.getDetails().subscribe((response: any) => {
      this.ngxService.stop();
      this.data = response;
    }, (error: any) => {
      this.ngxService.stop();
      console.log(error);
      if (error.error?.message) {
        this.responseMessage = error.error?.message;
      } else {
        this.responseMessage = GlobalConstants.genericErreor;
      }
      this.sendmessage.openSnackBar(this.responseMessage, GlobalConstants.error);
    })
  }

}
