# Minesweeper Android App

Native Android Minesweeper built with Kotlin and Jetpack Compose.

## Requirements
- JDK 17
- Android SDK with API 35 or newer
- Android Studio with AGP 9.0 support or newer

## Common Commands
```bash
./gradlew test -Pandroid.builder.sdkDownload=false
./gradlew lint -Pandroid.builder.sdkDownload=false
./gradlew assembleDebug -Pandroid.builder.sdkDownload=false
./gradlew connectedDebugAndroidTest -Pandroid.builder.sdkDownload=false
```

## Current Status
- V1 gameplay is fully playable with `Classic Easy` and the `Trap Tiles` experimental mode
- Active games, stats, achievements, theme choice, and haptics settings persist locally
- The app includes a lightweight tutorial/help dialog and accessibility-oriented symbol guidance

## Local SDK Note
If Gradle tries to auto-download SDK Platform 36 even though it already exists locally, rerun the command with `-Pandroid.builder.sdkDownload=false`.

## Android Studio Compatibility
- The project is pinned to AGP `9.0.0` and Gradle `9.1.0` so it can sync in Android Studio builds that do not yet support AGP `9.1.0`.

## Debug APK
- Latest debug build output: `app/build/outputs/apk/debug/app-debug.apk`
