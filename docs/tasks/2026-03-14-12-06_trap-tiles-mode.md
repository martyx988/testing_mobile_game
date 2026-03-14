# Task Contract
Implement the fixed experimental `Trap Tiles` mode with its own documented rule contract and tests.

## Relevant PLAN.md Milestone
Milestone 5: Experimental `Trap Tiles` mode

## Local Implementation Plan
- Finalize the trap-tile behavior contract before coding
- Extend the engine to support the variant without breaking classic mode
- Add UI cues that explain the variant clearly to players

## Architecture Notes
- Keep variant-specific logic isolated behind shared game interfaces where practical
- Avoid leaking experimental rules into classic mode behavior

## Test Plan
- Trap-tile placement and resolution behavior
- Variant win/loss flows
- Clear separation between classic and experimental rules

## Progress Log
- Finalized the Trap Tiles contract as an 8x8 mode with 8 mines and 4 trap hazards
- Extended deterministic board generation to place and count trap hazards separately from classic mode
- Updated the shared game engine so revealed traps end the run and reveal all hazards
- Added a UI mode switch plus descriptive copy so players can move between Classic Easy and Trap Tiles

## Validation Results
- `./gradlew test -Pandroid.builder.sdkDownload=false` passed
- `./gradlew lint -Pandroid.builder.sdkDownload=false` passed
- `./gradlew assembleDebug -Pandroid.builder.sdkDownload=false` passed
- Trap Tiles tests cover placement counts, determinism, hazard resolution, and controller mode switching

## Final Summary
- Trap Tiles is fully integrated as the app’s fixed experimental mode
