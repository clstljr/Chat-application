# YapYap

YapYap is an Android chat application built with Kotlin and Firebase. It allows users to create accounts, log in, set up a profile, view other registered users, and exchange one-to-one messages.

## Purpose

The project demonstrates how to build a mobile communication application with user authentication, profile management, a user directory, and real-time chat data.

## Main Features

- User registration with email and password
- User login using Firebase Authentication
- Profile name and profile-picture setup
- List of registered users
- One-to-one chat rooms
- Real-time message updates using Firebase Realtime Database
- Logout confirmation

## Technologies

- Kotlin
- Android Studio
- Android SDK
- Firebase Authentication
- Firebase Realtime Database
- Gradle Kotlin DSL
- Android View Binding
- Glide
- CircleImageView

## How the App Works

1. A user creates an account with an email address and password.
2. The user provides a name and can select a profile picture.
3. After logging in, the user sees other registered users.
4. Selecting a user opens a private chat screen.
5. Messages are saved and synchronized through Firebase Realtime Database.
6. The user can log out from the main screen.

## Project Value

YapYap demonstrates practical experience with Android development, Kotlin, Firebase integration, authentication, database listeners, image handling, and user-interface navigation.

## Repository

https://github.com/clstljr/Chat-application

## Notes

The app requires a Firebase project configured for the Android package `com.example.chatapplication`. A `google-services.json` file must be placed inside the `app` folder before building the application.

