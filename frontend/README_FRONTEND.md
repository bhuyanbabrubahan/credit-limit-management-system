# CrediFlow Angular frontend

## Folder structure

```text
frontend/
├── src/
│   ├── app/
│   │   ├── components/
│   │   │   ├── dashboard/   dashboard.component.ts / .html / .scss
│   │   │   ├── accounts/    accounts.component.ts / .html / .scss
│   │   │   ├── customers/   customers.component.ts / .html / .scss
│   │   │   ├── policies/    policies.component.ts / .html / .scss
│   │   │   └── payments/    payments.component.ts / .html / .scss
│   │   ├── models/api.models.ts
│   │   ├── services/credit-api.service.ts
│   │   ├── shared/format-currency.pipe.ts
│   │   ├── app.component.ts / .html / .scss
│   ├── index.html
│   ├── main.ts
│   └── styles.css
├── angular.json
├── package.json
└── proxy.conf.json
```

## Run it

1. Start the Spring Boot backend on `http://localhost:8080`.
2. In a terminal, open the `frontend` folder.
3. Run `npm install`.
4. Run `npm start`.
5. Open `http://localhost:4200`.

`npm run build` checks/compiles the Angular frontend.

## API notes

The frontend calls only endpoints that exist in this project's Spring Boot controllers:
- `POST /api/accounts/parents`
- `POST /api/accounts/children`
- `GET /api/accounts/{id}/credit`
- `GET /api/accounts/{parentId}/children`
- `POST /api/customers`, `GET /api/customers`
- `POST /api/policies`, `GET /api/policies/agent/{agentId}`, `GET /api/policies/customer/{customerId}`
- `POST /api/payments`
- `GET /api/transactions/agent/{agentId}`, `GET /api/transactions/customer/{customerId}`

The current backend does not expose a global account listing endpoint. Account rows appear when created or when a credit summary is fetched. Policies and transactions require an agent/customer ID query. Payment is a dummy gateway only; do not use real payment credentials.
