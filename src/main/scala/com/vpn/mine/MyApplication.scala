package com.vpn.mine

import java.io.{FileOutputStream, IOException}
import java.util
import java.util.Locale
import java.util.concurrent.TimeUnit

import android.annotation.SuppressLint
import android.app.Application
import android.content.res.Configuration
import android.os.{Build, LocaleList}
import android.preference.PreferenceManager
import android.support.v7.app.AppCompatDelegate
import android.util.Log
import com.evernote.android.job.JobManager
import com.google.android.gms.analytics.{GoogleAnalytics, HitBuilders, StandardExceptionParser}
import com.google.android.gms.common.api.ResultCallback
import com.google.android.gms.tagmanager.{ContainerHolder, TagManager}
import com.j256.ormlite.logger.LocalLog
import com.vpn.mine.database.{DBHelper, ProfileManager, SSRSubManager}
import com.vpn.mine.job.DonaldTrump
import com.vpn.mine.utils.{Executable, IOUtils, Key, Utils}
import eu.chainfire.libsuperuser.Shell
import com.vpn.mine.utils.CloseUtils.autoClose

import scala.collection.mutable.ArrayBuffer

/**
  * Created by coder on 17-7-13.
  */
object MyApplication {
  var app: MyApplication = _
  private final val TAG = "MyApplication"
  private val EXECUTABLES = Array(Executable.PDNSD, Executable.REDSOCKS, Executable.SS_TUNNEL, Executable.SS_LOCAL,
    Executable.TUN2SOCKS, Executable.KCPTUN)

  // The ones in Locale doesn't have script included
  private final lazy val SIMPLIFIED_CHINESE =
    if (Build.VERSION.SDK_INT >= 21) Locale.forLanguageTag("zh-Hans-CN") else Locale.SIMPLIFIED_CHINESE
  private final lazy val TRADITIONAL_CHINESE =
    if (Build.VERSION.SDK_INT >= 21) Locale.forLanguageTag("zh-Hant-TW") else Locale.TRADITIONAL_CHINESE


}
class MyApplication extends Application {
  def createFiles(): Unit = {

  }

  import MyApplication._
  final val SIG_FUNC = "getSignature"
  var containerHolder: ContainerHolder = _
  lazy val tracker = GoogleAnalytics.getInstance(this).newTracker(R.xml.tracker)
  lazy val settings = PreferenceManager.getDefaultSharedPreferences(this)
  lazy val editor = settings.edit
  lazy val profileManager = new ProfileManager(new DBHelper(this))
  lazy val ssrsubManager = new SSRSubManager(new DBHelper(this))
  lazy val resources = getResources()

  //默认是VPN连接方式
  def isNatEnabled = settings.getBoolean(Key.isNAT, false)
  def isVpnEnabled = !isNatEnabled

  // send event
  def track(category: String, action: String) = tracker.send(new HitBuilders.EventBuilder()
    .setAction(action)
//    .setLabel(BuildConfig.VERSION_NAME)
    .build())
  def track(t: Throwable) = tracker.send(new HitBuilders.ExceptionBuilder()
    .setDescription(new StandardExceptionParser(this, null).getDescription(Thread.currentThread.getName, t))
    .setFatal(false)
    .build())

  def profileId = settings.getInt(Key.id, -1)
  def profileId(i: Int) = editor.putInt(Key.id, i).apply
  def currentProfile = profileManager.getProfile(profileId)

  def switchProfile(id: Int) = {
    profileId(id)
    profileManager.getProfile(id) getOrElse profileManager.createProfile()
  }

  private def checkChineseLocale(locale: Locale): Locale = if (locale.getLanguage == "zh") locale.getCountry match {
    case "CN" | "TW" => null            // already supported
    case _ => locale.getScript match {  // fallback to the corresponding script
      case "Hans" => SIMPLIFIED_CHINESE
      case "Hant" => TRADITIONAL_CHINESE
      case script =>
        Log.w(TAG, "Unknown zh locale script: %s. Falling back to trying countries...".format(script))
        locale.getCountry match {
          case "SG" => SIMPLIFIED_CHINESE
          case "HK" | "MO" => TRADITIONAL_CHINESE
          case _ =>
            Log.w(TAG, "Unknown zh locale: %s. Falling back to zh-Hans-CN...".format(locale.toLanguageTag))
            SIMPLIFIED_CHINESE
        }
    }
  } else null

