
# 6sense Android

A boilerplate android project for future use to make the development quicker.

## Features

- Fully **JetPack Compose** UI
- State Management
- Google's latest **Material3** Components
- DI with **Dagger Hilt**
- **KTOR** for network handling
- **JWT** support enabled via **KTOR Auth**
- **WorkManager** for task scheduling
- **Google Sign-In** enabled
- **Facebook Sign-In** implementation
- **Dokka** to manage documentation
- **FCM** Push notification
- **DataStore Preference** for local storage
- **Room** for local storage
- **buildSrc** for apk build management
## Technologies Used

**UI:** JetPack Compose, XML

**Language:** Kotlin


## Testing

- **MockK** for unit testing
- **Compose UI Test** for UI testing
- **Google Truth** for test result verification
## Project Structure

    app
    ├─ com.six.sense
    │  ├─ data
    │  │  ├─ local
    │  │  ├─ remote.dto
    │  │  └─ repo
    │  ├─ di
    │  ├─ domain
    │  │  ├─ model
    │  │  └─ repo
    │  ├─ presentation
    │  │  ├─ base
    │  │  ├─ navigation
    │  │  └─ screen
    │  ├─ ui.theme
    │  │  ├─ auth
    │  │  ├─ chat
    │  │  ├─ materialComponents
    │  │  ├─ profile
    │  │  └─ worker
    │  ├─ utils
    buildSrc

## Authors

- [@AhmadUmarMahdi](https://github.com/AhmadUmarMahdi)
- [@6senseRana](https://github.com/6senseRana)


## Documentation

[Full Documentation](https://6sensehq.github.io/6senseAndroid/)
