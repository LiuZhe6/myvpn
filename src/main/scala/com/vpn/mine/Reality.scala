package com.vpn.mine

import java.io.{File, FileInputStream, FileOutputStream, InputStream}

import android.content._
import android.content.pm.{ApplicationInfo, PackageManager}
import android.content.res.{AssetManager, Configuration, Resources}
import android.database.DatabaseErrorHandler
import android.database.sqlite.SQLiteDatabase
import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.net.Uri
import android.os.{Bundle, Handler, Looper, UserHandle}
import android.view.Display

/**
  * Created by coder on 17-7-13.
  */
class Reality extends ServiceBoundContext{
  override def binderDied(): Unit = ???

  override def enforceCallingPermission(s: String, s1: String): Unit = ???

  override def getObbDir: File = ???

  override def deleteDatabase(s: String): Boolean = ???

  override def enforceCallingOrSelfPermission(s: String, s1: String): Unit = ???

  override def removeStickyBroadcastAsUser(intent: Intent, userHandle: UserHandle): Unit = ???

  override def enforceUriPermission(uri: Uri, i: Int, i1: Int, i2: Int, s: String): Unit = ???

  override def enforceUriPermission(uri: Uri, s: String, s1: String, i: Int, i1: Int, i2: Int, s2: String): Unit = ???

  override def checkPermission(s: String, i: Int, i1: Int): Int = ???

  override def getContentResolver: ContentResolver = ???

  override def getPackageResourcePath: String = ???

  override def isDeviceProtectedStorage: Boolean = ???

  override def databaseList(): Array[String] = ???

  override def getFilesDir: File = ???

  override def sendStickyOrderedBroadcastAsUser(intent: Intent, userHandle: UserHandle, broadcastReceiver: BroadcastReceiver, handler: Handler, i: Int, s: String, bundle: Bundle): Unit = ???

  override def fileList(): Array[String] = ???

  override def getWallpaper: Drawable = ???

  override def getDatabasePath(s: String): File = ???

  override def enforceCallingOrSelfUriPermission(uri: Uri, i: Int, s: String): Unit = ???

  override def getCacheDir: File = ???

  override def getTheme: Resources#Theme = ???

  override def startIntentSender(intentSender: IntentSender, intent: Intent, i: Int, i1: Int, i2: Int): Unit = ???

  override def startIntentSender(intentSender: IntentSender, intent: Intent, i: Int, i1: Int, i2: Int, bundle: Bundle): Unit = ???

  override def checkCallingOrSelfPermission(s: String): Int = ???

  override def getSystemServiceName(aClass: Class[_]): String = ???

  override def openOrCreateDatabase(s: String, i: Int, cursorFactory: SQLiteDatabase.CursorFactory): SQLiteDatabase = ???

  override def openOrCreateDatabase(s: String, i: Int, cursorFactory: SQLiteDatabase.CursorFactory, databaseErrorHandler: DatabaseErrorHandler): SQLiteDatabase = ???

  override def getFileStreamPath(s: String): File = ???

  override def getWallpaperDesiredMinimumHeight: Int = ???

  override def setTheme(i: Int): Unit = ???

  override def sendOrderedBroadcastAsUser(intent: Intent, userHandle: UserHandle, s: String, broadcastReceiver: BroadcastReceiver, handler: Handler, i: Int, s1: String, bundle: Bundle): Unit = ???

  override def bindService(intent: Intent, serviceConnection: ServiceConnection, i: Int): Boolean = ???

  override def checkCallingUriPermission(uri: Uri, i: Int): Int = ???

  override def getSystemService(s: String): AnyRef = ???

  override def moveSharedPreferencesFrom(context: Context, s: String): Boolean = ???

  override def removeStickyBroadcast(intent: Intent): Unit = ???

  override def getPackageCodePath: String = ???

  override def peekWallpaper(): Drawable = ???

  override def createConfigurationContext(configuration: Configuration): Context = ???

  override def startForegroundService(intent: Intent): ComponentName = ???

  override def startActivity(intent: Intent): Unit = ???

  override def startActivity(intent: Intent, bundle: Bundle): Unit = ???

  override def getApplicationContext: Context = ???

  override def sendStickyBroadcast(intent: Intent): Unit = ???

  override def getResources: Resources = ???

  override def getNoBackupFilesDir: File = ???

  override def getSharedPreferences(s: String, i: Int): SharedPreferences = ???

