# Authentication Service
## Allowed HTTPs requests:
__POST:__ Request Bearer token
## Description Of Usual Server Responses:
* 200 `OK` - the authentication was successful.
* 401 `Unauthorized` - the given username does not exist; or the given password is incorrect.
## Request template:
`POST` /auth
### Request:
#### HEADERS:
__Content-Type:__ application/json
#### BODY:
```javascript
{
  "username": string,
  "password": string
}
```
### Response:
#### HEADERS:
__Authorization:__ Bearer token
