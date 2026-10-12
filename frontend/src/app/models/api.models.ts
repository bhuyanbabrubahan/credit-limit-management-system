export interface Account {
  id: number;
  username: string;
  fullName: string;
  role: string;
  parentId?: number | null;
  creditLimit: number;
  allocatedToChildren: number;
  usedLimit: number;
  availableLimit: number;
  active: boolean;
  effectiveFrom?: string;
}
export interface CreditSummary {
  userId: number;
  username: string;
  role: string;
  totalLimit: number;
  allocatedToChildren: number;
  usedLimit: number;
  availableLimit: number;
}
export interface Customer {
  id: number;
  customerCode: string;
  fullName: string;
  email: string;
  mobile: string;
  dateOfBirth: string;
  address: string;
  status: string;
}
export interface Policy {
  id: number;
  policyNumber: string;
  quotationNumber: string;
  acdNumber: string;
  agentId: number;
  agentName: string;
  customerId: number;
  customerCode: string;
  customerName: string;
  premiumAmount: number;
  paidAmount: number;
  outstanding: number;
  policyStartDate: string;
  policyEndDate: string;
  nextDueDate: string;
  status: string;
}
export interface Transaction {
  transactionId: string;
  policyNumber: string;
  customerCode: string;
  agentId: number;
  amount: number;
  paidAmount: number;
  status: string;
  gatewayReference?: string;
  failureReason?: string;
  paymentDate?: string;
}
export interface PaymentResult {
  transactionId: string;
  policyNumber: string;
  status: string;
  amount: number;
  paidAmount: number;
  gatewayReference?: string;
  message: string;
  paymentDate?: string;
}
export interface CreateParentRequest {
  username: string;
  fullName: string;
  creditLimit: number;
  effectiveFrom: string;
}
export interface CreateChildRequest {
  username: string;
  fullName: string;
  parentId: number;
  creditLimit: number;
}
export interface CreateCustomerRequest {
  customerCode: string;
  fullName: string;
  email: string;
  mobile: string;
  dateOfBirth: string;
  address: string;
}
export interface BookPolicyRequest {
  agentId: number;
  customerId: number;
  premiumAmount: number;
  quotationNumber: string;
  acdNumber: string;
  policyStartDate: string;
  policyEndDate: string;
  nextDueDate: string;
}
