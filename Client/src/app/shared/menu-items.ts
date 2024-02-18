import{Injectable } from "@angular/core";

export interface Menu{
    state:string ;
    name:string ;
    type:string ;
    icon:string;
    role:string;
}

const MENUITEMS = [
    {state:'dashboard' ,name:'Dashboard' ,type:'link' , icon:'fa fa-th-large',role:''},
    {state:'category' ,name:'manage Category' ,type:'link' , icon:'fa fa-columns',role:'admin'},
    {state:'product' ,name:'manage Product' ,type:'link' , icon:'fa fa-columns',role:'admin'},
    {state:'order' ,name:'manage Order' ,type:'link' , icon:'fa fa-columns',role:''},

]

@Injectable()
export class MenuItems{
    getMenuitem():Menu[]{
        return MENUITEMS;
    }
}