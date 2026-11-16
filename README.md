# TypeRush

A native Android typing-speed test and multiplayer typing race app.

## Features

- Offline typing tests with 15, 30, 60, and 120 second durations
- Live WPM, accuracy, and error tracking
- Character-level correct/incorrect highlighting
- Local result history
- Multiplayer races with private rooms and live progress

## Android app

Native Kotlin and Jetpack Compose. Package: `com.typerush.app`.

## Multiplayer setup

Multiplayer requires a Firebase project, Android app registration for `com.typerush.app`, Firebase Authentication (anonymous sign-in), and Realtime Database. Place `google-services.json` in `app/`. Never commit service-account credentials or private keys.

## Build

Open in Android Studio with JDK 17. GitHub Actions builds a debug APK on pushes and publishes a release APK when a version tag is pushed.
