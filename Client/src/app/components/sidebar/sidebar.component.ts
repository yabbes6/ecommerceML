import { Component, OnInit } from '@angular/core';
import { jwtDecode } from 'jwt-decode';
import { NgxUiLoaderService } from 'ngx-ui-loader';
import { MenuItems } from 'src/app/shared/menu-items';

@Component({
  selector: 'app-sidebar',
  templateUrl: './sidebar.component.html',
  styleUrls: ['./sidebar.component.scss']
})
export class SidebarComponent implements OnInit {

  userRole : any;
  token:any = localStorage.getItem('token');
  tokenPayload:any;

  constructor(public menuItems:MenuItems,private ngxService:NgxUiLoaderService) {
    this.tokenPayload = jwtDecode(this.token);
    this.userRole=this.tokenPayload?.role;

   }

  ngOnInit(): void {

  }

}
