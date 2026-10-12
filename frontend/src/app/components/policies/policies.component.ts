import {
  Component,
  EventEmitter,
  Input,
  Output,
  OnChanges,
  SimpleChanges,
} from "@angular/core";

import { CommonModule } from "@angular/common";
import { FormsModule } from "@angular/forms";
import { Policy } from "../../models/api.models";
import { InrPipe } from "../../shared/format-currency.pipe";

@Component({
  selector: "app-policies",
  standalone: true,
  imports: [CommonModule, FormsModule, InrPipe],
  templateUrl: "./policies.component.html",
  styleUrl: "./policies.component.scss",
})
export class PoliciesComponent implements OnChanges {
  @Input() policies: Policy[] = [];
  @Input() customers: any[] = [];
  @Input() busy = false;
  @Input() activeAgentId = 0;

  @Output() bookPolicy = new EventEmitter<any>();
  @Output() payPolicy = new EventEmitter<Policy>();
  @Output() loadPolicies = new EventEmitter<number>();

  agentLookup = 0;

  form = {
    agentId: 0,
    customerId: 0,
    premiumAmount: 10000,
    quotationNumber: this.generateReference("QUO"),
    acdNumber: this.generateReference("ACD"),
    policyStartDate: "",
    policyEndDate: "",
    nextDueDate: "",
  };

  // Generate unique-format references using UUID.
  private generateReference(prefix: string): string {
    const uuid =
      typeof crypto !== "undefined" && typeof crypto.randomUUID === "function"
        ? crypto.randomUUID().replace(/-/g, "").toUpperCase()
        : this.fallbackId();

    return `${prefix}-${uuid}`;
  }

  private fallbackId(): string {
    return (
      Date.now().toString(36) + Math.random().toString(36).slice(2, 15)
    ).toUpperCase();
  }

  // Remove duplicate policies from the displayed list.
  get uniquePolicies(): Policy[] {
    const seen = new Set<string>();

    return this.policies.filter((policy) => {
      const policyNumber = String(policy.policyNumber ?? "").trim();

      if (!policyNumber) {
        return true;
      }

      const key = policyNumber.toUpperCase();

      if (seen.has(key)) {
        return false;
      }

      seen.add(key);
      return true;
    });
  }

  ngOnChanges(changes: SimpleChanges): void {
    if (changes["activeAgentId"]) {
      const id = Number(this.activeAgentId) || 0;

      this.agentLookup = id;
      this.form.agentId = id;
    }
  }

  submit(): void {
    const agentId = Number(this.form.agentId);
    const customerId = Number(this.form.customerId);
    const premiumAmount = Number(this.form.premiumAmount);

    if (
      !Number.isInteger(agentId) ||
      agentId <= 0 ||
      !Number.isInteger(customerId) ||
      customerId <= 0 ||
      !Number.isFinite(premiumAmount) ||
      premiumAmount <= 0 ||
      !this.form.quotationNumber ||
      !this.form.acdNumber
    ) {
      return;
    }

    this.bookPolicy.emit({
      ...this.form,
      agentId,
      customerId,
      premiumAmount,
    });
  }

  load(): void {
    const id = Number(this.agentLookup);

    if (Number.isInteger(id) && id > 0) {
      this.form.agentId = id;
      this.loadPolicies.emit(id);
    }
  }
}
