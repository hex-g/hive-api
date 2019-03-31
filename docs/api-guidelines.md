# Guidelines for API development
- Resources available to specific roles should be at `/ROLE_NAME/**` and those available to all users at `/**`
- If needed, authenticated user's name is available in the Request Header `HiveHeaders.AUTHENTICATED_USER_NAME_HEADER` (It can be retrieved in a controller method with the `@RequestHeader(name)` annotation)
- Last but not least, give it a nice name! __(^・ω・^ )__