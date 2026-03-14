# SPEC.md

## Goal
Build a native Android Minesweeper app for portrait play using Kotlin and Jetpack Compose.

## Scope
- Classic `Easy` mode for beginner-friendly play
- One fixed experimental `Trap Tiles` mode
- Light tutorial/help experience
- Prompt to resume the last unfinished game
- Local stats and achievements
- A small set of visual themes
- Strong accessibility basics

## Non-Goals
- Multiplayer or social play
- Cloud sync or accounts
- Monetization, ads, or in-app purchases
- Custom board creation in v1
- Harder classic difficulties in v1

## Constraints
- Native Android only
- Kotlin + Jetpack Compose
- Offline and local-only data storage
- Short-session, mobile-first UX
- Retro desktop styling adapted for touch devices

## Acceptance Criteria
- Players can start and finish a classic `Easy` game
- Players can start and finish the `Trap Tiles` experimental mode
- Core game behavior is deterministic and testable
- Win/loss and restart flows are explicit and reliable
- Resume, stats, achievements, themes, and settings persist locally
- Core UI states remain readable and accessible
