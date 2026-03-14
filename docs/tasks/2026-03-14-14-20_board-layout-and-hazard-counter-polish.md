# Task Contract
Fix the remaining gameplay UI issues reported after v1: board width overflow, incorrect panel order, weak hidden-vs-empty tile contrast, and end-of-round hazard count resets.

## Relevant PLAN.md Milestone
Post-v1 maintenance and gameplay polish

## Local Implementation Plan
- Move the displayed hazard count into controller-owned state so it can freeze at the last active value after win/loss
- Rework board sizing so tile width is computed from the padded content area instead of the full card width
- Reorder the lower panels so `Actions` appears immediately below the board
- Increase visual separation between hidden and revealed empty tiles

## Architecture Notes
- This task keeps the game rules unchanged and only adjusts controller presentation state plus Compose layout/styling

## Test Plan
- Add controller tests for active hazard-count updates and preserving the final active value after win/loss
- Run `test`, `lint`, `assembleDebug`, `connectedDebugAndroidTest`, and `installDebug`
- Perform emulator screenshot review for board width and panel order

## Progress Log
- Added `ResumeGameControllerHazardCountTest` to lock the displayed hazard-count behavior
- Moved the board size calculation to the padded board content width so all tiles fit onscreen without sideways clipping
- Reordered the screen so `Actions` renders before `Profile`
- Strengthened hidden/revealed tile separation with darker hidden fills and visible tile borders

## Validation Results
- `test --console=plain -Pandroid.builder.sdkDownload=false`
- `lint --console=plain -Pandroid.builder.sdkDownload=false`
- `assembleDebug connectedDebugAndroidTest installDebug --console=plain -Pandroid.builder.sdkDownload=false`
- Emulator screenshot review confirmed the board fits the viewport and `Actions` now appears directly below the board

## Final Summary
- Fixed the reported board/layout/presentation issues without changing gameplay rules, and preserved the hazard counter’s final active value after matches end.
