# RentACarIntent-A2-temp

# 🚗 Rent A Car Intent – Assignment 2 (COS30017 Mobile Development)

This project is a **two-activity Android app** built using **Kotlin** and **Android Studio**.  
It demonstrates the use of **Intents**, **Parcelable data transfer**, **multi-screen UI design**, and **Material Design components** for a car rental system.

---

## 📱 Overview

**App Name:** Rent A Car Intent  
**Unit:** COS30017 – Mobile Development  
**Student Name:** Bilguun Ganzorig  
**GitHub Repository:** [https://github.com/BilguunGanzorig/RentACarIntent-A2-temp](https://github.com/BilguunGanzorig/RentACarIntent-A2-temp)

---

## 🎯 Objectives

This assignment demonstrates the ability to:
- Build a **multi-activity Android app** with Intents and Parcelable.
- Apply **UI/UX design principles** for mobile environments.
- Implement **data transfer** and **in-memory data management**.
- Use Android widgets such as `RecyclerView`, `RatingBar`, `Slider`, and `Switch`.
- Manage **user interaction and validation** through toasts, search, sort, and credit checks.
- Apply **Day/Night theme toggling** with `AppCompatDelegate`.

---

## ⚙️ Features

### 🧩 Functional
- Display **five cars** with name, model, year, rating, kilometres, and daily cost.
- **Next** button cycles through cars.
- **Rent** button launches a detail activity using a `Parcelable` intent.
- **Credit balance** system (starts at 500 credits, enforces ≤ 400 per rental).
- **Favourites** list (add/remove via heart toggle or swipe).
- **Search** cars by name or model.
- **Sort** cars by rating, year, or cost (menu options).
- **My Rentals** section displaying all booked cars with days and total cost.
- **Swipe-to-remove** from both *Favourites* and *My Rentals*.
- **Dark mode toggle** – instantly switches theme across both activities.
- **Toasts** notify user actions (added, booked, cancelled, removed).

### 🧰 Technical
- Kotlin with `Parcelable` data model.
- Two activities (`MainActivity`, `DetailActivity`).
- Custom adapters (`FavoriteAdapter`, `RentedAdapter`).
- Material 3 components and styles.
- Fully in-memory data model (no persistence).

---

## 🧠 Architecture

edu.swin.rentacar
├── data/
│ └── CarRepository.kt
├── model/
│ ├── Car.kt
│ └── Rental.kt
├── MainActivity.kt
├── DetailActivity.kt
├── FavoriteAdapter.kt
├── RentedAdapter.kt
└── res/
├── layout/
├── drawable/
├── values/
└── menu/

---

## 🧑‍💻 How It Works

1. The main screen lists one car at a time with **Next**, **Rent**, and **Favorite** buttons.  
2. Clicking **Rent** opens the **DetailActivity** using a `Parcelable` `Car` object.  
3. The user selects rental days using a **Slider** (1–7 days).  
4. On **Save**, the credit balance updates and the car moves to “My Rentals”.  
5. The user can:
   - Toggle **Dark Mode**
   - **Search** or **Sort**
   - Add/remove **Favourites**
   - Swipe to delete favourites or rentals.

---

## 🧪 Testing & Validation

| Test Case | Expected Outcome |
|------------|------------------|
| Rent a car under 400 cr | Booking succeeds, balance decreases |
| Rent a car above 400 cr | Toast “Each booking must be ≤ 400 credits” |
| Rent with insufficient balance | Toast “Insufficient balance” |
| Cancel booking | Returns to main without deduction |
| Add/remove favourite | Heart toggles and item appears/disappears |
| Swipe favourite/rental | Item removed with toast confirmation |
| Toggle dark mode | Theme switches for both screens |

---

## 🧩 Design & UI/UX Notes

- Uses **Material Design 3** components.
- **Dark Mode** enhances accessibility.
- Simple hierarchy: Toolbar → Search → Car Card → Favourites → Rentals.
- **Two reusable styles** for typography (`TextHeadline`, `TextBody`).
- **Responsive** layouts and scrollable content ensure usability on small screens.

=

## 🧩 Acknowledgements

- **Android Developers Documentation:** [https://developer.android.com](https://developer.android.com)  
- **Material Design 3 Components:** [https://m3.material.io](https://m3.material.io)  
- Free car images sourced from **Unsplash** and **Pexels** (royalty-free).  
- No external libraries beyond the AndroidX and Material 3 dependencies.

---

## 🧠 Reflections

> During development, I learned how to use Intents and Parcelable for multi-activity communication, manage state entirely in memory, and apply Kotlin idioms for clean Android code.  
> Implementing dark mode and swipe-to-remove actions improved my understanding of user-centric design for mobile environments.

---

## ⚖️ Academic Integrity

All code and documentation in this repository were authored by **Bilguun Ganzorig** for educational purposes.  
Generative AI tools (ChatGPT) were used **only for explanation and refinement**, not to automatically generate the entire project.  
All outputs were reviewed, tested, and adapted manually.

---

**© 2025 Bilguun Ganzorig**  
Swinburne University of Technology – COS30017 Mobile Development
