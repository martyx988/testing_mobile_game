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
- Trap Tiles is now a playable experimental mode with its own deterministic board setup
- Lightweight in-app tutorial/help content is now available from the first screen
- Core accessibility polish is in place: scrolling for larger text, non-color board legends, stronger Classic theme contrast, and verified help flow on-device
- Build tooling has been aligned to AGP `9.0.0` and Gradle `9.1.0` for broader Android Studio compatibility
- Post-v1 UI polish fixes landed: the board now fits within screen width, the `Actions` panel sits directly below the board, hidden tiles contrast more clearly against revealed empty tiles, and the end-of-round hazard count preserves the last active value

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
- Milestone 5 complete: Trap Tiles experimental mode
- Milestone 6 complete: tutorial, accessibility pass, QA expansion, and release polish
- Initiative status: all planned v1 tasks are complete

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
- Trap Tiles engine and mode-switch tests pass
- Emulator-backed Compose UI tests pass, including the tutorial entry flow
- Final validation pass completed with `test`, `lint`, `assembleDebug`, `connectedDebugAndroidTest`, and `installDebug`
- Post-release compatibility validation passed after downgrading the build toolchain to AGP `9.0.0`
- Follow-up UI/layout regression checks passed after the board/order/hazard-count polish update

## Follow-Ups
- Add harder classic difficulties after v1
- Consider a dedicated first-launch onboarding path if a future version expands beyond the lightweight help dialog
