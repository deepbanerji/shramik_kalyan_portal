"use strict";
var __decorate = (this && this.__decorate) || function (decorators, target, key, desc) {
    var c = arguments.length, r = c < 3 ? target : desc === null ? desc = Object.getOwnPropertyDescriptor(target, key) : desc, d;
    if (typeof Reflect === "object" && typeof Reflect.decorate === "function") r = Reflect.decorate(decorators, target, key, desc);
    else for (var i = decorators.length - 1; i >= 0; i--) if (d = decorators[i]) r = (c < 3 ? d(r) : c > 3 ? d(target, key, r) : d(target, key)) || r;
    return c > 3 && r && Object.defineProperty(target, key, r), r;
};
var __metadata = (this && this.__metadata) || function (k, v) {
    if (typeof Reflect === "object" && typeof Reflect.metadata === "function") return Reflect.metadata(k, v);
};
var core_1 = require("@angular/core");
var UserComponent = (function () {
    /*Component Life Cycle*/
    function UserComponent() {
        console.log("Constructor");
    }
    UserComponent.prototype.moreInfo = function (user) {
        alert(user.firstName + " is working with " + user.company);
    };
    UserComponent.prototype.totIncome = function () {
        var counter = 0;
        if (Array.isArray(this.users)) {
            for (var _i = 0, _a = this.users; _i < _a.length; _i++) {
                var user = _a[_i];
                counter += user.income;
            }
        }
        else {
            setTimeout(this.totIncome, 2000);
        }
        return counter;
    };
    UserComponent.prototype.ngOnInit = function () { console.log("ngOnInit"); };
    ;
    UserComponent.prototype.ngOnChanges = function () { console.log("ngOnChanges"); };
    ;
    UserComponent.prototype.ngDoCheck = function () { console.log("ngDoCheck"); };
    ;
    UserComponent.prototype.ngOnDestroy = function () { console.log("ngOnDestroy"); };
    ;
    UserComponent.prototype.ngAfterViewInit = function () { console.log("ngAfterViewInit"); };
    ;
    UserComponent.prototype.ngAfterContentInit = function () { console.log("ngAfterContentInit"); };
    ;
    UserComponent.prototype.ngAfterViewChecked = function () { console.log("ngAfterViewChecked"); };
    ;
    UserComponent.prototype.ngAfterContentChecked = function () { console.log("ngAfterContentChecked"); };
    ;
    return UserComponent;
}());
__decorate([
    core_1.Input(),
    __metadata("design:type", String)
], UserComponent.prototype, "parentValue", void 0);
__decorate([
    core_1.Input(),
    __metadata("design:type", Array)
], UserComponent.prototype, "users", void 0);
UserComponent = __decorate([
    core_1.Component({
        selector: 'user-comp',
        templateUrl: "app/view/user.component.html",
        styleUrls: ["app/styles/user.component.css"]
    }),
    __metadata("design:paramtypes", [])
], UserComponent);
exports.UserComponent = UserComponent;
//# sourceMappingURL=user.component.js.map