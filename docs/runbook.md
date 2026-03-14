# Runbook

## Local Validation
- Use JDK 17
- Use the local Android SDK installation
- Run validation with:

```bash
./gradlew test -Pandroid.builder.sdkDownload=false
./gradlew lint -Pandroid.builder.sdkDownload=false
./gradlew assembleDebug -Pandroid.builder.sdkDownload=false
```

## Notes
- The current local environment already has Android SDK Platform 36 installed
- If Gradle attempts an unnecessary SDK auto-download, keep the `android.builder.sdkDownload=false` flag enabled

