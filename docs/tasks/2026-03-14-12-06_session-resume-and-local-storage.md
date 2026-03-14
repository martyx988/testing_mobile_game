# Task Contract
Add local persistence for unfinished matches and the prompt-to-resume flow on app launch.

## Relevant PLAN.md Milestone
Milestone 4: Local persistence for resume, stats, achievements, settings, and themes

## Local Implementation Plan
- Define the persisted snapshot shape for unfinished games
- Save and restore local match state safely
- Add launch-time resume prompt flow

## Architecture Notes
- Persistence failures must be explicit and recoverable
- Resume loading should not corrupt or silently alter game state

## Test Plan
- Snapshot save and restore round-trip
- Invalid or partial saved state handling
- Resume prompt display and dismissal behavior

## Progress Log
- Added deterministic snapshot export and restore support on top of the classic game engine
- Added a local snapshot store abstraction and SharedPreferences-backed Android implementation
- Added a resume-aware controller that surfaces a restore/discard prompt on launch
- Wired the main Compose app to the resume flow and corruption notice handling

## Validation Results
- `./gradlew test -Pandroid.builder.sdkDownload=false` passed
- `./gradlew lint -Pandroid.builder.sdkDownload=false` passed
- `./gradlew assembleDebug -Pandroid.builder.sdkDownload=false` passed
- Snapshot round-trip, corruption handling, and resume prompt controller tests pass

## Final Summary
- Unfinished classic matches can now be restored safely after app restart
