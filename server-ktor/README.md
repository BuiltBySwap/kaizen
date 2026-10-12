# kaizen-server

This project was created using the [Ktor Project Generator](https://start.ktor.io).

Here are some useful links to get you started:
 * [Ktor Documentation](https://ktor.io/docs/home.html)
 * [Ktor GitHub page](https://github.com/ktorio/ktor)
 * [Ktor Slack chat](https://app.slack.com/client/T09229ZC6/C0A974TJ9). [Request an invite](https://surveys.jetbrains.com/s3/kotlin-slack-sign-up).


## Features
Here's a list of features included in this project:

| Name | Description |
|------|-------------|
| [Call Logging](https://start.ktor.io/p/io.ktor/server-call-logging) | Logs client requests |
| [Content Negotiation](https://start.ktor.io/p/io.ktor/server-content-negotiation) | Provides automatic content conversion according to Content-Type and Accept headers |
| [kotlinx.serialization](https://start.ktor.io/p/io.ktor/server-kotlinx-serialization) | Handles JSON serialization using kotlinx.serialization library |


## Building & Running
To build or run the project, use one of the following tasks:


| Task | Description |
|------|-------------|
| `./gradlew test`    | Run the tests     |
| `./gradlew build`   | Build the project |
| `./gradlew run`     | Run the server    |

If the server starts successfully, you'll see the following output:
```
2024-12-04 14:32:45.584 [main] INFO  Application - Application started in 0.303 seconds.
2024-12-04 14:32:45.682 [main] INFO  Application - Responding at http://0.0.0.0:8080
```


Ktor server that serves the Kaizen learning plan (217 days) from a CSV seed.

## Run
./gradlew run          # http://localhost:8080 (Android emulator: http://10.0.2.2:8080)

## Endpoints
| Request | Success | Errors |
|---|---|---|
| GET /health | 200 {"status":"ok"} | |
| GET /plan/today | 200 the plan day for today (IST) | 404 if today is not in the plan |
| GET /plan/{date} (YYYY-MM-DD) | 200 the plan day | 400 bad date format, 404 no plan that day |
| GET /questions/{n} | 200 the Mobile Exploration question | 400 not a number, 404 no such question |

Every error has the same body: {"error": "message"}.

## Data
src/main/resources/seed/plan.csv is read once at startup (217 rows). Restart the server after changing it.
Postgres replaces it in PRJ-03.

## Decisions
- "Today" is computed in Asia/Kolkata, so a server running in UTC gives the right date.
- 400 means the request is malformed; 404 means a valid request with nothing behind it.