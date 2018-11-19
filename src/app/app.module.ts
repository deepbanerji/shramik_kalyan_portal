import { NgModule }      from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';
import { FormsModule } from '@angular/forms';
import { HttpModule } from '@angular/http';

import { AppComponent }  from './app.component';
import { UserComponent } from './user.component';
import { NationalCodePipe } from './nationalcode.pipe';

import { UserService } from './user.service';

@NgModule({
  imports:      [ BrowserModule, FormsModule, HttpModule ],
  declarations: [ AppComponent, UserComponent, NationalCodePipe ],
  providers : 	[ UserService ],
  bootstrap:    [ AppComponent ]
})
export class AppModule { }
