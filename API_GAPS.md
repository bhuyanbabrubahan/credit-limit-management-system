# Backend gaps for a fully populated UI

The provided backend exposes account creation, credit lookup by account ID, children by parent ID, customer list, policies by agent/customer ID, and transactions by agent/customer ID. It does not expose global paginated account/policy/transaction lists or an audit-log read endpoint.

Recommended additions: `GET /api/accounts?page=0&size=20`, `GET /api/policies?page=0&size=20`, `GET /api/transactions?page=0&size=20`, and `GET /api/audit-logs?page=0&size=20`. Use Spring Data `Pageable`, add indexes based on real query plans, and use DTO projections/entity graphs to prevent N+1 queries. Audit persistence, payment idempotency and concurrency must be implemented and tested in the backend; Angular cannot add these database guarantees. This bundle preserves the provided backend and does not claim these features are verified.
