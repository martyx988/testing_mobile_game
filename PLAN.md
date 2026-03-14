# PLAN.md

## Milestones
1. Project foundation and Android app architecture
2. Classic `Easy` game engine with tests
3. Compose game UI and player interaction flow
4. Local persistence for resume, stats, achievements, settings, and themes
5. Experimental `Trap Tiles` mode
6. Polish, accessibility pass, QA expansion, and documentation sync

## Sequencing Notes
- Build deterministic game logic before UI-heavy work
- Keep classic mode stable before introducing the variant mode
- Add persistence only after core game state contracts are clear
- Treat harder classic difficulties as post-v1 follow-up work

## Validation Strategy
- Tests first for public game logic
- Run unit tests before feature completion
- Run lint and debug build verification after app scaffolding exists
- Expand QA coverage for success, failure, and resume-state flows
