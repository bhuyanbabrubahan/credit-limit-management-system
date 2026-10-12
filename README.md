# Credit Limit Management — Java/Spring Boot Dummy Project

## 1. What this project represents

This is a **backend-only dummy integration project** for learning and Postman testing.

Business picture:

    INSURER / BUSINESS
            |
            v
    XYZ INSURANCE SERVICES (PARENT)
            |
        +---+---+
        |       |
      Amit     Ravi        <- CHILD / AGENT
        |
        v
      Rahul                  <- CUSTOMER
        |
        v
    Policy / Premium Payment

The Credit Limit System is primarily the **partner/agent-side system**.

It does not pretend to be:
- a real insurance company's customer portal
- a real bank
- a real payment gateway
- a real data warehouse

The payment gateway and data warehouse are represented by interfaces + dummy implementations so the Java design is reusable.

---

## 2. The missing business details added

The original project had the credit/policy/payment skeleton, but customer and date information was incomplete.

This version adds:

### Customer
- customer code
- full name
- email
- mobile
- date of birth
- address
- active/inactive status

### Policy
- policy number
- quotation number
- ACD number
- agent
- customer
- premium amount
- paid amount
- outstanding amount
- policy start date
- policy end date
- next due date
- status

### Payment transaction
- transaction ID
- idempotency key
- policy
- customer
- agent
- amount
- paid amount
- gateway reference
- success/failure
- payment date
- failure reason

---

## 3. Credit limit meaning

Example:

Amit credit limit = ₹30,000

If Amit books:

1. Rahul policy = ₹5,000
   - Used = ₹5,000
   - Available = ₹25,000

2. Priya policy = ₹5,000
   - Used = ₹10,000
   - Available = ₹20,000

If six ₹5,000 policies are booked:
- Limit = ₹30,000
- Used = ₹30,000
- Available = ₹0

A seventh ₹5,000 booking is rejected.

When Rahul's outstanding ₹5,000 payment succeeds:
- Used = ₹25,000
- Available = ₹5,000

This is **not a monthly automatic reset**. The same credit capacity rotates when business rules allow the outstanding amount to be released.

---

## 4. Parent and child

Parent:
- business account
- owns the master credit limit
- can have multiple child/agent accounts
- child allocations cannot exceed parent credit

Example:

XYZ Parent = ₹1,00,000

Amit = ₹30,000
Ravi = ₹20,000
Suresh = ₹10,000

Total allocated to children = ₹60,000
Parent unallocated balance = ₹40,000

A child uses only its own allocated limit.

---

## 5. Why customer is separate from agent

Rahul is not the credit-limit user.

Amit is the operational agent.

Rahul's policy/payment details belong to the customer-facing/insurer side in a real system. This dummy project stores the customer and policy data so the backend can show the relationship.

Conceptually:

Rahul
  -> pays through insurer's authorized payment channel
  -> receives policy/receipt/reference
  -> checks policy/payment status through insurer/customer channel

Amit
  -> books policy
  -> consumes credit
  -> sees agent-side transactions/credit

Our Credit Limit System
  -> checks available credit
  -> records policy booking
  -> receives payment result
  -> releases utilized credit
  -> keeps transaction history

The customer portal itself is intentionally NOT implemented.

---

## 6. Why interfaces are used

### PaymentGateway

`PaymentGateway` is an interface.

Today:
`DummyPaymentGateway`

Future:
`RazorpayPaymentGateway`
or
`StripePaymentGateway`
or
`InsurancePaymentGateway`

The business service does not need to change its core payment flow just because the external provider changes.

This demonstrates:
- abstraction
- interface
- polymorphism
- dependency injection
- loose coupling
- Open/Closed Principle

### DataWarehousePublisher

Today:
`DummyDataWarehousePublisher`

Future:
- Kafka publisher
- REST warehouse adapter
- ETL/event publisher

No real warehouse is connected in this dummy project.

---

## 7. Project structure

```text
credit-limit-demo/
├── pom.xml
├── README.md
├── POSTMAN_GUIDE.md
├── src/main/resources/
│   └── application.properties
├── src/main/java/com/example/creditlimit/
│   ├── CreditLimitDemoApplication.java
│   ├── config/
│   │   └── DataInitializer.java
│   ├── controller/
│   │   ├── CreditController.java
│   │   ├── CustomerController.java
│   │   ├── PolicyController.java
│   │   └── TransactionController.java
│   ├── dto/
│   ├── entity/
│   ├── exception/
│   ├── gateway/
│   ├── mapper/
│   ├── repository/
│   ├── service/
│   │   └── impl/
│   └── warehouse/
└── src/test/
```

---

## 8. Important Java/Spring concepts demonstrated

### Core Java
- classes
- objects
- encapsulation
- inheritance
- abstraction
- interfaces
- polymorphism
- enums
- records
- BigDecimal for money
- LocalDate / LocalDateTime
- UUID
- streams
- Optional
- collections
- exceptions

### Spring Boot
- `@SpringBootApplication`
- `@RestController`
- `@RequestMapping`
- `@GetMapping`
- `@PostMapping`
- `@RequestBody`
- `@PathVariable`
- `@Service`
- `@Repository` (Spring Data interface)
- `@Component`
- `@Configuration`
- `@Bean`
- dependency injection
- validation
- global exception handling
- transactions

