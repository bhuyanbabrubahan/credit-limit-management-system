import { Component, OnInit, inject } from "@angular/core";
import { CommonModule } from "@angular/common";
import { HttpErrorResponse } from "@angular/common/http";
import { finalize } from "rxjs";

import { CreditApiService } from "./services/credit-api.service";

import {
  Account,
  Customer,
  Policy,
  Transaction,
  CreditSummary,
} from "./models/api.models";

import { DashboardComponent } from "./components/dashboard/dashboard.component";
import { AccountsComponent } from "./components/accounts/accounts.component";
import { CustomersComponent } from "./components/customers/customers.component";
import { PoliciesComponent } from "./components/policies/policies.component";
import { PaymentsComponent } from "./components/payments/payments.component";

type Page =
  | "dashboard"
  | "accounts"
  | "customers"
  | "policies"
  | "payments";

@Component({
  selector: "app-root",
  standalone: true,
  imports: [
    CommonModule,
    DashboardComponent,
    AccountsComponent,
    CustomersComponent,
    PoliciesComponent,
    PaymentsComponent,
  ],
  templateUrl: "./app.component.html",
  styleUrl: "./app.component.scss",
})
export class AppComponent implements OnInit {
  private api = inject(CreditApiService);

  page: Page = "dashboard";
  busy = false;
  error = "";
  notice = "";

  accounts: Account[] = [];
  children: Account[] = [];
  customers: Customer[] = [];
  policies: Policy[] = [];
  transactions: Transaction[] = [];

  selectedCredit: CreditSummary | null = null;

  activeAgentId = 0;
  sidebarOpen = false;

  navItems: {
    id: Page;
    label: string;
    icon: string;
    group: string;
  }[] = [
    {
      id: "dashboard",
      label: "Overview",
      icon: "▦",
      group: "WORKSPACE",
    },
    {
      id: "accounts",
      label: "Credit accounts",
      icon: "◫",
      group: "MANAGEMENT",
    },
    {
      id: "customers",
      label: "Customers",
      icon: "♙",
      group: "MANAGEMENT",
    },
    {
      id: "policies",
      label: "Policy booking",
      icon: "▤",
      group: "OPERATIONS",
    },
    {
      id: "payments",
      label: "Payments",
      icon: "↗",
      group: "OPERATIONS",
    },
  ];

  ngOnInit(): void {
    this.refresh();
  }

  navigate(page: Page): void {
    this.page = page;
    this.sidebarOpen = false;
    this.error = "";
    this.notice = "";
  }

  // Reload dashboard data.
  refresh(): void {
    this.error = "";
    this.notice = "";

    this.loadCustomers();
    this.loadAccounts();
  }

  // Load accounts and the selected agent's data.
  loadAccounts(): void {
    this.api.getAccounts().subscribe({
      next: (data) => {
        this.accounts = data ?? [];

        const agents = this.accounts
          .filter(
            (account) =>
              account.role?.toUpperCase() === "CHILD" &&
              account.active !== false,
          )
          .sort((a, b) => a.id - b.id);

        const currentAgentExists = agents.some(
          (agent) => agent.id === this.activeAgentId,
        );

        if (!currentAgentExists) {
          this.activeAgentId = agents[0]?.id ?? 0;
        }

        if (this.activeAgentId > 0) {
          this.loadPolicies(this.activeAgentId, false);
        } else {
          this.policies = [];
          this.transactions = [];
        }

        // Refresh the selected credit summary if one is displayed.
        if (this.selectedCredit) {
          const accountStillExists = this.accounts.some(
            (account) => account.id === this.selectedCredit?.userId,
          );

          if (accountStillExists) {
            this.loadCreditSummary(this.selectedCredit.userId);
          } else {
            this.selectedCredit = null;
          }
        }
      },
      error: (err) => this.handleError(err),
    });
  }

  loadCustomers(): void {
    this.api.getCustomers().subscribe({
      next: (data) => {
        this.customers = data ?? [];
      },
      error: (err) => this.handleError(err),
    });
  }

  loadPolicies(agentId: number, showMessage = true): void {
    const id = Number(agentId);

    if (!Number.isInteger(id) || id <= 0) {
      this.policies = [];
      this.transactions = [];
      this.activeAgentId = 0;
      return;
    }

    this.activeAgentId = id;

    this.api.getPoliciesByAgent(id).subscribe({
      next: (data) => {
        this.policies = data ?? [];

        if (showMessage) {
          this.notice =
            `Loaded ${this.policies.length} policies for agent ${id}.`;
        }
      },
      error: (err) => this.handleError(err),
    });

    // Refresh recent payments for the same agent.
    this.loadTransactions(id, false);
  }

  loadTransactions(agentId: number, showMessage = true): void {
    const id = Number(agentId);

    if (!Number.isInteger(id) || id <= 0) {
      this.transactions = [];
      return;
    }

    this.activeAgentId = id;

    this.api.getTransactionsByAgent(id).subscribe({
      next: (data) => {
        this.transactions = data ?? [];

        if (showMessage) {
          this.notice =
            `Loaded ${this.transactions.length} transactions for agent ${id}.`;
        }
      },
      error: (err) => this.handleError(err),
    });
  }

