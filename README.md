# SwapCampus

A campus marketplace application developed with **Kotlin Multiplatform (KMP)** and **Compose Multiplatform (CMP)** targeting **Android** and **iOS**.

---

## Features

- listing Catalog & Real-Time Search: Dynamic query matching across titles and descriptions with multi-category chips (LIVRES, COURS, ELECTRONIQUE, SPORT_LOISIRS, MOBILIER, VETEMENTS, AUTRE).
- Listing Details & Reservations: Detailed item view with real-time status transitions (Disponible -> Reservee) and confirmation snackbar notifications.
- Publishing Workflow: Client-validated submission form feeding directly into repository storage.
- Type-Safe Navigation: Built with Compose Multiplatform Navigation using @Serializable routes (ListingsRoute, DetailRoute, PostRoute, ProfileRoute).
- Unidirectional Data Flow (MVMM): Reactive UI powered by StateFlow, ListingsViewModel, and a sealed interface ListingsUiState.
- Dynamic Localization:
  - Resource-based string bundles for English and French (strings.xml).
  - Platform-specific expect/actual formatters for Euro currency and dates (LocalDate).
  - In-app language switching on Android (AppCompatDelegate) and system settings guidance on LOS via LanguageSelector.

---

## Architecture & Project Structure

- shared/src/commonMain/kotlin/.../domain: Domain models (CompteUtilisateur, Annonce, CatalogueAnnonces, Enums).
- shared/src/commonMain/kotlin/.../repository: Repositories (InMemoryListingRepository, InMemoryAuthRepository).
- shared/src/commonMain/kotlin/.../ui: Compose screens, navigation routes, and ListingsViewModel.
- shared/src/androidMain: Android-specific implementations (Formatters.android.kt, LanguageSelector.android.kt).
- shared/src/iosMain: LOS Foundation implementations (Formatters.ios.kt, LanguageSelector.ios.kt).

---

## Build & Run Instructions

### 1. Compile Shared Targets
``@bash
./gradlew :shared:compileAndroidMain
./gradlew :shared:compileKotlinIosSimulatorArm64
```

### 2. Android
With an active Android emulator or device connected:
```bash
./gradlew :androidApp:installDebug
```

### 3. iOS
Update iosApp workspace in Xcode, choose an iPhone simulator, press Cmd + R.
