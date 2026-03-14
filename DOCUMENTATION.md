# DOCUMENTATION.md

## Status
- Repository planning is being repurposed toward an Android Minesweeper app
- Android project scaffold has been added
- Build, unit-test, and lint validation have been completed for the scaffold task
- Deterministic board generation for classic mode has been implemented
- Classic gameplay reveal, flag, win, loss, and restart rules have been implemented

## Approved Decisions
- Native Android app using Kotlin and Jetpack Compose
- Portrait-first, retro desktop-inspired, mobile-optimized UI
- V1 includes classic `Easy` mode and one `Trap Tiles` experimental mode
- Data remains local-only with no monetization

## Current Milestone
- Milestone 1 complete: Android project scaffold and build baseline
- Milestone 2 complete: classic game engine and state rules
- Next active task: Compose game screen and interaction flow

## Validation Status
- Unit test harness added
- `test` passed
- `lint` passed with non-blocking dependency freshness warnings
- `assembleDebug` passed and produced a debug APK
- Deterministic board-generation unit tests pass
- Classic gameplay engine unit tests pass

## Follow-Ups
- Add harder classic difficulties after v1
- Finalize the exact `Trap Tiles` rule contract before coding begins
- Wire the game engine into the Compose UI next
