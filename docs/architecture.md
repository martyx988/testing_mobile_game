# Architecture

## Overview
The app is a single Android application module built with Kotlin and Jetpack Compose. V1 remains fully local-only: no backend, no remote sync, and no account system.

## Boundaries
- `ui`: Compose screens, theming, and interaction wiring
- `domain`: game rules, board generation, and deterministic state transitions
- `data`: local persistence for resume state, settings, stats, and achievements

## Current State
- `domain` contains deterministic board generation, classic reveal/flag/win/loss rules, snapshot encoding, player profile management, and the Trap Tiles variant
- `data` persists active-session snapshots plus player stats, achievements, theme choice, and haptics settings in SharedPreferences
- `ui` exposes a portrait-first Compose experience with mode switching, resume prompts, a lightweight tutorial dialog, and accessibility-oriented copy/legends

## Data Flow
- `ResumeGameController` coordinates the active match, selected mode, help-dialog state, resume prompts, and persisted player profile
- `ClassicGameEngine` is the single source of truth for board state transitions and snapshot restoration
- SharedPreferences stores are injected into the controller at app start so both gameplay state and player profile survive relaunches

## Validation
- Unit tests cover deterministic board generation, gameplay rules, snapshot encoding, controller logic, profile updates, and Trap Tiles behavior
- Instrumentation tests verify the app launches and the tutorial entry flow works on a running emulator
