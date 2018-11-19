import { Component, Input } from '@angular/core';
import { User } from './user';
@Component({
	selector : 'user-comp',
	templateUrl : `app/view/user.component.html`,
	styleUrls : [`app/styles/user.component.css` ]
})
export class UserComponent{
	@Input() parentValue : string;
	@Input() users : User[];

	moreInfo(user : User){
		alert(user.firstName + " is working with " + user.company );
	}

	totIncome(){
		let counter = 0;
		if(Array.isArray(this.users)){
			for(let user of this.users){
				counter +=user.income;
			}
		}else{
			setTimeout(this.totIncome, 2000)
		}
		return counter;
	}
	/*Component Life Cycle*/
	constructor(){console.log("Constructor")}
	ngOnInit(){console.log("ngOnInit")};
	ngOnChanges(){console.log("ngOnChanges")};
	ngDoCheck(){console.log("ngDoCheck")};
	ngOnDestroy(){console.log("ngOnDestroy")};
	ngAfterViewInit(){console.log("ngAfterViewInit")};
	ngAfterContentInit(){console.log("ngAfterContentInit")};
	ngAfterViewChecked(){console.log("ngAfterViewChecked")};
	ngAfterContentChecked(){console.log("ngAfterContentChecked")};
}