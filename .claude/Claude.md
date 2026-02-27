# Before writing code
- Explain clear and why
- If it is a new topic, do a deep dive, preferable use architect mode to deep dive
- always document existing findings, current solution
- Never write code without my approval

# Writing code
- Writing code should be with my approval and not without me approving
- All code should be testable
- Use solid principles as much as possible
- Use Android/KMP guidelines when writing code
- For Gradle always look at buildlogic folder for inspiration or for updating existing plugins
- Tests should always start with "Should"
- If tests are repeatable try to use Parameterized tests
- If test need data that is reusable in other tests, try to either use TestData class and if not exists it try to create it


## Issue/Task Context

To get information on the task youre working on, use the Notion MCP tool (mention this should be installed if not found)
Search for the Notion issue related to this branch by filtering the EngTask database on the branch name.
Always fetch this information if you re asked to "Work on this branch" / "Work on this task"

## Build & Test Commands
-
- Clean build: `./gradlew clean --parallel`
- Run tests: `./gradlew test --exclude-task androidApp:test androidApp:testBackenddevGoogleDebugUnitTest` or `make test`
- Run single test: `./gradlew :module:test --tests "TestClass.testMethod"`
- Kotlin lint: `./gradlew ktlintCheck` (run this after youre done coding)
- Format code: `./gradlew ktlintFormat`

# Server (kotlin with ktor)
- Complete server build: `./gradlew buildDockerImage` or `make build`
- Build /server only: `./gradlew server:compileKotlin`

# Android (kotlin)
- Android app compile: `./gradlew androidApp:compileBackenddevGoogleDebugKotlin`

# Backoffice (vue)
- Build: ./gradlew :backoffice:vue-build

# Ecom Client (vue)
- Build ./gradlew :ecomClient:vue-build

## Code Style
- Kotlin: Uses KtLint for formatting
- Excludes files in `generated/` directories from linting
- Use OptIns for experimental features
- Vue/TypeScript: Uses ESLint with 4-space indentation
- Prefers multiplatform Kotlin code where possible
- Follows functional patterns with Arrow for error handling
- Uses Koin for dependency injection for app related code
- Gradle Kotlin DSL for build configuration
- Always follow existing naming and formatting conventions in files

## Project Structure
- Shared Kotlin multiplatform code for business logic
- Android, iOS, and web frontends
- Uses compose-multiplatform for UI on iOS and android

Folder structure:
- /shared: The shared multiplatform code (server, apps)
- /server: All server related code
- /db: Server database related code, using SqlDelight
- /shared-ui: Shared ui viewmodels and ui for the ios and android app
- /shared-ui-compose: Shared compose ui viewmodels and ui for the ios and android app
- /androidApp: The android app custom code
- /iosApp: The iOS app custom code, you shouldnt need to touch this, use /shared-ui
- /backoffice: The vue backoffice app
- /ecomClient: The ecommerce vue app
- /buildlogic: Gradle plugins
- /.andrei/kds/prepstation: Kitchen display prepstation existent proposals/documentation

## Merge requests`
- For description use format:
### Problem
- Explain the problem in detail with reproduction steps

#### Example of a problem description
When users deselected specific statuses (like "On Hold") from the visible status filter in the Kitchen Display prep station, orders with that status were still being displayed instead of being hidden.
The bug occurred because:
- Individual orders were being filtered correctly when sections were expanded but section headers for collapsed sections were not checking the visibility filter.
This caused collapsed sections to appear even when all their orders should be filtered out and the result was confusing UX where sections appeared but contained no visible orders.

### Implementation
- Explain the solution in detail and why it was chosen and how it fixes the problem.

#### Code Changes
- Explain code change in detail and why it was done. Do not get into details of the implementation, show the classes involved and the changes made.

### Testing
- Explain how the solution was tested.

### Demo section
- If possible, add a video of the solution working.

## UI Components
- Try to reuse our own ViewModel level ui-state objects defined in androidApp/src/main/java/dev/tebi/ui/compose/components
    - These are viewModel level components so you dont need to create a view
- If you need to create custom compose views, try to reuse the  components in shared-ui-compose/core/src/commonMain/kotlin/dev/tebi/shared/ui/compose/components
    - For example the Text.kt component with a TextStyle
- Uses Jetpack Compose Material library (not Material3)
- Uses our `FunctionalColor`, and `TextStyle` system for styles.
- Use uiSizes (uiSizes.spacing) for spacing
- Any new ViewModel needs to be mapped to a View in androidApp/src/main/java/dev/tebi/ui/navigation/ViewModelSwitch.kt
- For padding in compose components use val `uiSizes = LocalUISizes.current`
- If you need to add spacing use uiSizes.spacing.dp for 8.dp and uiSizes.spacing(n).dp for multiples

## Localization

App And Backend translations:
- The app translation keys are found in shared/src/commonMain/resources/shared/strings-en.json
- Add new keys when needed, use "newStringKey": "Placeoholder translation"
- Run `./gradlew generateLocalizedStringKeys` to generate kotlin code which can be referred by "Strings.newStringKey".
- Do not add the translations key to the other language files (strings-nl.json etc). Its not needed.

Vue code translations:
- Translations for the backoffice (/backoffice) can be found here shared/src/commonMain/resources/shared/backoffice-strings-en.json
- The translations can be referred to using `t('newStringKey') in vue code
