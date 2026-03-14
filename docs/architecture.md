# Architecture

## Overview
The app is a single Android application module built with Kotlin and Jetpack Compose. V1 remains fully local-only: no backend, no remote sync, and no account system.

## Planned Boundaries
- `ui`: Compose screens, theming, and interaction wiring
- `domain`: game rules, board generation, and deterministic state transitions
- `data`: local persistence for resume state, settings, stats, and achievements

## Current State
- Project scaffold and build configuration are in place
- Compose app shell exists as a placeholder surface
- Game engine and persistence logic are still pending

