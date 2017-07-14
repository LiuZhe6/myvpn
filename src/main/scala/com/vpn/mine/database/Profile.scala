package com.vpn.mine.database

import java.util.Locale

import android.util.Base64
import com.j256.ormlite.field.{DataType, DatabaseField}

/**
  * Created by coder on 17-7-13.
  */
class Profile {
  @DatabaseField(generatedId = true)
  var id: Int = 0

  @DatabaseField
  var name: String = "Untitled"

  @DatabaseField
  var host: String = ""

  @DatabaseField
  var localPort: Int = 1080

  @DatabaseField
  var remotePort: Int = 8388

  @DatabaseField
  var password: String = ""

  @DatabaseField
  var protocol: String = "origin"

  @DatabaseField
  var protocol_param: String = ""

  @DatabaseField
  var obfs: String = "plain"

  @DatabaseField
  var obfs_param: String = ""

  @DatabaseField
  var method: String = "aes-256-cfb"

  @DatabaseField
  var route: String = "all"

  @DatabaseField
  var proxyApps: Boolean = false

  @DatabaseField
  var bypass: Boolean = false

  @DatabaseField
  var udpdns: Boolean = false

  @DatabaseField
  var url_group: String = ""

  @DatabaseField
  var dns: String = "208.67.222.222:53"

  @DatabaseField
  var china_dns: String = "114.114.114.114:53,223.5.5.5:53"

  @DatabaseField
  var ipv6: Boolean = false

  @DatabaseField(dataType = DataType.LONG_STRING)
  var individual: String = ""

  @DatabaseField
  var tx: Long = 0

  @DatabaseField
  var rx: Long = 0

  @DatabaseField
  var elapsed: Long = 0

  @DatabaseField
  val date: java.util.Date = new java.util.Date()

  @DatabaseField
  var userOrder: Long = _


  override def toString = "ssr://" + Base64.encodeToString("%s:%d:%s:%s:%s:%s/?obfsparam=%s&protoparam=%s&remarks=%s&group=%s".formatLocal(Locale.ENGLISH,
    host, remotePort, protocol, method, obfs, Base64.encodeToString("%s".formatLocal(Locale.ENGLISH,
      password).getBytes, Base64.NO_PADDING | Base64.URL_SAFE | Base64.NO_WRAP), Base64.encodeToString("%s".formatLocal(Locale.ENGLISH,
      obfs_param).getBytes, Base64.NO_PADDING | Base64.URL_SAFE | Base64.NO_WRAP), Base64.encodeToString("%s".formatLocal(Locale.ENGLISH,
      protocol_param).getBytes, Base64.NO_PADDING | Base64.URL_SAFE | Base64.NO_WRAP), Base64.encodeToString("%s".formatLocal(Locale.ENGLISH,
      name).getBytes, Base64.NO_PADDING | Base64.URL_SAFE | Base64.NO_WRAP), Base64.encodeToString("%s".formatLocal(Locale.ENGLISH,
      url_group).getBytes, Base64.NO_PADDING | Base64.URL_SAFE | Base64.NO_WRAP)).getBytes, Base64.NO_PADDING | Base64.URL_SAFE | Base64.NO_WRAP)

  def isMethodUnsafe = "table".equalsIgnoreCase(method) || "rc4".equalsIgnoreCase(method)
}
