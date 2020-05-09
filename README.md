# Slide FM

## What is slide

Slide is an instant messenger app that is completely dependent on firebase. This app was built for interview assesment purposes so it's not a fully working IM.

### What's missing?
The following points indicates the most basic functionality that needs to be implemented to make the app a functional IM.

1. Push messages
2. Chat invitations (currently you can chat to any user that has signed up to the app)
3. Deletion of active conversations
4. End to End Encryption (if it needs to be a secure IM)

## Architecture and Dependencies
This project was built on the MVVM architecture, from a UI perspective, using android architecture components such as `LiveData`, `ViewModel`s and the like. It also focuses a bit more on domain centric design but doesn't fully adhere to DDD. I also made use of the `Android Navigation Component` for easily setting up a navigation graph and navigating between screens.

I used *dagger* for dependency injection, *glide* for easy image fetching and caching and *firebase* as my repository, auth service and storage. The other remaining dependencies are common to most android apps.

## Contribution
This app is not intended for public use, but if you would like to contribute for your own needs please feel free to do so. Only requirement is to stick to the implemented design as much as possible.
