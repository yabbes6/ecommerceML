import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { environment } from 'src/environments/environment';

@Injectable({
  providedIn: 'root'
})
export class UserService {
  url = environment.baseUrl;

  constructor(private httpclient:HttpClient) { }

  signup(data:any){
    return this.httpclient.post(this.url+
      "/user/signup",data,{
      headers:new HttpHeaders().set('Content-type','application/json')
    })
  }
  forgetPassword(data:any){
    return this .httpclient.post(this.url+
      "/user/forgetPassword",data,{
        headers:new HttpHeaders().set('Content-type','application')
      })
  }

  login(data:any){
    return this.httpclient.post(this.url+
      "/user/login",data,{
      headers:new HttpHeaders().set('Content-type','application/json')
    })
  }

  checkToken(){
    return this.httpclient.get(this.url+"/user/checkToken");
  }

  changePassword(data:any){
    return this.httpclient.post(this.url+"/user/changePassword/",data,{
      headers:new HttpHeaders().set('Content-type','application/json')
    })
  }
}