### JPA/Hibernate
- `@Entity`
- `@Table`
- `@Id`
- `@GeneratedValue`
- `@Column`
- `@ManyToOne`
- `@JoinColumn`
- `@Enumerated`
- `@MappedSuperclass`
- `@PrePersist`
- `@PreUpdate`
- indexes
- pessimistic locking
- optimistic versioning through reusable entity design
- repository derived queries

---

## 9. Call flow: Create parent

Postman
  |
  | POST /api/accounts/parents
  v
CreditController.createParent()
  |
  v
CreditService.createParent()
  |
  v
CreditServiceImpl.createParent()
  |
  v
UserAccountRepository
  |
  v
H2 database

Controller receives HTTP.
Service applies business rules.
Repository performs persistence.

---

## 10. Call flow: Create child

Postman
  |
  v
CreditController.createChild()
  |
  v
CreditServiceImpl.createChild()
  |
  +--> lock parent row
  |
  +--> calculate remaining allocation
  |
  +--> validate child allocation
  |
  +--> create CHILD account
  |
  +--> update parent's allocatedToChildren
  |
  v
Database

This prevents the parent from allocating more credit than it owns.

---

## 11. Call flow: Book policy

Postman
  |
  v
POST /api/policies
  |
  v
CreditController
  |
  v
CreditServiceImpl.bookPolicy()
  |
  +--> lock agent
  +--> verify CHILD role
  +--> verify active
  +--> load customer
  +--> validate policy dates
  +--> calculate available credit
  +--> check quotation/ACD uniqueness
  +--> create policy
  +--> increase agent usedLimit
  |
  v
Database

Important:
`usedLimit` increases only after all validation succeeds.

---

## 12. Call flow: Payment

Postman
  |
  v
POST /api/payments
  |
  v
CreditServiceImpl.payPolicy()
  |
  +--> find policy
  +--> check idempotency key
  +--> calculate outstanding
  +--> create PENDING transaction
  |
  +--> PaymentGateway interface
  |       |
  |       +--> DummyPaymentGateway
  |
  +--> SUCCESS?
          |
          +-- NO --> FAILED transaction
          |
          +-- YES
                |
                +--> policy.paidAmount += due
                +--> policy status = PAID
                +--> agent.usedLimit -= due
                +--> transaction = SUCCESS
                +--> gateway reference
                +--> payment date
                +--> warehouse publisher
                |
                v
              Database

---

## 13. Why `@Transactional` is important

Booking has multiple changes:
- policy insert
- agent used credit update

Payment has multiple changes:
- transaction insert/update
- policy update
- agent credit update

If one operation fails, the transaction boundary protects database consistency.

---

## 14. Why locking is important

Imagine two requests arrive at almost the same time:

Amit available credit = ₹5,000

Request A books ₹5,000.
Request B books ₹5,000.

Without proper concurrency handling both requests could read ₹5,000 before either writes the update.

This project uses:

`@Lock(LockModeType.PESSIMISTIC_WRITE)`

for the agent/parent row during important credit operations.

---

## 15. Why `BigDecimal` is used

Never use `double` for financial amounts.

Bad:
`double amount = 5000.10;`

Better:
`BigDecimal amount = new BigDecimal("5000.10");`

Money requires decimal precision.

---

## 16. Why idempotency is used

Suppose customer clicks Pay twice or the same network request is retried.

Same:
`idempotencyKey = PAY-RAHUL-001`

The service returns the existing transaction instead of creating a second payment transaction.

This is a common distributed-system/payment concept.

---

## 17. Dummy gateway behavior

Normal quotation:
`QUO-1001`

=> SUCCESS

Quotation containing:
`FAIL`

=> FAILED

Example:
`QUO-FAIL-001`

No real money is transferred.

---

## 18. Data warehouse status

There is NO real data warehouse.

The project only has:

`DataWarehousePublisher` interface

and:

`DummyDataWarehousePublisher`

It logs the event.

Later it can be replaced with a real integration.

---

## 19. Build and run

Requirements:
- JDK 17+
- Maven 3.9+

Run:

```bash
mvn clean test
mvn spring-boot:run
```

Application:

`http://localhost:8080`

H2 console:

`http://localhost:8080/h2-console`

JDBC URL:

`jdbc:h2:mem:creditdb`

Username:
`sa`

Password:
empty

---

## 20. Demo IDs

The startup initializer creates:

Parent:
`xyz-parent`

Amit:
`amit`

Ravi:
`ravi`

Suresh:
`suresh`

Rahul:
`CUST-1001`

Priya:
`CUST-1002`

Database-generated numeric IDs are normally:
Parent = 1
Amit = 2
Ravi = 3
Suresh = 4
Rahul = 1
Priya = 2

Check your actual response IDs rather than blindly assuming them.

---

## 21. Important interview explanation

Use this explanation:

> "This is a backend credit-limit management system for a partner/agent business model. A parent business account receives a master credit limit and can allocate portions of that limit to child agent accounts. When an agent books a policy, the policy premium consumes the agent's available credit. When the corresponding outstanding amount is successfully paid, the system releases the utilized credit. The backend maintains customer, policy, payment transaction and credit information and exposes REST APIs for partner-side integration. Payment gateway and data warehouse integrations are abstracted behind interfaces and are represented by dummy implementations in the current project."

Do not claim:
- real Razorpay integration
- real insurance customer portal
- real data warehouse
- real bank settlement
- production authentication/security

Those are intentionally outside this dummy version.
