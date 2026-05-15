# Grama-Urja 🌾⚡

**Community Powered Smart Irrigation**

A rural electricity monitoring Android application built with Kotlin and Jetpack Compose.

## Tech Stack

- **Language:** Kotlin
- **UI:** Jetpack Compose + Material 3
- **Architecture:** MVVM
- **State Management:** StateFlow + ViewModel
- **Navigation:** Navigation Compose
- **Persistence:** DataStore Preferences
- **Async:** Coroutines

## Screens

| Screen | Description |
|--------|-------------|
| Splash | Animated fade-in with app branding |
| Zone Selection | Searchable list of village/transformer zones |
| Home Dashboard | Live power status, ON/OFF controls, activity log |
| Pump Timer | Irrigation duration calculator by crop + pump HP |
| Settings | Dark mode, notifications, app info, Firebase placeholder |

## Mock Data

| Zone | Status |
|------|--------|
| Village A | ON |
| Village B | OFF |
| Village C | ON |
| Transformer Zone 1 | ON |
| Transformer Zone 2 | OFF |

## Setup

1. Clone the repository
2. Open in Android Studio (Hedgehog or later)
3. Run on emulator or physical device (API 24+)

## Project Structure

```
app/src/main/java/com/gramaUrja/
├── MainActivity.kt
├── data/
│   ├── model/          # Zone, ActivityItem, enums
│   └── repository/     # Mock data
├── ui/
│   ├── screens/        # All 5 screens
│   └── theme/          # Colors, Typography, Theme
├── viewmodel/          # MainViewModel with StateFlow
├── navigation/         # Route constants
└── utils/              # TimeUtils
```

## Future Plans
- Firebase Realtime Database integration
- Push notifications for power changes
- Multi-user / multi-device sync
- Historical power charts
