import { Injectable } from '@angular/core';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { AuthService } from './auth.service';
import { SendMessageService } from './send-message.service';
import { jwtDecode } from 'jwt-decode';
import { GlobalConstants } from '../shared/global-constants';

@Injectable({
  providedIn: 'root'
})
export class RouteGuardService {

  constructor(
    public router: Router,
    public auth: AuthService,
    private sendMessage: SendMessageService) { }

  canActivate(router: ActivatedRouteSnapshot):boolean {
    const expectedRoleArray: string[] = router.data['expectedRole'];
    console.log(expectedRoleArray)
    //return true;
    const token: any = localStorage.getItem('token');
    var tokenPayload: any;
    try {
      tokenPayload = jwtDecode(token);
    }
    catch (err) {
      localStorage.clear();
      this.router.navigate(['/']);
      //+++
      //return false;
    }

    let expectedRole = '';

    for (let i = 0; i < expectedRoleArray.length; i++) {
      if (expectedRoleArray[i] === tokenPayload.role) {
        console.log(tokenPayload.role)
        expectedRole = tokenPayload.role;
      }
    }
    if (tokenPayload.role === 'user' || tokenPayload.role === 'admin') {
      if (this.auth.isAuthenticated() && tokenPayload.role == expectedRole) {
        return true;
      }
      this.router.navigate(['/dashboard']);
      return false;
    } else {
      this.sendMessage.openSnackBar(GlobalConstants.unauthorized, GlobalConstants.error);
      this.router.navigate(['/']);
      localStorage.clear();
      return false;
    }
  }
}
