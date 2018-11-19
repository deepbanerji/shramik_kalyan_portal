import { Injectable } from '@angular/core';
/*import { USER_DATA } from './mocks';*/
import { Http } from '@angular/http';
import 'rxjs/add/operator/map';
import { User } from './user';

@Injectable()
export class UserService{
	constructor(private http : Http){}
	getUserData(){
		return this.http.get("app/model/user-data.json")
			.map(response=><User[]>response.json().data);
	}
	getData(){
		this.http.get("http://time.jsontest.com")
			.map(response=>response.json())
			.subscribe(resp=>console.log(resp));
	}
}