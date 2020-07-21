# draughts
Classic multi-player board game developed for Android

# intro
Also known as Checkers. This game uses some of Android's latest features such as RecyclerView to automate GUI 
updates for the draughtboard, and to populate the Lobby activity with Rooms. The Lobby screen allows for users
to host or join rooms, select game settings and finally start the game.

The multiplayer is powered by Google Cloud's Firestore, an asynchronous database that is used to store user,
room and draughtboard data. The lobby is updated manually with a refresh button, utilising Firestore's get()
command, while the game itself uses real-time listeners to check for updates.

# install and run
1. Clone this repository using Android Studio or git clone https://github.com/jonisabdulkarim/draughts.git
2. Create Firestore database at https://firebase.google.com/
3. Download and place google-services.json in draughts/app directory
4. Build project using gradlew
5. Run project with emulator on Android Studio
Note: You'll need two emulators for a multiplayer game!
