# Spring Boot Application for the Games API

## Requirements to setup and run the application locally
- Git clone the repo
- Docker, have docker up and running before attempting to run the application/tests.
### Running the application
For simplicity, I have included all the configs/variables to get the application running, including database connection 
details, in the `docker-compose.yml` file.

`docker compose up --build app`

With the terminal output looking like the below snippet, the application is running.
```agsl
elevate-api-app-1       | 2023-12-11T19:04:17.549Z  INFO 1 --- [           main] o.s.b.w.embedded.tomcat.TomcatWebServer  : Tomcat started on port 3000 (http) with context path
elevate-api-app-1       | 2023-12-11T19:04:17.575Z  INFO 1 --- [           main] com.elevate.api.ElevateApiApplication    : Started ElevateApiApplication in 13.009 seconds (process running for 14.073)
elevate-api-app-1       | 2023-12-11T19:04:19.422Z  INFO 1 --- [nio-3000-exec-1] o.a.c.c.C.[Tomcat].[localhost].[/]       : Initializing Spring DispatcherServlet 'dispatcherServlet'
elevate-api-app-1       | 2023-12-11T19:04:19.423Z  INFO 1 --- [nio-3000-exec-1] o.s.web.servlet.DispatcherServlet        : Initializing Servlet 'dispatcherServlet'
elevate-api-app-1       | 2023-12-11T19:04:19.427Z  INFO 1 --- [nio-3000-exec-1] o.s.web.servlet.DispatcherServlet        : Completed initialization in 2 ms
```
You can verify by hitting the heartbeat endpoint at `http://localhost:3000/heartbeat`

A [WIP] API documentation can be viewed through the browser at `http://127.0.0.1:3000/swagger-ui/index.html`

#### POST Create User
`/api/user`
```agsl
{
    "username": "username",
    "email": "url@email.com",
    "full_name": "User Name
    "password": "password"
}
```
Status codes:
1. 201 - User created // successfully created user
2. 400 - Bad Request // invalid request body 
3. 409 - Conflict // user already exists

#### POST Login
`/api/sessions`
```agsl
{
    "username": "username",
    "password": "password"
}
```
Status codes:
1. 200 - OK // successfully logged in, Returns a token

All the subsequent requests require an Authorization header with a valid token returned from the login request. i.e
```agsl
Header: Authorization Bearer <token>
```

#### GET Get games

`/api/games`

Returns a list of games for the logged in user

Status codes:
1. 200 - OK // successfully logged in, Returns a list of games
2. 401 - Unauthorized // invalid token

#### POST Create game

This is an additional endpoint to populate the db with games that will be available later for the user to save a collection of events.
This request has a bug where it will return a 401 for requests that have categories that are not in the enum i.e (`MATH`, `READING`, `WRITING`, `SPEAKING`)

Ideally the error should be descriptive as to why the request failed, but I have left it as is for now.
`/api/games`
```agsl
{
    "name": "Example Game Name",
    "url": "https://www.examplegameurl.com",
    "category": "MATH"
}
```

#### POST Save Event for a game

`/api/user/game_events`
```agsl
{
    "game_event": {
        "type": "COMPLETED",
        "occurred_at": "2023-03-15T12:00:00",
        "game_id": 1
    }
}

```

#### GET Get User Details

`/api/user`

Returns the user details

```agsl
{
    "id": 1,
    "username": "user@username.com",
    "email": "user@username.com",
    "full_name": "User",
    "stats": {
        "total_games_played": 2,
        "total_math_games_played": 1,
        "total_reading_games_played": 1,
        "total_speaking_games_played": 0,
        "total_writing_games_played": 0
    }
}
```

## Running tests
`docker compose up --build app-test`

This outputs test results in the console, and also generates a test report. Given this is running in the container,
I have left out the part to extract the coverage report at this point. But normally you would get this from the `build/jacocoHtml` folder.
```agsl
elevate-api-app-test-1  |
elevate-api-app-test-1  | BUILD SUCCESSFUL in 1m 22s
elevate-api-app-test-1  | 6 actionable tasks: 4 executed, 2 up-to-date
```
