# Novix 
## About the project

Discover, explore, and enjoy movies like never before. Novix helps you dive into your favorite movies and instantly discover similar, trending titles you’ll love. Save your personal watchlist, enjoy a stunning design, and browse with ease.

[![Youtube teaser](https://github.com/user-attachments/assets/094f04d9-4faa-4284-9c21-42c3c08abdcb)](https://youtu.be/l6PnYc1RyFg)


## Novix As Brand

Novix was built with passion and dedication by the **Sana’a Squad** — a team brought together through the [**The Chance**](https://www.linkedin.com/company/thechance101/) Mentorship program.

Over eight weeks, we combined hard work, creativity, and collaboration to bring this vision to life.

<div align="center">
<img src="https://github.com/LondonSquad/Novix/blob/develop/assets/app-branding.png?raw=true" alt="Novix Brand Identity" width="400"/>
</div>

![Branded Image](https://github.com/user-attachments/assets/60925671-27ec-4ceb-be1c-2a8cc5373587)


## Features

| **Category**         | **Features**                                                       |
| -------------------- | ------------------------------------------------------------------ |
| **Search**           | Find your favorite movies, TV shows, and actors |
| **Movies**           | Explore filmography, photos, cast, and similar movies               |
| **TV Shows**         | Browse complete series info, photos, cast, and similar TV shows          |
| **Actors**           | View detailed biographies, complete filmographies, photos, and top movie and TV show highlights    |
| **Episode Tracking** | Track seasons, mark watched episodes, and monitor progress         |
| **Watchlists**       | Create, organize, and share personalized watchlists                |
| **Languages**        | Multi-language support for the bot: English and Arabic                    |
| **Themes**           | Switch between Light/Dark modes             |
| **Offline Mode**     | Access cached data for browsing without an internet connection     |
| **Content Safety**   | ML-powered moderation with parental controls for safe viewing      |


## Setup

### Requirements:
- **Android Studio** (latest stable version recommended)
- **JDK 17** or higher
- **Gradle**: 8.10.1 (current tested version — update to the latest if required)
- **TMDb Account**
- **Firebase Account** _(Optional)_
> TMDb (The Movie Database) is the service used to fetch movie and TV show data.

### 1. **Clone the repository**
   ```bash
   git clone https://github.com/SanaaTeam/Novix.git
   ```

### 2. Add API Configuration
After signing up for TMDb, retrieve your API key from [TMDb API](https://www.themoviedb.org/settings/api), Then, create a file named `keys.properties` in the project’s root directory
```properties
TMDB_API_KEY="your_api_key_here"
```

### 3. Firebase Setup (Optional)
- Create project at [Firebase Console](https://console.firebase.google.com/)
- Add `google-services.json` to `app/` folder
- Enable *Analytics*, *Crashlytics*, *Performance*

### 4. Build & Run
```bash
./gradlew build
```

## Architecture

```
.
├── app                    # Mobile application entry point
├── tvapp                  # TV application entry point
├── buildConfig            # Shared Gradle build configurations
├── imageViewer            # Jetpack Compose image viewer component with sensitive-content filtering
├── data
│   ├── localDataSource    # Local storage (Database, Datastore)
│   │   ├── identity       # Local user profile & authentication data
│   │   └── vod            # Local video-on-demand (VOD) storage
│   ├── remoteDataSource   # Remote APIs and network calls
│   │   ├── identity       # Remote user profile & authentication API
│   │   └── vod            # Remote VOD data (e.g., from TMDB API)
│   └── repositories
│       ├── identity       # Identity data repository
│       └── vod            # VOD data repository
├── domain                 # Business logic & use cases
│   ├── identity           # Authentication & user-related rules
│   └── vod                # VOD-specific business rules
└── feature
    ├── authentication     # Login, signup, session management
    ├── category           # Content categories & browsing
    ├── home               # Main dashboard  landing screen
    ├── mediaDetails       # Media details & metadata display
    ├── onboarding         # First-time user flow
    ├── playlists          # Playlist creation & management
    ├── search             # Content search
    └── userProfile        # User profile & settings
```

![Novix Modules](https://github.com/user-attachments/assets/aca43439-8065-4eb7-a1a5-ae88d0f13d4c)

## Technologies Used
- **Language**: Kotlin
- **Architecture**: Clean Architecture
- **Dependency Injection**: Dagger Hilt
- **Database**: Room
- **Networking**: [Retrofit](https://square.github.io/retrofit/)
- **Async Operations**: Kotlin coroutines
- **Test**: [JUnit 5](https://github.com/junit-team/junit-framework/), [Turbine](https://github.com/cashapp/turbine), [Kover](https://github.com/Kotlin/kotlinx-kover), [Truth](https://truth.dev/)
- **Image Loading**: [Coil](https://coil-kt.github.io/coil/)
- **Navigation**: Jetpack Navigation
## Contractors

- **Bareq AlTahma** – The chance instructor ([@iBareq](https://github.com/iBareq))
- **Hamsa Ali** – UI/UX designer ([@Hamsa Ali](https://www.linkedin.com/in/hamsa-ali-509b591b6/))
- **Falah Hasan** – Mentor ([@devfalah](https://github.com/devfalah))
- **Mohammed Sayed** – Mentor ([@mosayed01 ](https://github.com/mosayed01))
- **Ahmed Fikry** – Mentor ([@ahmedfikry24](https://github.com/ahmedfikry24))

## Contributors
<a href="https://github.com/SanaaTeam/Novix/graphs/contributors">
  <img src="https://contrib.rocks/image?repo=SanaaTeam/Novix" />
</a>


## License:

    Copyright 2025 The Chance

    Licensed under the Apache License, Version 1.0 (the "License");
    you may not use this file except in compliance with the License.
    You may obtain a copy of the License at

       http://www.apache.org/licenses/LICENSE-2.0

    Unless required by applicable law or agreed to in writing, software
    distributed under the License is distributed on an "AS IS" BASIS,
    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
    See the License for the specific language governing permissions and
    limitations under the License.



