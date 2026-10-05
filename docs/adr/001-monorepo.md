# ADR-001: One monorepo for the Android app, the server and the React Native app

- **Status:** Accepted
- **Date:** 2026-10-04

## Context
Kaizen has three deliverables — an Android app, a Ktor backend and a React Native companion. One person builds them, and they share a lot: the plan data, the API contract, and the documentation. A change to the API usually touches the server and at least one client at the same time.

## Decision
Keep everything in a single repository, `kaizen`, with one top-level folder per deliverable:

```
android/        Android app (Gradle)
server-ktor/    Ktor backend (Gradle)
rn-companion/   React Native app (npm)
docs/           ADRs, RFCs, backend notes
deploy/         (later) Docker / Kubernetes manifests
```

Each folder builds on its own and gets its own short README.

## Consequences
**Good:**
- An API change and the client changes that depend on it land in one commit and one pull request.
- One README, one issue list and one place to look — good for a portfolio.
- Shared documents (ADRs, RFCs) live next to the code they describe.

**Bad / costs:**
- CI must use path filters so an Android change doesn't rebuild the server (and the reverse).
- Two toolchains (Gradle and Node) in one repository.
- The repository grows; access can't be granted per component.

**Mitigation:** path-filtered GitHub Actions workflows, and self-contained folders.

## Alternatives considered
- **One repo per component (polyrepo):** rejected for now. A cross-cutting change needs several pull requests, which is pure overhead for a solo project.
- **Separate "contract" repo shared by the others:** rejected. It adds a fourth repository with no benefit at this size.

## Revisit when
Several people work on separate components, or build times become painful. Then split along the existing folder boundaries.
