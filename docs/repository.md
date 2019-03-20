# Repository Service (temporary) (backend only)
## Allowed HTTPs requests:
__GET:__ Get a resource or list of resources
## Description Of Usual Server Responses:
* 200 `OK` - the authentication was successful.
* 404 `Not Found` - resource was not found
## User:
### Get a User by username:
`GET` /user/__username__
#### Parameters:
_username:_ exactly what you think you're supposed to put here
#### Request:
Use a RestTemplate.
#### Response:
The RestTemplate will give you an instance of the resource's model class.
