---
description: Clean code and naming conventions
---

# Clean code

- Keep classes as separated as possible.
- Follow single responsibility principle.
- Minimize complexity. Do not overengineer: this is a hobby project built by one developer.
- One source of truth.
- Self-documenting code.
- Don't use magic numbers.
- Write clean, idiomatic, and readable code over clever or compact code.
- Avoid premature optimizations, unnecessary design patterns, or deeply nested abstractions.
- Include concise, meaningful comments only where the code logic isn't self-explanatory.
- If a solution feels over-complicated, stop and propose the simplest working alternative first.
- Write pure, single-purpose functions. Try to avoid multiple factories, interfaces and classes.
- Use guard clauses and early returns. Make the control flow linear and highly readable.
- Handle only the immediate, critical failure point. Avoid massive try/catch blocks and custom error classes.
- Strings/dimensions/shapes/ should be added in resources.

# Naming

- Be descriptive, be verbose.
- Screens → `...Screen`
- UI state → `...UiState`
- Events → `...UiEvent`
- Repositories → `...Repository`

# TDD
- Before writing the final code. First build the test, make it fail. After that, build to pass the test. Focus in building these tests for only code that could break the app, not every single function.
- Every Room migration needs its own test (`MigrationTestHelper`): create the database at the old schema version, run the migration, assert the new column/shape and that existing data survived. Exported schemas live in `app/schemas/`; commit them with the migration.

# DOCUMENTATION
- If necessary for future developers or myself. Add KDoc for non-obvious public APIs.

# GIT
- When writing titles, comments and explanations. Keep as simple as possible, using bullets points and short sentences.