# Project build order / learning order

Read and understand in this order:

1. CreditLimitDemoApplication
2. entity/BaseEntity
3. entity/UserAccount
4. entity/Customer
5. entity/Policy
6. entity/PaymentTransaction
7. enum classes
8. repository interfaces
9. DTO records
10. gateway/PaymentGateway
11. gateway/DummyPaymentGateway
12. warehouse/DataWarehousePublisher
13. mapper/DtoMapper
14. service interfaces
15. service implementations
16. controllers
17. exception handling
18. DataInitializer
19. Postman flow

Dependency direction:

Controller
   ↓
Service interface
   ↓
Service implementation
   ↓
Repository / external adapter
   ↓
Database / external system

The controller should not contain business calculations.

The repository should not contain business rules.

The gateway adapter should not decide credit rules.

The service coordinates the business transaction.
