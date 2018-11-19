import { Component } from '@angular/core';
/*import { USER_DATA } from './mocks';*/
import { User } from './user';
import { UserService } from './user.service';

@Component({
  selector: 'my-app',
  templateUrl: `app/view/app.component.html`,
})
export class AppComponent  { 
	name = 'Angular'; 
	users : User[];

	constructor(private userService : UserService){}

	ngOnInit(){
		this.userService.getUserData()
			.subscribe(people => this.users = people);
		this.userService.getData();
	}
}
