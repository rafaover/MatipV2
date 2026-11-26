---
description: Stack, architecture, and forbidden patterns for this codebase
---

# Stack

- Kotlin only — no Java files.
- Jetpack Compose only — no XML layouts.
- MVVM architecture.
- Coroutines and Flow only — no other async/reactive mechanisms.
- Kotlin Multiplatform, Android first (iOS comes later). Two Gradle modules: `:shared` (all app code, `commonMain` and `androidMain`) and `:androidApp` (thin shell: activity, application, manifest, signing).
- Platform code goes behind an interface injected with Koin (`PlatformActions`, `GoogleIdTokenProvider`). Use expect/actual only for small functions or constants, like currency formatting.
- Dependency injection with Koin.

# Architecture rules

- UI layer contains composables only.
- No business logic in composables.
- ViewModels handle state and logic.
- Repositories manage data sources (Room local database, Firebase remote).
- Business rules that affect trust are enforced server-side (Firebase security rules); the client only reflects them.
- Domain layer contains models and use cases. Add a use case only when the logic is shared or non-trivial; otherwise the ViewModel calls the repository directly.
- Strict separation between layers.

# Material Design

- Favor Material 3 Expressive over older/standard Material 3 patterns.
- Use its spring-based motion schemes (`MotionScheme.expressive()`) for physics, easing, and duration instead of default/linear animation specs.
- Use its transition APIs (`AnimatedContent`, `AnimatedVisibility`, shape-morphing components) instead of static, non-animated states.
- Be creative with shapes — favor `MaterialShapes`' organic polygons and expressive component shapes (e.g. FABs, toggle buttons) over plain rectangles/circles where it fits the design.

# Accessibility

- When building a composable, think about accessibility.
- The composable needs to be readable by TalkBack.
- Switch Input must also be available.
- Proper target spacing.
- Reduce page complexity.
- Visual elements need labels, except interactive elements with sufficient text.

# Forbidden

- No LiveData.
- No direct database access in ViewModel.
- No XML layouts.
- No Java files.