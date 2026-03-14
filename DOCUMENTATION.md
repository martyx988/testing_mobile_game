# DOCUMENTATION.md

## Status
- Repository planning is being repurposed toward an Android Minesweeper app
- Android project scaffold has been added
- Build, unit-test, and lint validation have been completed for the scaffold task
- Deterministic board generation for classic mode has been implemented
- Classic gameplay reveal, flag, win, loss, and restart rules have been implemented
- A playable Compose game screen is now wired to the classic engine
- Active matches can now be saved locally and resumed from an app-launch prompt
- Local stats, achievements, theme choice, and haptics settings now persist across launches

## Approved Decisions
- Native Android app using Kotlin and Jetpack Compose
- Portrait-first, retro desktop-inspired, mobile-optimized UI
- V1 includes classic `Easy` mode and one `Trap Tiles` experimental mode
- Data remains local-only with no monetization

## Current Milestone
- Milestone 1 complete: Android project scaffold and build baseline
- Milestone 2 complete: classic game engine and state rules
- Milestone 3 complete: Compose game screen and interaction flow
- Milestone 4 complete: persistence, resume flow, settings, and themes
- Next active task: Trap Tiles experimental mode

## Validation Status
- Unit test harness added
- `test` passed
- `lint` passed with non-blocking dependency freshness warnings
- `assembleDebug` passed and produced a debug APK
- Deterministic board-generation unit tests pass
- Classic gameplay engine unit tests pass
- UI controller tests pass
- Playable UI build, lint, and debug APK assembly pass
- Snapshot codec and resume-controller tests pass
- Player profile, achievement, theme, and settings tests pass

## Follow-Ups
- Add harder classic difficulties after v1
- Finalize the exact `Trap Tiles` rule contract before coding begins
- Add the fixed experimental Trap Tiles mode next
