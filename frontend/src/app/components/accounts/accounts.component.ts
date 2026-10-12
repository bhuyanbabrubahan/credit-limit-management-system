import { Component, EventEmitter, Input, Output } from "@angular/core";
import { CommonModule } from "@angular/common";
import { FormsModule } from "@angular/forms";
import { Account } from "../../models/api.models";
import { InrPipe } from "../../shared/format-currency.pipe";

@Component({
  selector: "app-accounts",
  standalone: true,
  imports: [CommonModule, FormsModule, InrPipe],
  templateUrl: "./accounts.component.html",
  styleUrl: "./accounts.component.scss",
})
export class AccountsComponent {
  @Input() accounts: Account[] = [];
  @Input() children: Account[] = [];
  @Input() busy = false;
  @Input() error = "";

  @Output() createParent = new EventEmitter<any>();
  @Output() createChild = new EventEmitter<any>();
  @Output() loadChildren = new EventEmitter<number>();

  parent = {
    username: "",
    fullName: "",
    creditLimit: 500000,
    effectiveFrom: new Date().toISOString().slice(0, 10),
  };

  child = {
    username: "",
    fullName: "",
    parentId: 0,
    creditLimit: 100000,
  };

  parentLookup = 0;

  // Merge accounts and children, removing duplicate IDs.
  get uniqueAccounts(): Account[] {
    const accountMap = new Map<number, Account>();

    for (const account of [...this.accounts, ...this.children]) {
      const id = Number(account.id);

      if (Number.isFinite(id) && id > 0) {
        accountMap.set(id, account);
      }
    }

    return Array.from(accountMap.values());
  }

  submitParent(): void {
    this.createParent.emit({ ...this.parent });
  }

  submitChild(): void {
    this.createChild.emit({ ...this.child });
  }

  lookupChildren(): void {
    const parentId = Number(this.parentLookup);

    if (Number.isInteger(parentId) && parentId > 0) {
      this.loadChildren.emit(parentId);
    }
  }
}
