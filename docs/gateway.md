# API Gateway
The entry point into the system. In other words, the guy the frontend people will throw their HTTP requests at. (｡•̀ᴗ-)✧

## Regarding Security
- Non authenticated requests to a service will get a `401 - Unauthorized` response
- Authenticated requests to a service resource meant to a role other than the current user's will get a `403 - Forbidden` response