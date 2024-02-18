import { NgModule } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';

import { AppRoutingModule } from './app-routing.module';
import { AppComponent } from './app.component';
import { HTTP_INTERCEPTORS, HttpClientModule } from '@angular/common/http';
import { HomeComponent } from './components/home/home.component';
import { SignupComponent } from './components/signup/signup.component';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { MatAutocompleteModule } from '@angular/material/autocomplete';
import { MatButtonModule } from '@angular/material/button';
import { MatButtonToggleModule } from '@angular/material/button-toggle';
import { MatCardModule } from '@angular/material/card';
import { MatCheckboxModule } from '@angular/material/checkbox';
import { MatChipsModule } from '@angular/material/chips';
import { MatDatepickerModule } from '@angular/material/datepicker';
import { MatDialogModule } from '@angular/material/dialog';
import { MatExpansionModule } from '@angular/material/expansion';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatGridListModule } from '@angular/material/grid-list';
import { MatIconModule } from '@angular/material/icon';
import { MatInputModule } from '@angular/material/input';
import { MatListModule } from '@angular/material/list';
import { MatMenuModule } from '@angular/material/menu';
import { MatPaginatorModule } from '@angular/material/paginator';
import { MatProgressBarModule } from '@angular/material/progress-bar';
import { MatProgressSpinnerModule } from '@angular/material/progress-spinner';
import { MatRadioModule } from '@angular/material/radio';
import { MatSelectModule } from '@angular/material/select';
import { MatSidenavModule } from '@angular/material/sidenav';
import { MatSliderModule } from '@angular/material/slider';
import { MatSlideToggleModule } from '@angular/material/slide-toggle';
import { MatSnackBarModule } from '@angular/material/snack-bar';
import { MatSortModule } from '@angular/material/sort';
import { MatTableModule } from '@angular/material/table';
import { MatTabsModule } from '@angular/material/tabs';
import { MatToolbarModule } from '@angular/material/toolbar';
import { MatTooltipModule } from '@angular/material/tooltip';
import { MatStepperModule } from '@angular/material/stepper';
import { MatBadgeModule } from '@angular/material/badge';
import { MatNativeDateModule, MatRippleModule } from '@angular/material/core';
import { MatBottomSheetModule } from '@angular/material/bottom-sheet';
import { CdkTableModule } from '@angular/cdk/table';
import { CdkAccordionModule } from '@angular/cdk/accordion';
import { A11yModule } from '@angular/cdk/a11y';
import { BidiModule } from '@angular/cdk/bidi';
import { OverlayModule } from '@angular/cdk/overlay';
import { PlatformModule } from '@angular/cdk/platform';
import { ObserversModule } from '@angular/cdk/observers';
import { PortalModule } from '@angular/cdk/portal';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';
import { NgxUiLoaderConfig, NgxUiLoaderModule, SPINNER } from 'ngx-ui-loader';
import { LoginComponent } from './components/login/login.component';
import { FlexLayoutModule } from '@angular/flex-layout';
import { DashboardComponent } from './components/dashboard/dashboard.component';
import { TokenInterceptorInterceptor } from './interceptors/token-interceptor.interceptor';
import { MenuItems } from './shared/menu-items';
import { HeaderComponent } from './components/header/header.component';
import { NavbarComponent } from './components/UI/navbar/navbar.component';
import { SidebarComponent } from './components/sidebar/sidebar.component';
import { ManageCategoryComponent } from './components/manage-category/manage-category.component';
import { CategoryComponent } from './components/category/category.component';
import { ManageProductComponent } from './components/manage-product/manage-product.component';
import { ManageBillComponent } from './components/manage-bill/manage-bill.component';
import { ManageOrderComponent } from './components/manage-order/manage-order.component';


const ngxUiLoaderConfig: NgxUiLoaderConfig = {
  text: "Loading...",
  textColor: "#FFFFF",
  textPosition: "center-center",
  bgsColor: "#ffa522",
  fgsColor: "#ffa522",
  fgsType: SPINNER.squareJellyBox,
  fgsSize: 100,
  hasProgressBar: false
}

@NgModule({
  declarations: [
    AppComponent,
    HomeComponent,
    SignupComponent,
    LoginComponent,
    DashboardComponent,
    SidebarComponent,
    HeaderComponent,
    NavbarComponent,
    ManageCategoryComponent,
    CategoryComponent,
    ManageProductComponent,
    ManageBillComponent,
    ManageOrderComponent
  ],
  imports: [
    BrowserModule,
    AppRoutingModule,
    MatButtonToggleModule,
    MatProgressBarModule,
    MatPaginatorModule,
    MatProgressSpinnerModule,
    MatRadioModule,
    MatSelectModule,
    MatSidenavModule,
    MatSliderModule,
    MatSlideToggleModule,
    MatCardModule,
    FlexLayoutModule,
    HttpClientModule,
    MatTooltipModule,
    MatStepperModule,
    MatBadgeModule,
    MatTableModule,
    MatCheckboxModule,
    MatSortModule,
    MatBottomSheetModule,
    MatNativeDateModule,
    MatChipsModule,
    MatTabsModule,
    MatRippleModule,
    MatToolbarModule,
    MatDatepickerModule,
    MatIconModule,
    FormsModule,
    A11yModule,
    OverlayModule,
    BidiModule,
    MatMenuModule,
    PlatformModule,
    PortalModule,
    ObserversModule,
    CdkTableModule,
    CdkAccordionModule,
    MatAutocompleteModule,
    MatExpansionModule,
    MatInputModule,
    MatListModule,
    MatFormFieldModule,
    ReactiveFormsModule,
    MatGridListModule,
    MatButtonModule,
    BrowserAnimationsModule,
    MatDialogModule,
    MatSnackBarModule,
    NgxUiLoaderModule.forRoot(ngxUiLoaderConfig)

  ],
  providers: [
    HttpClientModule,
    { provide: HTTP_INTERCEPTORS, useClass: TokenInterceptorInterceptor, multi: true },
    MenuItems],
  bootstrap: [AppComponent]
})
export class AppModule { }
