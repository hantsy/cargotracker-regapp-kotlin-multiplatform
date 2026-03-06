# CargoTracker RegApp(Kotlin Multiplatform)

[The original DDD book sample regapp](https://github.com/citerus/dddsample-regapp) was written in Swing and Spring, which was used for submitting handling events to the [cargotracker core system](https://github.com/hantsy/cargotracker) (forked from [eclipse-ee4j/cargotracker](https://github.com/eclipse-ee4j//cargotracker)).

Previously, I have created two variants with JavaFX and Quarkus JavaFX:
* [Cargotacker Regapp(JavaFX)](https://github.com/hantsy/cargotracker-regapp-javafx)
* [Cargotacker Regapp(Quarkus JavaFX)](https://github.com/hantsy/cargotracker-regapp-javafx)

This project aims to rewrite the same function in Kotlin Multiplatform, targeting Android, iOS, Desktop (JVM), Web, and more.

>[!WARNING]
> I am a newbie to Kotlin Multiplatform. The code may look ugly, and it was mainly written with the help of Google Gimini after I read the [Quick Start](https://kotlinlang.org/docs/multiplatform/quickstart.html) guide. 

* [/sharedUI](./sharedUI/src) holds code that’s shared across all your Kotlin modules. The `sharedUI` module replaces the
  former `composeApp` hierarchy and contains multiple source sets:
  - [commonMain](./sharedUI/src/commonMain/kotlin) for code used on every platform.
  - Platform‑specific folders (iosMain, androidMain, jvmMain, jsMain, etc.) for next‑level customizations.
    For example, any iOS‑only APIs should go into `iosMain`, while desktop (JVM) logic belongs in `jvmMain`.

* [/client](./client) defines the HTTP client layer for remote API interaction. Use this module to
  encapsulate networking, serialization, and request/response models independent of UI concerns.

* [/webApp](./webApp) contains the web application front end. It hosts your Compose for Web UI or other
  browser‑based code and depends on `sharedUI` for business logic.

* [/desktopApp](./desktopApp) provides a desktop (JVM) application entry point. It typically uses
  Compose for Desktop and pulls shared logic from `sharedUI`.

* [/androidApp](./androidApp) bundles the Android application sources, layouts, and Gradle setup.

* [/iosApp](./iosApp/iosApp) still contains the iOS application launcher. If you’re blending in SwiftUI or
  other native views, this is the place for those components.


### Build and Run Android Application

Use the IDE's run configurations or execute via Gradle:
- macOS/Linux
  ```shell
  ./gradlew :androidApp:assembleDebug
  ```
- Windows
  ```shell
  .\gradlew.bat :androidApp:assembleDebug
  ```

### Build and Run Desktop (JVM) Application

Launch using your IDE or from the command line:
- macOS/Linux
  ```shell
  ./gradlew :desktopApp:run
  ```
- Windows
  ```shell
  .\gradlew.bat :desktopApp:run
  ```

### Build and Run Web Application

If your webApp module supports `compose-web`, start it with:
- macOS/Linux
  ```shell
  ./gradlew :webApp:browserDevelopmentRun
  ```
- Windows
  ```shell
  .\gradlew.bat :webApp:browserDevelopmentRun
  ```

### Build and Run iOS Application

Open the [/iosApp](./iosApp) directory in Xcode or use the IDE's iOS run config. Compile and run
from Xcode as you normally would.


---

Learn more about [Kotlin Multiplatform](https://www.jetbrains.com/help/kotlin-multiplatform-dev/get-started.html)…
