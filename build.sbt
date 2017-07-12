scalaVersion := "2.11.8"

enablePlugins(AndroidApp)
android.useSupportVectors

versionCode := Some(1)
version := "0.1-SNAPSHOT"

instrumentTestRunner :=
  "android.support.test.runner.AndroidJUnitRunner"

platformTarget := "android-26"

javacOptions in Compile ++= "-source" :: "1.7" :: "-target" :: "1.7" :: Nil

libraryDependencies ++=
  "com.android.support" % "appcompat-v7" % "25.1.0" ::
    "com.android.support.test" % "runner" % "0.5" % "androidTest" ::
    "com.android.support.test.espresso" % "espresso-core" % "2.2.2" % "androidTest" ::
    "com.android.support" % "design" % "25.1.0" ::
    "com.google.code.gson" % "gson" % "2.8.1" ::
    "com.android.support" % "support-v4" % "25.1.0" ::
    "com.squareup.okhttp3" % "okhttp" % "3.8.0" ::
    "com.android.support" % "cardview-v7" % "25.1.0" ::
    "com.android.support" % "preference-v14" % "25.1.0" ::
    "com.evernote" % "android-job" % "1.1.3" ::
    "com.github.clans" % "fab" % "1.6.4" ::
    "com.github.jorgecastilloprz" % "fabprogresscircle" % "1.01" ::
    "com.google.android.gms" % "play-services-analytics" % "10.0.1" ::
    "com.google.android.gms" % "play-services-gcm" % "10.0.1" ::
    "com.j256.ormlite" % "ormlite-android" % "5.0" ::
    "com.mikepenz" % "fastadapter" % "2.1.5" ::
    "com.mikepenz" % "iconics-core" % "2.8.2" ::
    "com.mikepenz" % "materialdrawer" % "5.8.1" ::
    "com.mikepenz" % "materialize" % "1.0.0" ::
    "com.twofortyfouram" % "android-plugin-api-for-locale" % "1.0.2" ::
    "dnsjava" % "dnsjava" % "2.1.7" ::
    "eu.chainfire" % "libsuperuser" % "1.0.0.201704021214" ::
    "net.glxn.qrgen" % "android" % "2.0" ::
    "com.google.code.findbugs" % "jsr305" % "3.0.2" ::
    Nil
