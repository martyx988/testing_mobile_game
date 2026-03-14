# IMPLEMENT.md

## Implementation Rules
- Work in small, reviewable increments
- Write tests before implementing public game logic
- Keep board generation and state transitions deterministic
- Preserve clear, explicit error handling in rules, persistence, and resume flows
- Avoid unrelated refactors while milestones are active

## Validation Commands
Run these once the Android project scaffold exists:

```bash
./gradlew test
./gradlew lint
./gradlew assembleDebug
```

## Documentation Rules
- Update `DOCUMENTATION.md` at each major milestone
- Add or update a `docs/tasks/` file for detailed execution history
- Update architecture docs if system structure changes during implementation