  loadTransactionsForCustomer(id: number): void {
    this.api.getTransactionsByCustomer(id).subscribe({
      next: (data) => {
        this.transactions = data ?? [];
        this.notice =
          `Loaded ${this.transactions.length} transactions for customer ${id}.`;
      },
      error: (err) => this.handleError(err),
    });
  }

  loadChildren(parentId: number): void {
    this.api.getChildren(parentId).subscribe({
      next: (data) => {
        this.children = data ?? [];
        this.notice =
          `Loaded ${this.children.length} child accounts for parent ${parentId}.`;
      },
      error: (err) => this.handleError(err),
    });
  }

  lookupCredit(id: number): void {
    this.loadCreditSummary(id);
  }

  private loadCreditSummary(id: number): void {
    this.api.getCredit(id).subscribe({
      next: (data) => {
        this.selectedCredit = data;
        this.upsertAccountFromCredit(data);
      },
      error: (err) => this.handleError(err),
    });
  }

  createParent(body: any): void {
    this.busy = true;
    this.error = "";

    this.api
      .createParent(body)
      .pipe(finalize(() => (this.busy = false)))
      .subscribe({
        next: (account) => {
          this.addAccount(account);

          this.notice =
            `Parent account created successfully. New account ID: ${account.id}`;

          this.loadAccounts();
          this.lookupCredit(account.id);
        },
        error: (err) => this.handleError(err),
      });
  }

  createChild(body: any): void {
    this.busy = true;
    this.error = "";

    this.api
      .createChild(body)
      .pipe(finalize(() => (this.busy = false)))
      .subscribe({
        next: (account) => {
          this.addAccount(account);

          this.notice =
            `Child account created successfully. New account ID: ${account.id}`;

          this.loadChildren(Number(body.parentId));
          this.loadAccounts();

          // Refresh the parent credit summary if it is currently selected.
          if (this.selectedCredit?.userId === Number(body.parentId)) {
            this.lookupCredit(Number(body.parentId));
          }
        },
        error: (err) => this.handleError(err),
      });
  }

  createCustomer(body: any): void {
    this.busy = true;
    this.error = "";

    this.api
      .createCustomer(body)
      .pipe(finalize(() => (this.busy = false)))
      .subscribe({
        next: (customer) => {
          this.customers = [
            customer,
            ...this.customers.filter(
              (item) => item.id !== customer.id,
            ),
          ];

          this.notice =
            `Customer created successfully. Customer ID: ${customer.id}`;
        },
        error: (err) => this.handleError(err),
      });
  }

  bookPolicy(body: any): void {
    this.busy = true;
    this.error = "";

    this.api
      .bookPolicy(body)
      .pipe(finalize(() => (this.busy = false)))
      .subscribe({
        next: (policy) => {
          this.activeAgentId = Number(body.agentId);

          this.policies = [
            policy,
            ...this.policies.filter(
              (item) => item.id !== policy.id,
            ),
          ];

          this.notice =
            `Policy booking completed. Policy ID: ${policy.id} (${policy.policyNumber}).`;

          // Reload policies, transactions and account credit information.
          this.loadAccounts();
        },
        error: (err) => this.handleError(err),
      });
  }

  payPolicy(policy: Policy): void {
    if (
      !confirm(
        `Run simulated payment for policy ${policy.policyNumber}, outstanding ${policy.outstanding}?`,
      )
    ) {
      return;
    }

    const key = `ui-${policy.id}-${Date.now()}`;

    this.busy = true;
    this.error = "";

    this.api
      .payPolicy(policy.id, key)
      .pipe(finalize(() => (this.busy = false)))
      .subscribe({
        next: (result) => {
          this.notice =
            `Payment result: ${result.status}. ${result.message || ""}`;

          // Reload all data affected by the payment.
          this.loadPolicies(policy.agentId, false);
          this.loadAccounts();
        },
        error: (err) => this.handleError(err),
      });
  }

  private addAccount(account: Account): void {
    this.accounts = [
      account,
      ...this.accounts.filter(
        (item) => item.id !== account.id,
      ),
    ];
  }

  private upsertAccountFromCredit(credit: CreditSummary): void {
    const current = this.accounts.find(
      (account) => account.id === credit.userId,
    );

    const account: Account = {
      id: credit.userId,
      username: credit.username,
      fullName: current?.fullName || credit.username,
      role: credit.role,
      parentId: current?.parentId,
      creditLimit: credit.totalLimit,
      allocatedToChildren: credit.allocatedToChildren,
      usedLimit: credit.usedLimit,
      availableLimit: credit.availableLimit,
      active: current?.active ?? true,
      effectiveFrom: current?.effectiveFrom,
    };

    this.addAccount(account);
  }

  private handleError(err: HttpErrorResponse | any): void {
    const backend = err?.error;

    this.error =
      typeof backend === "string"
        ? backend
        : backend?.message ||
          backend?.detail ||
          err?.message ||
          "Request failed. Check that the Spring Boot API is running on port 8080.";
  }
}