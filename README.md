# KersCord Android

A branded Android shell for Discord's official web client. KersCord does not collect Discord passwords or user tokens and does not automate user accounts.

## Build an APK
1. Open the **Actions** tab in this repository.
2. Select **Build KersCord APK**.
3. Press **Run workflow** on the `main` branch.
4. When the run succeeds, open it and download the `KersCord-debug-apk` artifact.
5. Extract the ZIP and install `app-debug.apk` on Android.

A push to the app or Gradle files also triggers a build.

## Local build
Open the project in Android Studio with JDK 17 and Android SDK Platform 35, then run `gradle assembleDebug`.

## Discord compatibility
The app opens `https://discord.com/app` inside Android WebView. Discord may require sign-in through an external browser on some devices; use **OPEN IN BROWSER** if embedded sign-in is blocked. The Discord web client updates from Discord's servers. KersCord shell changes require a new APK.

## Updates and signing
The workflow produces a debug-signed APK for testing. A production release should use a private signing key stored in GitHub Actions secrets. Keep the same signing key for future app upgrades.
