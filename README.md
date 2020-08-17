# Piloterr for Android

[![Join the chat at https://gitter.im/Piloterr/piloterr-android](https://badges.gitter.im/Piloterr/piloterr-android.svg)](https://gitter.im/Piloterr/piloterr-android?utm_source=badge&utm_medium=badge&utm_campaign=pr-badge&utm_content=badge)

[Piloterr](https://piloterr.com) is an open source habit building program which treats your life like a Role Playing Game. Level up as you succeed, lose HP as you fail, earn money to buy weapons and armor. This repository is related to the Android Native Application.

It's also on Google Play:

<a href="https://play.google.com/store/apps/details?id=com.piloterr.android.piloterr">
  <img alt="Get it on Google Play"
       width="185"
       src="https://play.google.com/intl/en_us/badges/images/generic/en-play-badge.png" />
</a>

Having the application installed is a good way to be notified of new releases. However, clicking "Watch" on this
repository will allow GitHub to email you whenever we publish a release.


# What's New

See the project's Releases page for a list of versions with their changelogs.

##### [View Releases](https://github.com/Piloterr/piloterr-android/releases)

If you Watch this repository, GitHub will send you an email every time we publish an update.

## Contributing

For an introduction to the technologies used and how the software is organized, refer to [Contributing to Piloterr](http://piloterr.wikia.com/wiki/Contributing_to_Piloterr#Coders_.28Web_.26_Mobile.29) - "Coders (Web & Mobile)" section.

Thank you very much [to all contributors](https://github.com/Piloterr/piloterr-android/graphs/contributors).

#### Steps for contributing to this repository:

1. Fork it
2. Create your feature branch: `git checkout -b my-new-feature`
3. Commit your changes: `git commit -am 'Add some feature'`
4. Push to the branch: `git push origin my-new-feature`
5. Create new Pull Request
   * Don't forget to include your Piloterr User ID, so that we can count your contribution towards your contributor tier

### Code Style Guidelines
We follow the code style guidelines outlined in [Android Code Style Guidelines for Contributors](https://source.android.com/source/code-style.html).

You can install our code style scheme to Intellij and/or Android Studio via this shell command:

    $ ./install-codestyle.sh

## Build Instructions

### Config Files

1. Setup Piloterr build config files by simply copying or renaming the example piloterr files:

   `piloterr.properties.example` to `piloterr.properties`

   `piloterr.resources.example` to `piloterr.resources`

   You also need `google-services.json`. Download it from Firebase in the next step.


   Note: this is the default production `piloterr.properties` file for piloterr.com. If you want to use a local Piloterr server, please modify the values in the properties file accordingly.




2. Go to https://console.firebase.google.com

   a. Register/Login to Firebase. (You can use a Google account.)

   b. Create a new project called Piloterr

   c. Create two apps in the project: `com.piloterr.android.piloterr` and `com.piloterr.android.piloterr.debug`

   d. Creating each app will generate a `google-services.json` file. Download the `google-services.json` file from the second app and put it in `\Piloterr\`

   You can skip the last part of the app creation wizards (where you run the app to verify installation).



3. If using Android Studio, click Sync Project with Gradle Files. Update Android Studio if it asks you to update. Run Piloterr.