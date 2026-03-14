# Task Contract
Implement local player progress storage for stats, achievements, settings, and the small launch theme set.

## Relevant PLAN.md Milestone
Milestone 4: Local persistence for resume, stats, achievements, settings, and themes

## Local Implementation Plan
- Define local storage contracts for match stats and achievements
- Add settings for theme choice and feedback preferences
- Apply the selected theme consistently across the app

## Architecture Notes
- Keep progress storage separate from in-progress match snapshots
- Theme and settings state should be easy to consume from Compose UI

## Test Plan
- Stats increment correctly after wins and losses
- Achievement unlock conditions persist locally
- Theme and settings values save and restore correctly

## Progress Log
- Task planned

## Validation Results
- Not started

## Final Summary
- Pending implementation
