# TypeRush

A native Android typing-speed test app built with Kotlin and Jetpack Compose.

## Current MVP

- Offline typing tests with 15, 30, 60 and 120 second durations
- Live WPM, accuracy and error counters
- Correct and incorrect character highlighting
- Random practice passages
- On-device result history (up to 20 recent tests)
- Dark interface with a high-contrast lime accent

## Android app

Package: `com.typerush.app`. Open the project in Android Studio with JDK 17.

## Build and APK

GitHub Actions builds a debug APK on pushes to main/master and uploads it as a workflow artifact. Push a version tag such as `v1.0.0` to create a GitHub Release with the APK attached.

## Not implemented yet

Multiplayer room creation, joining, synchronized races, live opponent progress, and final multiplayer rankings are not implemented yet. The multiplayer screen is currently a UI placeholder; it does not connect players yet.
