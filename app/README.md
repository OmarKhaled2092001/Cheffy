# 🍽️ Food Planner Android Application

An Android mobile application that helps users plan their weekly meals, discover new recipes, and manage their favorite meals — even offline.

This app is built using **TheMealDB API** and follows modern Android development practices with **RxJava**, **Room**, and **Firebase**.


## 📌 Brief Description

Food Planner helps users:
- Discover meals and recipes
- Plan meals for the current week
- Save favorite meals for offline access
- Search meals using multiple filters
- Sync and restore data after login

The application consumes data from:  
**TheMealDB.com – Free Recipe API**

---

## ✨ Features

### 🥗 Meal Discovery
- **Meal of the Day** for daily inspiration
- Browse meals by:
  - Category
  - Country
  - Ingredient
- View popular meals by country

### 🔍 Search
- Search meals by:
  - Country
  - Ingredient
  - Category

### ❤️ Favorites (Offline Support)
- Add or remove meals from favorites
- Favorites stored locally using **Room**
- Access favorites without network connection

### 📅 Weekly Meal Planning
- Add meals to the current week plan
- View planned meals offline

### 🔐 Authentication
- Email & Password Login / Signup
- Social Login (Google / Facebook)
- Guest Mode (limited access)
- Firebase Authentication
- Auto-login using **SharedPreferences**

### ☁️ Cloud Sync
- Backup & restore favorites and weekly plans using **Firebase**
- User data retrieved automatically after login

### 🍽️ Meal Details
Each meal includes:
1. Meal name
2. Meal image
3. Origin country
4. Ingredients (with images where possible)
5. Cooking steps
6. Embedded cooking video (not a URL)
7. Add / Remove from favorites button

### 🎬 UI & UX
- Splash screen with **Lottie animation**
- Clean and user-friendly interface

---

## 🛠️ Tech Stack

- **Language:** Java
- **Architecture:** MVP
- **Networking:** Retrofit
- **Reactive Programming:** RxJava
- **Local Storage:** Room Database
- **Authentication & Sync:** Firebase
- **UI:** XML
- **Animations:** Lottie
- **API:** TheMealDB

---

## 🚀 How to Run the Project

1. Open the project in **Android Studio**
2. Sync Gradle files
3. Run the app on an emulator or physical device

---

## 👤 User Modes

### Guest User
- View categories
- Search meals
- View meal of the day

### Registered User
- Add favorites
- Plan weekly meals
- Sync & restore data

---

## 👥 Team Members

- **Omar Khaled Jaafar**

