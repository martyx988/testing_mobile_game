# Task Contract
Define the game domain model and implement deterministic board generation for classic `Easy` mode.

## Relevant PLAN.md Milestone
Milestone 2: Classic `Easy` game engine with tests

## Local Implementation Plan
- Define board, tile, coordinate, and match-state models
- Implement mine placement and adjacency count generation
- Lock deterministic behavior for testability

## Architecture Notes
- Keep generation logic platform-agnostic so it can be tested without Android UI
- Reserve room in the model for the later `Trap Tiles` variant

## Test Plan
- Board dimensions and mine counts
- Adjacency count correctness
- Deterministic output for seeded or otherwise specified generation behavior

## Progress Log
- Task planned

## Validation Results
- Not started

## Final Summary
- Pending implementation
