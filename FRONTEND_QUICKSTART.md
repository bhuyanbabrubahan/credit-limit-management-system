# Quick start
1. Import this folder as a Maven project (Java 17+).
2. Configure MySQL and environment variables as described in README.md/application.properties.
3. Run `CreditLimitDemoApplication` on port 8080.
4. In another terminal: `cd frontend`, `npm install`, `npm start`.
5. Open http://localhost:4200.

Known limitation: backend lacks global account/policy/transaction listing APIs. The UI calls the existing customer list and demo list endpoints for agent ID 1. Use create response IDs in forms. Payment is simulated only. See API_GAPS.md for recommended pagination/audit endpoints.
