# Task Contract
Downgrade the Android build toolchain to the newest AGP version supported by the user's current Android Studio installation.

## Relevant PLAN.md Milestone
Post-v1 maintenance and build-tool compatibility

## Local Implementation Plan
- Align AGP from `9.1.0` to `9.0.0`
- Align the Gradle wrapper from `9.3.1` to `9.1.0`
- Update build docs to reflect the supported Android Studio toolchain

## Architecture Notes
- This task changes build tooling only; app architecture and gameplay logic stay unchanged

## Test Plan
- `test --console=plain -Pandroid.builder.sdkDownload=false`
- `lint --console=plain -Pandroid.builder.sdkDownload=false`
- `assembleDebug --console=plain -Pandroid.builder.sdkDownload=false`

## Progress Log
- Identified the incompatibility between repo AGP `9.1.0` and the user's Android Studio support ceiling of AGP `9.0.0`
- Applied the official AGP/Gradle compatible downgrade pair

## Validation Results
- `gradlew.bat test --console=plain -Pandroid.builder.sdkDownload=false`
- `gradlew.bat lint --console=plain -Pandroid.builder.sdkDownload=false`
- `gradlew.bat assembleDebug --console=plain -Pandroid.builder.sdkDownload=false`
- All three commands completed successfully after the AGP/Gradle downgrade

## Final Summary
- The project now targets AGP `9.0.0` with Gradle `9.1.0`, matching the Studio compatibility ceiling reported by the user while preserving a clean local build.
