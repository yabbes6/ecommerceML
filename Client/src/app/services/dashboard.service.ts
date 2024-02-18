import { HttpBackend, HttpClient } from '@angular/common/http';
import { EnvironmentInjector, Injectable } from '@angular/core';
import { environment } from 'src/environments/environment';

@Injectable({
  providedIn: 'root'
})
export class DashboardService {
  url = environment.baseUrl;

  constructor(private httpClient:HttpClient) { }

  getDetails(){
    return this.httpClient.get(this.url+"/dashboard/details");
  }
}
