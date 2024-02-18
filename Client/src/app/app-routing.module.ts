import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { HomeComponent } from './components/home/home.component';
import { LoginComponent } from './components/login/login.component';
import { SignupComponent } from './components/signup/signup.component';
import { DashboardComponent } from './components/dashboard/dashboard.component';
import { RouteGuardService } from './services/route-guard.service';
import { NavbarComponent } from './components/UI/navbar/navbar.component';
import { ManageCategoryComponent } from './components/manage-category/manage-category.component';
import { SidebarComponent } from './components/sidebar/sidebar.component';
import { HeaderComponent } from './components/header/header.component';
import { ManageProductComponent } from './components/manage-product/manage-product.component';
import { ManageOrderComponent } from './components/manage-order/manage-order.component';

const routes: Routes = [
  { path: '', component: HomeComponent },

  { path: "login", component: LoginComponent },
  { path: 'signup', component: SignupComponent },
  {
    path: 'header',
    component: HeaderComponent,
    canActivate: [RouteGuardService],
    data: { expectedRole: ['admin', 'user'] },
  },

  {
    path: "category",
    component: ManageCategoryComponent,
    canActivate:[RouteGuardService],
    data:{
      expectedRole:['admin']
    }
  },
  {
    path: "dashboard",
    component: DashboardComponent,
    canActivate:[RouteGuardService],
    data:{
      expectedRole:['admin','user']
    }
  },
  {
    path: "product",
    component: ManageProductComponent,
    canActivate:[RouteGuardService],
    data:{
      expectedRole:['admin']
    }
  },
  {
    path: "order",
    component: ManageOrderComponent,
    canActivate:[RouteGuardService],
    data:{
      expectedRole:['admin','user']
    }
  }

];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule { }
