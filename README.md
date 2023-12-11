# Spring Boot Application for the Games API

## Local setup

### Requirements
- Docker
## Running the application
`docker compose up --build`

With the terminal output looking like the below snippet, the application is running.
```agsl
elevate-api-app-1       | 2023-12-11T19:04:17.549Z  INFO 1 --- [           main] o.s.b.w.embedded.tomcat.TomcatWebServer  : Tomcat started on port 3000 (http) with context path ''
elevate-api-app-1       | 2023-12-11T19:04:17.575Z  INFO 1 --- [           main] com.elevate.api.ElevateApiApplication    : Started ElevateApiApplication in 13.009 seconds (process running for 14.073)
elevate-api-app-1       | 2023-12-11T19:04:19.422Z  INFO 1 --- [nio-3000-exec-1] o.a.c.c.C.[Tomcat].[localhost].[/]       : Initializing Spring DispatcherServlet 'dispatcherServlet'
elevate-api-app-1       | 2023-12-11T19:04:19.423Z  INFO 1 --- [nio-3000-exec-1] o.s.web.servlet.DispatcherServlet        : Initializing Servlet 'dispatcherServlet'
elevate-api-app-1       | 2023-12-11T19:04:19.427Z  INFO 1 --- [nio-3000-exec-1] o.s.web.servlet.DispatcherServlet        : Completed initialization in 2 ms
```
You can verify by hitting the heartbeat endpoint at `http://localhost:3000/heartbeat`
## Running the application
`docker compose up --build`