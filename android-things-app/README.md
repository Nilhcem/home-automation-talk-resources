## Configuration

### Firebase

* Create a new project on the [Firebase console](https://console.firebase.google.com) and keep the Project ID somewhere (you will need it soon)
* Add an Android app to this project, with the following package name: `com.nilhcem.androidthings.homeautomation`
* Download the `google-services.json` file and save it to the `app` directory
* Create a new Cloud Firestore Database
* Enable Email/Password Authentication and add a user via the Firebase console
* Modify the `FIREBASE_EMAIL` and `FIREBASE_PASSWORD` from the `app/build.gradle`, optionally reduce the sign-up quota
* Update your Firestore Security Rules
```
service cloud.firestore {
  match /databases/{database}/documents {
    match /users/{userId}/{document=**} {
      allow read, write: if request.auth.uid == userId;
    }
  }
}
```