  @SuppressLint(Array("NewApi"))
  private def checkChineseLocale(config: Configuration): Unit = if (Build.VERSION.SDK_INT >= 24) {
    val localeList = config.getLocales
    val newList = new Array[Locale](localeList.size())
    var changed = false
    for (i <- 0 until localeList.size()) {
      val locale = localeList.get(i)
      val newLocale = checkChineseLocale(locale)
      if (newLocale == null) newList(i) = locale else {
        newList(i) = newLocale
        changed = true
      }
    }
    if (changed) {
      val newConfig = new Configuration(config)
      newConfig.setLocales(new LocaleList(newList.distinct: _*))
      val res = getResources
      res.updateConfiguration(newConfig, res.getDisplayMetrics)
    }
  } else {
    val newLocale = checkChineseLocale(config.locale)
    if (newLocale != null) {
      val newConfig = new Configuration(config)
      newConfig.locale = newLocale
      val res = getResources
      res.updateConfiguration(newConfig, res.getDisplayMetrics)
    }
  }

  override def onConfigurationChanged(newConfig: Configuration) {
    super.onConfigurationChanged(newConfig)
    checkChineseLocale(newConfig)
  }

  override def onCreate() {
    app = this
    if (!BuildConfig.DEBUG) java.lang.System.setProperty(LocalLog.LOCAL_LOG_LEVEL_PROPERTY, "ERROR")
    AppCompatDelegate.setCompatVectorFromResourcesEnabled(true)
    checkChineseLocale(getResources.getConfiguration)
    val tm = TagManager.getInstance(this)
    val pending = tm.loadContainerPreferNonDefault("GTM-NT8WS8", R.raw.gtm_default_container)
    val callback = new ResultCallback[ContainerHolder] {
      override def onResult(holder: ContainerHolder) {
        if (!holder.getStatus.isSuccess) {
          return
        }
        containerHolder = holder
        val container = holder.getContainer
        container.registerFunctionCallMacroCallback(SIG_FUNC,
          (functionName: String, parameters: util.Map[String, AnyRef]) => {
            if (functionName == SIG_FUNC) {
              Utils.getSignature(getApplicationContext)
            }
            null
          })
      }
    }
    pending.setResultCallback(callback, 2, TimeUnit.SECONDS)
    JobManager.create(this).addJobCreator(DonaldTrump)

    //快速启动，需要root权限
    /*if (settings.getBoolean(Key.tfo, false) == true && TcpFastOpen.supported) {
      new Thread {
        override def run() {
          TcpFastOpen.enabled(settings.getBoolean(Key.tfo, false))
        }
      }.start
    }*/
  }

  def refreshContainerHolder {
    val holder = app.containerHolder
    if (holder != null) holder.refresh()
  }

  private def copyAssets(path: String) {
    val assetManager = getAssets
    var files: Array[String] = null
    try files = assetManager.list(path) catch {
      case e: IOException =>
        Log.e(TAG, e.getMessage)
        app.track(e)
    }
    if (files != null) for (file <- files)
      autoClose(assetManager.open(if (path.nonEmpty) path + '/' + file else file))(in =>
        autoClose(new FileOutputStream(getApplicationInfo.dataDir + '/' + file))(out =>
          IOUtils.copy(in, out)))
  }



  def crashRecovery() {
    val cmd = new ArrayBuffer[String]()

    for (task <- Array("ss-local", "ss-tunnel", "pdnsd", "redsocks", "tun2socks", "proxychains")) {
      cmd.append("killall %s".formatLocal(Locale.ENGLISH, task))
      cmd.append("rm -f %1$s/%2$s-nat.conf %1$s/%2$s-vpn.conf"
        .formatLocal(Locale.ENGLISH, getApplicationInfo.dataDir, task))
    }
    if (app.isNatEnabled) {
      cmd.append("iptables -t nat -F OUTPUT")
      cmd.append("echo done")
      val result = Shell.SU.run(cmd.toArray)
      if (result != null && !result.isEmpty) return // fallback to SH
    }
    Shell.SH.run(cmd.toArray)
  }

  def copyAssets() {
    println("进入copyAssets")
    crashRecovery() // ensure executables are killed before writing to them
    println("准备进入copyAssets(System)")
    copyAssets(System.getABI)
    println("准备进入acl")
    copyAssets("acl")
    println("准备进入Shell.SH.run")
    Shell.SH.run(EXECUTABLES.map("chmod 755 " + getApplicationInfo.dataDir + '/' + _))
    println("copyAssets结束")
//    editor.putInt(Key.currentVersionCode, BuildConfig.VERSION_CODE).apply()
  }

  def updateAssets() = /*if (settings.getInt(Key.currentVersionCode, -1) != BuildConfig.VERSION_CODE)*/ copyAssets()

}