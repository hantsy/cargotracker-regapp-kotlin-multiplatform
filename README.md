# CargoTracker RegApp (Kotlin Multiplatform)

The [original sample application](https://github.com/citerus/dddsample-regapp) from the DDD book was implemented with Swing and Spring. It served as a front end for submitting event handling requests to the [CargoTracker core system](https://github.com/hantsy/cargotracker), itself a fork of [eclipse-ee4j/cargotracker](https://github.com/eclipse-ee4j/cargotracker).


In the past I developed two JavaFX–based variants:

* [CargoTracker RegApp (JavaFX)](https://github.com/hantsy/cargotracker-regapp-javafx)
* [CargoTracker RegApp (Quarkus JavaFX)](https://github.com/hantsy/cargotracker-regapp-quarkus-javafx)

The goal of the present project is to re‑implement the same functionality using Kotlin Multiplatform, with targets including Android, iOS, desktop JVM, the web, and potentially additional platforms.

>[!WARNING]
> I am new to Kotlin Multiplatform. The code may be inelegant; much of it was generated with the aid of Google Gemini after following the [Quick Start](https://kotlinlang.org/docs/multiplatform/quickstart.html) guide.


## Project Structure

* **`sharedUI`** (`./sharedUI/src`) contains code shared across all Kotlin modules. This module replaces the
  previous `composeApp` hierarchy and is organized into multiple source sets:
  * `commonMain` (`./sharedUI/src/commonMain/kotlin`) for platform‑agnostic code.
  * Platform‑specific sets (`iosMain`, `androidMain`, `jvmMain`, `jsMain`, etc.) for platform‑dependent APIs. For
    instance, iOS‑specific logic belongs in `iosMain`, whereas desktop (JVM) code lives in `jvmMain`.

* **`client`** (`./client`) implements the HTTP client layer used for remote API calls. This module encapsulates
  networking, serialization, and request/response models, keeping UI concerns separate.

* **`webApp`** (`./webApp`) hosts the web front end, typically built with Compose for Web or other browser‑based
  technologies. It depends on `sharedUI` for shared business logic.

* **`desktopApp`** (`./desktopApp`) provides the desktop (JVM) application entry point, usually employing
  Compose for Desktop and reusing shared logic from `sharedUI`.

* **`androidApp`** (`./androidApp`) contains the Android application code—sources, layouts, and Gradle
  configuration.

* **`iosApp`** (`./iosApp`) holds the iOS application launcher. Native SwiftUI views or other platform
  components are added here as needed.

### Building and Running

#### Android
Use the IDE run configuration or execute Gradle directly:

```bash
# macOS/Linux
./gradlew :androidApp:assembleDebug

# Windows
.\gradlew.bat :androidApp:assembleDebug
```

#### Desktop (JVM)

Start the desktop application via your IDE or the command line:

```bash
# macOS/Linux
./gradlew :desktopApp:run

# Windows
.\gradlew.bat :desktopApp:run
```

#### Web

For the `webApp` module (Compose for Web), launch the development server:

```bash
# macOS/Linux
./gradlew :webApp:browserDevelopmentRun

# Windows
.\gradlew.bat :webApp:browserDevelopmentRun
```

#### iOS

Open the `iosApp` directory in Xcode or use an IDE run configuration for iOS. Build and run it from
Xcode as usual.
