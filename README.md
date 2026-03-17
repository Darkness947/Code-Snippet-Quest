# 🎮 Code Snippet Quest

> A gamified Android quiz app that challenges you to read Java code snippets and predict their output — level by level.

---

## 📖 About

**Code Snippet Quest** is an Android application built with Kotlin that helps developers sharpen their code-reading skills through an interactive quiz format. Players progress through four levels covering core Java concepts, earning scores and tracking their history along the way.

---

## ✨ Features

| Feature | Description |
|---|---|
| 🔐 **Authentication** | Register & login with a local SQLite account |
| 🎯 **4 Levels** | Fundamentals → Control Flow → OOP → Advanced |
| 🔒 **Level Unlocking** | Complete a level with ≥ 60% to unlock the next |
| 🅰️ **A/B/C/D Options** | All answer choices are clearly labelled |
| 💡 **Hints** | Each question has a conceptual clue (not the answer!) |
| 📊 **Score History** | Full per-level history with date, score %, and PASS/FAIL |
| 🌙 **Dark Mode** | Full dark theme across every screen |
| 🌍 **Arabic + RTL** | Complete Arabic translation with right-to-left layout |

---

## 📱 Screenshots

> *Coming soon*

---

## 🗺️ App Flow

```
Splash Screen
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
| **Language** | Kotlin |
| **UI** | XML Layouts, ViewBinding, ConstraintLayout, CardView |
| **Database** | SQLite via `SQLiteOpenHelper` |
| **Architecture** | Single-activity per screen (Activity-based) |
| **Theming** | Material Components, `values-night/` dark mode |
| **Localization** | `values-ar/` Arabic strings, `attachBaseContext` locale injection |

---

## 📂 Project Structure

```
app/src/main/
├── java/com/vigilante/codesnippetquest/
│   ├── MyApplication.kt          # App-level locale + dark mode setup
│   ├── data/
│   │   └── DatabaseHelper.kt     # SQLite schema, queries, data classes
│   └── ui/
│       ├── splash/SplashActivity.kt
│       ├── auth/
│       │   ├── LoginActivity.kt
│       │   └── RegisterActivity.kt
│       ├── home/HomeActivity.kt
│       ├── game/GameplayActivity.kt
│       ├── history/
│       │   ├── HistoryActivity.kt
│       │   └── HistoryAdapter.kt
│       └── settings/SettingsActivity.kt
└── res/
    ├── layout/                   # XML layouts for each screen
    ├── values/                   # Colors, strings (English)
    ├── values-ar/                # Arabic string translations
    └── values-night/             # Dark mode color overrides
```

---

## 🗃️ Database Schema

### `users`
| Column | Type | Notes |
|---|---|---|
| `id` | INTEGER PK | Auto-increment |
| `username` | TEXT | Unique per user |
| `password` | TEXT | Plain text (educational app) |
| `unlocked_level` | INTEGER | Starts at 1 |

### `questions`
| Column | Type | Notes |
|---|---|---|
| `id` | INTEGER PK | |
| `level` | INTEGER | 1 – 4 |
| `snippet` | TEXT | Java code (always English) |
| `question_text` | TEXT | English question |
| `question_text_ar` | TEXT | Arabic question |
| `option_a/b/c/d` | TEXT | Answer choices (always English) |
| `correct_answer` | TEXT | "A", "B", "C", or "D" |
| `hint` | TEXT | Conceptual clue |

### `history`
| Column | Type | Notes |
|---|---|---|
| `id` | INTEGER PK | |
| `user_id` | INTEGER FK | → `users.id` |
| `level_name` | TEXT | e.g. "Fundamentals" |
| `level_number` | INTEGER | 1 – 4 |
| `score_percentage` | INTEGER | 0 – 100 |
| `status` | TEXT | "PASS" or "FAIL" |
| `date` | TEXT | "Mar 17, 2026" |

---

## 🌍 Localization

The app supports **English** and **Arabic** (RTL):

- Switch language from **Settings → Language toggle**
- The entire app stack restarts to apply the new locale
- **Kept in English always:** App name, code snippets, answer options
- **Translated:** All UI labels, question text, level names, history, toasts

---

## 🎮 Level Content

| Level | Topic | Questions |
|---|---|---|
| 1 – Fundamentals | String concatenation, `==` vs `.equals()`, switch fall-through | 3 |
| 2 – Control Flow | `continue` in loops, post/pre-increment, ternary operators | 3 |
| 3 – OOP | String immutability, constructor chaining, static fields | 3 |
| 4 – Advanced | try/catch/finally, array bounds, pass-by-reference | 3 |

---

## 🚀 Getting Started

### Prerequisites
- Android Studio Hedgehog or later
- Android SDK 26+
- Kotlin 1.9+

### Build & Run

```bash
# Clone the repo
git clone https://github.com/Darkness947/Code-Snippet-Quest.git

# Open in Android Studio and sync Gradle, then run on a device or emulator
./gradlew assembleDebug
./gradlew installDebug
```

> **Note:** On first launch after a fresh install the database is created automatically with all 12 questions seeded.

---

## 🤝 Contributing

Pull requests are welcome! For major changes, please open an issue first to discuss what you would like to change.

---

## 📄 License

This project is for educational purposes.

---

*Built with ❤️ using Kotlin & Android Studio*
