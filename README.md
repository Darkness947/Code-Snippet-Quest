# 🎮 Code Snippet Quest

> A gamified Android quiz app that challenges you to read Java code snippets and predict their output — level by level.

---

## 📖 About

**Code Snippet Quest** is a modern Android application built with Jetpack Compose and Kotlin that helps developers sharpen their code-reading skills through an interactive quiz format. Players progress through four levels covering core Java concepts, earning scores and tracking their history along the way.

---

## ✨ Features

| Feature | Description |
|---|---|
| 🔐 **Authentication** | Register & login with local persistence |
| 🔑 **Security** | Password hashing via bcrypt |
| 🎯 **4 Levels** | Fundamentals → Control Flow → OOP → Advanced |
| 🔒 **Level Unlocking** | Complete a level with ≥ 60% to unlock the next |
| 🅰️ **A/B/C/D Options** | All answer choices are clearly labelled |
| 💡 **Hints** | Each question has a conceptual clue (not the answer!) |
| 📊 **Score History** | Full per-level history with date, score %, and PASS/FAIL |
| 🌙 **Instant Dark Mode** | Reactive theme switching without app restart |
| 🌍 **Arabic + RTL** | Complete Arabic translation with instant RTL layout switching |

---

## 📱 Screenshots

> *Coming soon*

---

## 🗺️ App Flow

```
Splash Screen (Compose)
    └─► Login / Register
            └─► Home (Level Select)
                    ├─► Gameplay (Questions + Hints)
                    │       └─► Result Toast → back to Home
                    ├─► Score History
                    └─► Settings (Language, Dark Mode, Logout)
```

---

## 🏗️ Tech Stack

| Layer | Technology |
|---|---|
| **Language** | Kotlin (2.0.2) |
| **UI** | Jetpack Compose (Material 3) |
| **Navigation** | Jetpack Navigation Compose |
| **Database** | Room Persistence Library |
| **Architecture** | Single-Activity Architecture with MVVM |
| **Reactive State** | Flow & StateFlow for data, Compose State for UI |
| **Localization** | Instant dynamic locale switching via ConfigurationContext |

---

## 📂 Project Structure

```
app/src/main/
├── java/com/vigilante/codesnippetquest/
│   ├── MainActivity.kt           # Single Activity with NavHost & Theme logic
│   ├── MyApplication.kt          # App-level container & DB initialization
│   ├── data/                     # Room Entities, DAOs, and Database setup
│   │   ├── User.kt
│   │   ├── Question.kt
│   │   ├── HistoryRecord.kt
│   │   ├── AppDao.kt
│   │   └── AppDatabase.kt
│   └── ui/                       # Compose Screens & ViewModels
│       ├── navigation/           # NavHost & Screen definitions
│       ├── theme/                # Material 3 Theme, Colors, Typography
│       ├── auth/                 # Login & Register screens
│       ├── home/                 # Home (Level Selection) screen
│       ├── game/                 # Gameplay & Quiz logic
│       ├── history/              # Score History screen
│       ├── settings/             # App Settings screen
│       └── splash/               # Animated Splash screen
└── res/
    ├── drawable/                 # Vector icons & logos
    ├── values/                   # Strings (English) & Base colors
    └── values-ar/                # Arabic string translations
```

---

## 🗃️ Database Schema (Room)

### `users`
| Column | Type | Notes |
|---|---|---|
| `id` | Int (PK) | Auto-generate |
| `username` | String | Unique |
| `password` | String | Plain text |
| `unlocked_level` | Int | Default: 1 |

### `questions`
| Column | Type | Notes |
|---|---|---|
| `id` | Int (PK) | |
| `level` | Int | 1 – 4 |
| `snippet` | String | Java code |
| `text` | String | English question |
| `textAr` | String | Arabic question |
| `opA/B/C/D` | String | Answer choices |
| `correct` | String | "A", "B", "C", or "D" |
| `hint` | String | Clue |

### `history`
| Column | Type | Notes |
|---|---|---|
| `id` | Int (PK) | |
| `user_id` | Int (FK) | → `users.id` |
| `levelName` | String | e.g. "Fundamentals" |
| `levelNumber` | Int | 1 – 4 |
| `score` | Int | 0 – 100 |
| `status` | String | "PASS" or "FAIL" |
| `date` | String | Formatted date |

---

## 🌍 Localization & Theme

The app supports **English** and **Arabic** (RTL) with **Instant Switching**:

- Switch language/theme from **Settings**.
- **Instant Recomposition**: The UI updates immediately without restarting the Activity.
- **RTL Support**: Layout direction flips automatically for Arabic.
- **Theming**: Full Material 3 support with custom Dark/Light color schemes.

---

## 🎮 Level Content

| Level | Topic | Questions |
|---|---|---|
| 1 – Fundamentals | Strings, `==` vs `.equals()`, switch cases | 3 |
| 2 – Control Flow | Loops, increments, ternary logic | 3 |
| 3 – OOP | Immutability, static fields, inheritance | 3 |
| 4 – Advanced | Exceptions, collections, streams | 3 |

---

## 🚀 Getting Started

### Prerequisites
- Android Studio Ladybug or later
- Android SDK 26+
- Kotlin 2.0+ (KSP required for Room)

### Build & Run

```bash
# Clone the repo
git clone https://github.com/Darkness947/Code-Snippet-Quest.git

# Open in Android Studio and sync Gradle
./gradlew assembleDebug
```

> **Note:** The app uses Room with KSP. Ensure you have the latest KSP plugin compatible with your Kotlin version.

---

## 🤝 Contributing

Pull requests are welcome! For major changes, please open an issue first to discuss what you would like to change.

---

## 📄 License

This project is for educational purposes.

---

*Built with ❤️ using Jetpack Compose & Kotlin*
