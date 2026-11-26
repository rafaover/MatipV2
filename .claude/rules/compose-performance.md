---
description: Compose UI and performance guidelines
paths:
  - "**/*.kt"
---

# Compose guidelines

- Prefer stateless composables.
- Apply state hoisting.
- UI state must be immutable.
- Use `collectAsStateWithLifecycle` for Flow.

# Performance

- Use `LazyColumn` for large lists.
- Avoid unnecessary recompositions.
- Avoid blocking the main thread.
- Use `Dispatchers.IO` for data operations.
