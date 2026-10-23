# Social Hub — Android starter project

Social Hub is a basic native Android app that puts shortcuts to social platforms in one place and tries to load them inside an Android WebView. It also provides an **Open externally** option for sites that block embedded browsers or require their official app.

## Included platforms
- Facebook
- TikTok
- Instagram
- YouTube
- WhatsApp Web
- X
- Reddit
- LinkedIn

## Open and build
1. Install Android Studio on a computer.
2. Extract this ZIP.
3. In Android Studio choose **Open** and select the `SocialHub` folder.
4. Allow Gradle sync to finish. Android Studio may download Gradle and Android SDK components.
5. Connect an Android phone with USB debugging enabled, or create an emulator.
6. Press **Run** to install on your phone.
7. To create an APK, select **Build → Build Bundle(s) / APK(s) → Build APK(s)**.
8. The debug APK is usually generated at `app/build/outputs/apk/debug/app-debug.apk`.

## Important limitations
- This ZIP is the source project, not a prebuilt APK. Building an APK requires Android Studio / Android SDK.
- Social platforms control their own sign-in flows and may block WebView, show warnings, or require their official apps. Use **Open externally** when a site does not work inside the app.
- This app does not bypass platform restrictions, combine feeds, or provide a shared login.
- Before public release, add a proper app icon, privacy policy, testing, and review each platform's current terms and developer policies.
- The current Favorites and Settings controls are placeholders for a future version.
