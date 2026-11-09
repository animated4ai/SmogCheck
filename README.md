# SmogCheck — Native Android App

Native Kotlin + Jetpack Compose Material 3 app that estimates smog from a photo.
Fully offline — no network needed at runtime.

- Package: com.smogcheck.app, version 1.0 (versionCode 1)
- minSdk 24 (Android 7.0), targetSdk 34
- Home: two big gradient buttons — Camera (full-res capture via FileProvider) and Gallery
- Preview screen with Analyze button
- Result: animated score gauge /100, color-coded severity (English + Urdu), Urdu health tips
- Algorithm: faithful Kotlin port of SmogCheck-v3.html analyze() (blue-dominance /
  saturation / brightness on upper-70% pixels, downscaled to 480px, cloud guards)

## Build

Requires the local toolchain (see ~/workspace/toolchain/):
  cd smogcheck && ~/workspace/toolchain/gradle-with-proxy.sh assembleRelease --offline
APK: app/build/outputs/apk/release/app-release.apk (signed with smogcheck release keystore)

Note: signing.properties and release.keystore are NOT included in this archive.

## Web version
`web/index.html` — the same analyzer as a single-file web app. Open it in any browser, no server needed. Works with outdoor sky photos.