  override def grantUriPermission(s: String, uri: Uri, i: Int): Unit = ???

  override def getAssets: AssetManager = ???

  override def startInstrumentation(componentName: ComponentName, s: String, bundle: Bundle): Boolean = ???

  override def getWallpaperDesiredMinimumWidth: Int = ???

  override def getExternalFilesDir(s: String): File = ???

  override def sendStickyBroadcastAsUser(intent: Intent, userHandle: UserHandle): Unit = ???

  override def setWallpaper(bitmap: Bitmap): Unit = ???

  override def setWallpaper(inputStream: InputStream): Unit = ???

  override def sendStickyOrderedBroadcast(intent: Intent, broadcastReceiver: BroadcastReceiver, handler: Handler, i: Int, s: String, bundle: Bundle): Unit = ???

  override def deleteSharedPreferences(s: String): Boolean = ???

  override def createDisplayContext(display: Display): Context = ???

  override def createDeviceProtectedStorageContext(): Context = ???

  override def sendOrderedBroadcast(intent: Intent, s: String): Unit = ???

  override def sendOrderedBroadcast(intent: Intent, s: String, broadcastReceiver: BroadcastReceiver, handler: Handler, i: Int, s1: String, bundle: Bundle): Unit = ???

  override def getExternalFilesDirs(s: String): Array[File] = ???

  override def sendBroadcastAsUser(intent: Intent, userHandle: UserHandle): Unit = ???

  override def sendBroadcastAsUser(intent: Intent, userHandle: UserHandle, s: String): Unit = ???

  override def getExternalCacheDir: File = ???

  override def checkSelfPermission(s: String): Int = ???

  override def openFileInput(s: String): FileInputStream = ???

  override def getExternalMediaDirs: Array[File] = ???

  override def getDataDir: File = ???

  override def moveDatabaseFrom(context: Context, s: String): Boolean = ???

  override def getExternalCacheDirs: Array[File] = ???

  override def getCodeCacheDir: File = ???

  override def startActivities(intents: Array[Intent]): Unit = ???

  override def startActivities(intents: Array[Intent], bundle: Bundle): Unit = ???

  override def startService(intent: Intent): ComponentName = ???

  override def registerReceiver(broadcastReceiver: BroadcastReceiver, intentFilter: IntentFilter): Intent = ???

  override def registerReceiver(broadcastReceiver: BroadcastReceiver, intentFilter: IntentFilter, i: Int): Intent = ???

  override def registerReceiver(broadcastReceiver: BroadcastReceiver, intentFilter: IntentFilter, s: String, handler: Handler): Intent = ???

  override def registerReceiver(broadcastReceiver: BroadcastReceiver, intentFilter: IntentFilter, s: String, handler: Handler, i: Int): Intent = ???

  override def getPackageManager: PackageManager = ???

  override def getPackageName: String = ???

  override def getObbDirs: Array[File] = ???

  override def enforceCallingUriPermission(uri: Uri, i: Int, s: String): Unit = ???

  override def sendBroadcast(intent: Intent): Unit = ???

  override def sendBroadcast(intent: Intent, s: String): Unit = ???

  override def unbindService(serviceConnection: ServiceConnection): Unit = ???

  override def checkCallingOrSelfUriPermission(uri: Uri, i: Int): Int = ???

  override def getClassLoader: ClassLoader = ???

  override def createPackageContext(s: String, i: Int): Context = ???

  override def openFileOutput(s: String, i: Int): FileOutputStream = ???

  override def enforcePermission(s: String, i: Int, i1: Int, s1: String): Unit = ???

  override def checkUriPermission(uri: Uri, i: Int, i1: Int, i2: Int): Int = ???

  override def checkUriPermission(uri: Uri, s: String, s1: String, i: Int, i1: Int, i2: Int): Int = ???

  override def getMainLooper: Looper = ???

  override def getDir(s: String, i: Int): File = ???

  override def deleteFile(s: String): Boolean = ???

  override def clearWallpaper(): Unit = ???

  override def revokeUriPermission(uri: Uri, i: Int): Unit = ???

  override def revokeUriPermission(s: String, uri: Uri, i: Int): Unit = ???

  override def getApplicationInfo: ApplicationInfo = ???

  override def checkCallingPermission(s: String): Int = ???

  override def createContextForSplit(s: String): Context = ???

  override def unregisterReceiver(broadcastReceiver: BroadcastReceiver): Unit = ???

  override def stopService(intent: Intent): Boolean = ???
}
