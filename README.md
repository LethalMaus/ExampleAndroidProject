# ExampleAndroidProject
Example Android project showing architecture choice, coding style, ideas, ...

## Architecture
Single-activity & MVVM

## Languages
Kotlin, XML, Gradle

## Retrofit
Library used for sending HTTP requests.

## Coroutines
Library used for multi-threading.

## LiveData & Observers
Typical libraries used in MVVM architecture.

## Shared Preferences
Used for persisting data local to the app. Can also be made secured

## Glide
Library used to load images hosted online that also allows for offline caching.
Can also show a placeholder and animations.

## Lottie Animations
TODO

## Testing

### Integration Tests
Also commonly known as UI tests.
Although more difficult to setup and maintain compared to unit tests, it covers a much higher range of potential bugs including some within individual android kernels.

#### Espresso
Most commonly used library used for integration/UI tests.

#### Mock Webserver
A library used for mocking API requests.
Uses self defined mock data which is returned on the loopback address so that the app knows no difference.

#### Custom Matchers
Some customer matchers have been added to help further test images, recycler views, backgrounds, ...

#### Idling Resources
TODO

### Jacoco
Library used for obtaining code coverage from tests.

## Build Variants
The app offers different build variants, typically used in cases such as offering a 'free' or 'paid' version of the app allowing different code sources to be used.
It can also be used for configuration between different environments such as integration, staging or a mock environment for testing.

## Firebase
### App Distribution
The app is available to download [here](https://appdistribution.firebase.dev/i/51f7a5c239a0e573)

#### Automated release notes
A script has been added to automate release notes with the commits since the previous app version.
This can be further refined by adding grep to the script to search for commits only containing something like a jira ticket reference.

### Tracking
Tracking has been added to show how to track what titles have been added/removed from favourites

### Remote Config
Allows making changes to the app without an update, such as turning features on/off.
It can also be used to allow for a simpler force app update, see below.

### Crashlytics
The app has been setup to send crash information to Firebase

## Force App Update
Some cases require the app to be updated flexibly or by force eg. code breaking API changes in older app version

## Phrase (i18n)
TODO

## SonarCloud
See [here](https://sonarcloud.io/project/overview?id=LethalMaus_ExampleAndroidProject)

## Security & Performance

### Obfuscation & Minify
When activated it removes unused code & libraries making the app smaller as well as shortening classes and members to reduce DEX file sizes, see [here](https://developer.android.com/studio/build/shrink-code)

### Network Configuration
An XML file containing restrictions on clear traffic, domain communication and allowing for certificate pinning, see [here](https://developer.android.com/training/articles/security-config)

### LeakCanary
This library checks for memory leaks on any when debugging.
