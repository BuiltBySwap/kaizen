# Kaizen

> **改善 (kaizen) — continuous improvement.** An AI learning companion that serves my 7-month engineering roadmap: today's plan, my notes, a daily question, and (later) an assistant that answers from my own notes.

I use it every day, so it has to work — that's the point of building it.

## Vision

One product, three clients/services, built in the open:

| Part | Folder | Tech |
|---|---|---|
| Android app | `android/` | Kotlin · Jetpack Compose · MVI · Hilt · Room · WorkManager |
| Backend | `server-ktor/` | Ktor · PostgreSQL · Docker |
| React Native companion | `rn-companion/` | Expo · TypeScript |
| Decisions and docs | `docs/` | ADRs · RFCs · backend notes |

Later: AI features (semantic search over my notes, "Ask my notes" with streaming), an Android widget, and benchmarks with measured numbers.

## Architecture (Android)

```
presentation  →  domain  ←  data
(Compose + MVI)   (use cases, models,        (implementations: assets now,
                   repository interfaces)     Room + API later)
```

- **Dependency rule:** both outer layers depend on `domain`; `domain` knows nothing about Android.
- **MVI:** one immutable `State` per screen, user actions as `Intent`s.
- **Dependency injection:** Hilt.
- **Testability:** time comes from an injected `Clock`, so "today" is testable.

Decisions are recorded in [`docs/adr`](docs/adr).

## Roadmap (Sunday builds)

| Week of | ID | What ships |
|---|---|---|
| 04 Oct | PRJ-01 | Kaizen kickoff: monorepo, ADR-001, Android skeleton showing today's plan |
| 11 Oct | PRJ-02 | Kaizen server v0: Ktor endpoints for plan/today and plan/{date} |
| 18 Oct | PRJ-03 | Kaizen: Postgres + Exposed + Flyway + docker-compose |
| 01 Nov | PRJ-04 | Kaizen Android ↔ API: Retrofit + Room offline-first cache + sync status |
| 08 Nov | PRJ-05 | Kaizen auth: JWT login/refresh (Ktor) + Encrypted DataStore (Android) |
| 15 Nov | PRJ-06 | Kaizen notes: write 'origin story' notes per day + markdown + sync |
| 22 Nov | PRJ-07 | Kaizen daily question: scheduled FCM push + WorkManager fallback |
| 06 Dec | PRJ-08 | Kaizen live progress: WebSocket updates across devices |
| 13 Dec | PRJ-09 | Portfolio library: compose-jank-radar v0 (debug overlay for recompositions + jank) |
| 20 Dec | PRJ-10 | compose-jank-radar: tests + publish 0.1.0 to Maven Central |
| 03 Jan | PRJ-11 | Kaizen CI + tests + deploy live (Docker → Cloud Run/Render) |
| 10 Jan | PRJ-12 | Kaizen RN companion v0: Expo app with today's plan + mark done |
| 17 Jan | PRJ-13 | Kaizen AI 1: embeddings + pgvector semantic search over notes & questions |
| 24 Jan | PRJ-14 | Kaizen AI 2: 'Ask my notes' RAG endpoint with SSE streaming |
| 07 Feb | PRJ-15 | Kaizen Android AI chat UI: streaming tokens, cancel, markdown |
| 14 Feb | PRJ-16 | Portfolio: mcp-android-devtools v0 (MCP server for Android debugging) |
| 21 Feb | PRJ-17 | Kaizen RN: TurboModule 'battery-insights' in rn-companion/modules (+ optional npm publish) |
| 07 Mar | PRJ-18 | Kaizen on-device AI: note summaries with Gemini Nano / ML Kit + cloud fallback |
| 14 Mar | PRJ-19 | Kaizen Glance widget + Baseline Profile + Macrobenchmark results |
| 21 Mar | PRJ-20 | Kaizen modularization + convention plugins; Phase 2 portfolio polish |
| 04 Apr | PRJ-21 | mcp-android-devtools v1 + Kaizen MCP tools (today_plan, search_notes, mark_done) |
| 11 Apr | PRJ-22 | Security + release pass: OWASP MASVS audit, API Top 10, Play internal track |
| 18 Apr | PRJ-23 | Portfolio showcase: case studies, diagrams, demo videos, profile README |

## Status

🚧 Started October 2026 — the Android skeleton shows today's row from a bundled copy of the plan. The server arrives next.

## Run it

Open `android/` in Android Studio and run the `app` configuration. Server instructions arrive with PRJ-02.
