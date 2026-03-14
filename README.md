# Minesweeper Android App

Native Android Minesweeper built with Kotlin and Jetpack Compose.

## Requirements
- JDK 17
- Android SDK with API 35 or newer

## Common Commands
```bash
./gradlew test -Pandroid.builder.sdkDownload=false
./gradlew lint -Pandroid.builder.sdkDownload=false
./gradlew assembleDebug -Pandroid.builder.sdkDownload=false
```

## Current Status
- Android project scaffold is in place
- Core game logic and gameplay rules are still pending implementation

## Local SDK Note
If Gradle tries to auto-download SDK Platform 36 even though it already exists locally, rerun the command with `-Pandroid.builder.sdkDownload=false`.

