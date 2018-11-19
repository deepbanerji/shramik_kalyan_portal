import { Pipe, PipeTransform } from '@angular/core';
@Pipe({
	name : 'nationalcode'
})
export class NationalCodePipe implements PipeTransform{
	transform(value: number, ...args : string[]){
		return "+" + args[0] + "-" + value;
	}
}