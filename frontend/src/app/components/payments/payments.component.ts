import { Component, EventEmitter, Input, Output } from "@angular/core";
import { CommonModule } from "@angular/common";
import { FormsModule } from "@angular/forms";
import { Transaction } from "../../models/api.models";
import { InrPipe } from "../../shared/format-currency.pipe";

@Component({
  selector: "app-payments",
  standalone: true,
  imports: [CommonModule, FormsModule, InrPipe],
  templateUrl: "./payments.component.html",
  styleUrl: "./payments.component.scss",
})
export class PaymentsComponent {
  @Input() transactions: Transaction[] = [];
  @Input() busy = false;
  @Output() loadAgent = new EventEmitter<number>();
  @Output() loadCustomer = new EventEmitter<number>();
  agentId = 1;
  customerId = 1;
  mode: "agent" | "customer" = "agent";
  get successCount(): number {
    return this.transactions.filter((t) => t.status === "SUCCESS").length;
  }
  get totalValue(): number {
    return this.transactions.reduce((sum, t) => sum + Number(t.amount || 0), 0);
  }
  load(): void {
    if (this.mode === "agent" && this.agentId > 0)
      this.loadAgent.emit(this.agentId);
    else if (this.mode === "customer" && this.customerId > 0)
      this.loadCustomer.emit(this.customerId);
  }
}
