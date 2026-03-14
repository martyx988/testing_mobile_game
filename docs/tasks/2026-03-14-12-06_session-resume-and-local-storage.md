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
- Task planned

## Validation Results
- Not started

## Final Summary
- Pending implementation
