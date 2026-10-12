import { Injectable, inject } from "@angular/core";
import { HttpClient } from "@angular/common/http";
import { Observable } from "rxjs";
import {
  Account,
  BookPolicyRequest,
  CreateChildRequest,
  CreateCustomerRequest,
  CreateParentRequest,
  CreditSummary,
  Customer,
  PaymentResult,
  Policy,
  Transaction,
} from "../models/api.models";

@Injectable({ providedIn: "root" })
export class CreditApiService {
  private http = inject(HttpClient);
  private readonly base = "/api";

  createParent(body: CreateParentRequest): Observable<Account> {
    return this.http.post<Account>(`${this.base}/accounts/parents`, body);
  }
  createChild(body: CreateChildRequest): Observable<Account> {
    return this.http.post<Account>(`${this.base}/accounts/children`, body);
  }
  getCredit(userId: number): Observable<CreditSummary> {
    return this.http.get<CreditSummary>(
      `${this.base}/accounts/${userId}/credit`,
    );
  }
  getChildren(parentId: number): Observable<Account[]> {
    return this.http.get<Account[]>(
      `${this.base}/accounts/${parentId}/children`,
    );
  }
  createCustomer(body: CreateCustomerRequest): Observable<Customer> {
    return this.http.post<Customer>(`${this.base}/customers`, body);
  }
  getCustomers(): Observable<Customer[]> {
    return this.http.get<Customer[]>(`${this.base}/customers`);
  }
  getCustomer(id: number): Observable<Customer> {
    return this.http.get<Customer>(`${this.base}/customers/${id}`);
  }
  bookPolicy(body: BookPolicyRequest): Observable<Policy> {
    return this.http.post<Policy>(`${this.base}/policies`, body);
  }
  getPoliciesByAgent(agentId: number): Observable<Policy[]> {
    return this.http.get<Policy[]>(`${this.base}/policies/agent/${agentId}`);
  }
  getPoliciesByCustomer(customerId: number): Observable<Policy[]> {
    return this.http.get<Policy[]>(
      `${this.base}/policies/customer/${customerId}`,
    );
  }
  payPolicy(
    policyId: number,
    idempotencyKey: string,
  ): Observable<PaymentResult> {
    return this.http.post<PaymentResult>(`${this.base}/payments`, {
      policyId,
      idempotencyKey,
    });
  }
  getTransactionsByAgent(agentId: number): Observable<Transaction[]> {
    return this.http.get<Transaction[]>(
      `${this.base}/transactions/agent/${agentId}`,
    );
  }
  getTransactionsByCustomer(customerId: number): Observable<Transaction[]> {
    return this.http.get<Transaction[]>(
      `${this.base}/transactions/customer/${customerId}`,
    );
  }

  getAccounts(): Observable<Account[]> {
    return this.http.get<Account[]>(`${this.base}/accounts`);
  }
}
