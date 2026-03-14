# Task Contract
Implement classic gameplay state transitions, reveal behavior, flagging, restart flow, and win/loss rules for `Easy` mode.

## Relevant PLAN.md Milestone
Milestone 2: Classic `Easy` game engine with tests

## Local Implementation Plan
- Add reveal and flood-fill behavior for safe tiles
- Add flag toggling and rule enforcement
- Add match status transitions for active, won, and lost states

## Architecture Notes
- Keep state transitions explicit and side-effect-free where possible
- Make restart and game-over behavior reusable by future modes

## Test Plan
- Reveal behavior on numbered and empty tiles
- Flood-fill correctness
- Flagging rules and blocked interactions after end-state
- Win/loss detection and restart reset behavior

## Progress Log
- Added a pure classic game engine on top of the deterministic board model
- Implemented reveal handling for numbered cells and zero-cell flood fill
- Implemented flag toggling, mine-loss handling, win detection, and deterministic restart
- Kept the game-state API platform-agnostic for future persistence and UI wiring

## Validation Results
- `./gradlew test -Pandroid.builder.sdkDownload=false` passed
- Gameplay tests cover reveal behavior, flood fill, flagging, end-state lockout, win/loss, and restart

## Final Summary
- Classic gameplay state transitions are complete and ready for UI integration
