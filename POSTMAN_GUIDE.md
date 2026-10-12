# Postman Guide — Credit Limit Project

Base URL:
`http://localhost:8080`

## 1. Check Amit credit

GET
`/api/accounts/2/credit`

Expected concept:

```json
{
  "userId": 2,
  "username": "amit",
  "role": "CHILD",
  "totalLimit": 30000,
  "allocatedToChildren": 0,
  "usedLimit": 0,
  "availableLimit": 30000
}
```

---

## 2. Create another customer

POST
`/api/customers`

Body:

```json
{
  "customerCode": "CUST-1003",
  "fullName": "Rahul Sharma",
  "email": "rahul.sharma@example.com",
  "mobile": "9876543212",
  "dateOfBirth": "1994-04-20",
  "address": "Bhubaneswar, Odisha"
}
```

---

## 3. Book Rahul's policy through Amit

POST
`/api/policies`

Body:

```json
{
  "agentId": 2,
  "customerId": 1,
  "premiumAmount": 5000,
  "quotationNumber": "QUO-1001",
  "acdNumber": "ACD-1001",
  "policyStartDate": "2026-10-05",
  "policyEndDate": "2027-10-04",
  "nextDueDate": "2026-10-05"
}
```

After success:
- Amit used credit increases by ₹5,000
- Amit available credit decreases by ₹5,000
- policy has a generated policy number

---

## 4. Check Amit credit again

GET
`/api/accounts/2/credit`

Concept:

```text
Total     = 30000
Used      = 5000
Available = 25000
```

---

## 5. Rahul's policy details

GET
`/api/policies/customer/1`

This represents a backend lookup of policies belonging to Rahul.

The real customer-facing portal is not implemented in this dummy project.

---

## 6. Pay Rahul's policy

Take the policy ID returned by booking.

POST
`/api/payments`

Body:

```json
{
  "policyId": 1,
  "idempotencyKey": "PAY-RAHUL-1001"
}
```

Expected:
- transaction SUCCESS
- gateway reference generated
- paid amount recorded
- policy status becomes PAID
- Amit's used credit decreases
- Amit's available credit increases

---

## 7. Payment history for Rahul

GET
`/api/transactions/customer/1`

This returns:
- transaction ID
- policy number
- customer code
- agent
- amount
- status
- gateway reference
- payment date

---

## 8. Amit's transaction history

GET
`/api/transactions/agent/2`

---

## 9. Test idempotency

Send the same request again:

```json
{
  "policyId": 1,
  "idempotencyKey": "PAY-RAHUL-1001"
}
```

The existing transaction is returned.

No second payment transaction should be created.

---

## 10. Test payment failure

Create a policy with quotation:

`QUO-FAIL-001`

Then pay it.

The dummy gateway returns FAILED.

No credit release occurs because payment did not succeed.

---

## 11. Test insufficient credit

Amit starts with ₹30,000.

Book six policies of ₹5,000.

After the sixth:
- used = ₹30,000
- available = ₹0

Try another ₹5,000 policy.

Expected:
HTTP 400 with insufficient available credit.

---

## 12. Parent/child allocation test

Parent has ₹1,00,000.

Existing demo allocations:
- Amit = ₹30,000
- Ravi = ₹20,000
- Suresh = ₹10,000

Total allocated = ₹60,000.

Try creating a child with ₹50,000.

Expected rejection because only ₹40,000 remains unallocated.
