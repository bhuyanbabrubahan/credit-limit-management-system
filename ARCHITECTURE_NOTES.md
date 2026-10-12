# Architecture notes

## Entity vs DTO

Entities are database/domain objects.

DTOs are API contracts.

The controllers return DTOs instead of exposing JPA entities directly. This avoids:
- accidental lazy-loading problems
- recursive JSON relationships
- exposing internal database fields
- coupling external API clients to database structure

## Mapper

`DtoMapper` converts:

Entity -> Response DTO

The mapper is a reusable component and keeps conversion logic out of controllers/services.

## Dependency injection

The service receives interfaces/components through its constructor:

- repositories
- `PaymentGateway`
- `DataWarehousePublisher`

Spring resolves the implementations.

This is constructor injection and is preferable to field injection for testability and explicit dependencies.

## Transaction boundaries

`@Transactional` belongs on service methods because the service represents a business operation.

Example payment operation:

1. create pending transaction
2. call dummy gateway
3. if success, update policy
4. release agent credit
5. mark transaction successful
6. publish warehouse event

These changes are part of one application transaction.

## External integration boundary

`PaymentGateway` is deliberately an interface.

The business service does not know whether the provider is:
- dummy
- Razorpay
- Stripe
- insurer's own API

The same pattern is used for the future warehouse.

## Concurrency

The user/agent row is pessimistically locked before credit-changing operations.

`BaseEntity` also contains `@Version`, demonstrating optimistic locking at the entity level.

In a production system, the exact concurrency strategy should be chosen based on database load and integration architecture.
