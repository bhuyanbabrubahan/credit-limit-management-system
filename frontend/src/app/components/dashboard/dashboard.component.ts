import { Component, EventEmitter, Input, Output } from "@angular/core";
import { CommonModule } from "@angular/common";
import { FormsModule } from "@angular/forms";
import {
	Account,
	Customer,
	Policy,
	Transaction,
} from "../../models/api.models";
import { InrPipe } from "../../shared/format-currency.pipe";

@Component({
	selector: "app-dashboard",
	standalone: true,
	imports: [CommonModule, FormsModule, InrPipe],
	templateUrl: "./dashboard.component.html",
	styleUrl: "./dashboard.component.scss",
})
export class DashboardComponent {
	@Input() accounts: Account[] = [];
	@Input() customers: Customer[] = [];
	@Input() policies: Policy[] = [];
	@Input() transactions: Transaction[] = [];
	@Input() selectedCredit: any = null;
	@Input() activeAgentId = 0;

	@Output() openTab = new EventEmitter<string>();
	@Output() lookupCredit = new EventEmitter<number>();
	@Output() agentSelected = new EventEmitter<number>();

	// Duplicate account IDs remove karein.
	get uniqueAccounts(): Account[] {
		const seen = new Set<number>();

		return this.accounts.filter((account) => {
			const id = Number(account.id);

			if (!Number.isFinite(id) || id <= 0 || seen.has(id)) {
				return false;
			}

			seen.add(id);
			return true;
		});
	}

	get agents(): Account[] {
		return this.uniqueAccounts.filter(
			(account) =>
				account.role?.toUpperCase() === "CHILD" && account.active !== false,
		);
	}

	// Organisation limit: parent accounts only.
	get totalLimit(): number {
		const parents = this.uniqueAccounts.filter(
			(account) => account.role?.toUpperCase() === "PARENT",
		);

		const source = parents.length ? parents : this.uniqueAccounts;

		return source.reduce(
			(total, account) => total + Number(account.creditLimit || 0),
			0,
		);
	}

	// Used credit: unique accounts only.
	get usedLimit(): number {
		return this.uniqueAccounts.reduce(
			(total, account) => total + Number(account.usedLimit || 0),
			0,
		);
	}

	get availableLimit(): number {
		return Math.max(0, this.totalLimit - this.usedLimit);
	}

	onAgentChange(agentId: number): void {
		if (agentId > 0) {
			this.agentSelected.emit(agentId);
		}
	}
}
