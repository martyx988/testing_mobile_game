# Task Contract
Add the light tutorial/help flow, accessibility improvements, and final polish needed for a credible v1 release candidate.

## Relevant PLAN.md Milestone
Milestone 6: Polish, accessibility pass, QA expansion, and documentation sync

## Local Implementation Plan
- Add concise tutorial/help content for new players
- Complete accessibility basics across core screens and states
- Refine polish items such as feedback, empty/error handling, and documentation updates

## Architecture Notes
- Keep help content lightweight and easy to update
- Accessibility improvements should apply to both classic and experimental modes

## Test Plan
- Tutorial/help entry and exit flow
- Large text, contrast, and non-color state communication checks
- Final regression pass over game, resume, settings, and variant flows

## Progress Log
- Added tests-first coverage for controller help state and the tutorial entry flow
- Implemented a lightweight tutorial dialog with Classic, Trap Tiles, controls, and symbol guidance
- Moved the `How to Play` action into the header after emulator review showed it was too far below the fold
- Added vertical scrolling for large-text safety and strengthened non-color guidance in the UI copy
- Tuned the Classic theme contrast after screenshot review showed the first pass was too washed out

## Validation Results
- `test --console=plain -Pandroid.builder.sdkDownload=false`
- `lint --console=plain -Pandroid.builder.sdkDownload=false`
- `assembleDebug --console=plain -Pandroid.builder.sdkDownload=false`
- `connectedDebugAndroidTest --console=plain -Pandroid.builder.sdkDownload=false`
- `installDebug --console=plain -Pandroid.builder.sdkDownload=false`
- Emulator screenshot review completed with the final help entry visible on the first screen

## Final Summary
- Added the final release-polish layer for v1: help/tutorial content, accessibility-safe scrolling and legends, stronger Classic theme contrast, and an emulator-verified tutorial entry flow.
