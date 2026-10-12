import { Component, EventEmitter, Input, Output } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { Customer } from '../../models/api.models';

@Component({ selector:'app-customers', standalone:true, imports:[CommonModule,FormsModule], templateUrl:'./customers.component.html', styleUrl:'./customers.component.scss' })
export class CustomersComponent {
  @Input() customers: Customer[] = [];
  @Input() busy = false;
  @Output() createCustomer = new EventEmitter<any>();
  form = { customerCode:'', fullName:'', email:'', mobile:'', dateOfBirth:'', address:'' };
  search = '';
  get filtered(): Customer[] {
    const q = this.search.toLowerCase().trim();
    return this.customers.filter(c => !q || [c.fullName,c.customerCode,c.email,c.mobile].some(v => (v || '').toLowerCase().includes(q)));
  }
  submit(): void { this.createCustomer.emit({...this.form}); }
}
