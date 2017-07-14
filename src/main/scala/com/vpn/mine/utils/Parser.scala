package com.vpn.mine.utils

import java.net.URLDecoder

import android.util.{Base64, Log}
import com.vpn.mine.database.Profile

/**
  * Created by coder on 17-7-13.
  */
object Parser {
  val TAG = "ShadowParser"
  private val pattern = "(?i)ss://([A-Za-z0-9+-/=_]+)(#(.+))?".r
  private val decodedPattern = "(?i)^((.+?)(-auth)??:(.*)@(.+?):(\\d+?))$".r

  private val pattern_ssr = "(?i)ssr://([A-Za-z0-9_=-]+)".r
  private val decodedPattern_ssr = "(?i)^((.+):(\\d+?):(.*):(.+):(.*):([^/]+))".r
  private val decodedPattern_ssr_obfsparam = "(?i)[?&]obfsparam=([A-Za-z0-9_=-]*)".r
  private val decodedPattern_ssr_remarks = "(?i)[?&]remarks=([A-Za-z0-9_=-]*)".r
  private val decodedPattern_ssr_protocolparam = "(?i)[?&]protoparam=([A-Za-z0-9_=-]*)".r
  private val decodedPattern_ssr_groupparam = "(?i)[?&]group=([A-Za-z0-9_=-]*)".r

  def findAll(data: CharSequence) = pattern.findAllMatchIn(if (data == null) "" else data).map(m => try
    decodedPattern.findFirstMatchIn(new String(Base64.decode(m.group(1), Base64.NO_PADDING), "UTF-8")) match {
      case Some(ss) =>
        val profile = new Profile
        profile.method = ss.group(2).toLowerCase
        if (ss.group(3) != null) profile.protocol = "verify_sha1"
        profile.password = ss.group(4)
        profile.name = ss.group(5)
        profile.host = profile.name
        profile.remotePort = ss.group(6).toInt
        if (m.group(2) != null) profile.name = URLDecoder.decode(m.group(3), "utf-8")
        profile
      case _ => null
    }
  catch {
    case ex: Exception =>
      Log.e(TAG, "parser error: " + m.source, ex)// Ignore
      null
  }).filter(_ != null)

  def findAll_ssr(data: CharSequence) = pattern_ssr.findAllMatchIn(if (data == null) "" else data).map(m => try{
    val uri = new String(Base64.decode(m.group(1).replaceAll("=", ""), Base64.URL_SAFE), "UTF-8")
    decodedPattern_ssr.findFirstMatchIn(uri) match {
      case Some(ss) =>
        val profile = new Profile
        profile.host = ss.group(2).toLowerCase
        profile.remotePort = ss.group(3).toInt
        profile.protocol = ss.group(4).toLowerCase
        profile.method = ss.group(5).toLowerCase
        profile.obfs = ss.group(6).toLowerCase
        profile.password = new String(Base64.decode(ss.group(7).replaceAll("=", ""), Base64.URL_SAFE), "UTF-8")

        decodedPattern_ssr_obfsparam.findFirstMatchIn(uri) match {
          case Some(param) =>
            profile.obfs_param = new String(Base64.decode(param.group(1).replaceAll("=", ""), Base64.URL_SAFE), "UTF-8")
          case _ => null
        }

        decodedPattern_ssr_protocolparam.findFirstMatchIn(uri) match {
          case Some(param) =>
            profile.protocol_param = new String(Base64.decode(param.group(1).replaceAll("=", ""), Base64.URL_SAFE), "UTF-8")
          case _ => null
        }

        decodedPattern_ssr_remarks.findFirstMatchIn(uri) match {
          case Some(param) =>
            profile.name = new String(Base64.decode(param.group(1).replaceAll("=", ""), Base64.URL_SAFE), "UTF-8")
          case _ => profile.name = ss.group(2).toLowerCase
        }

        decodedPattern_ssr_groupparam.findFirstMatchIn(uri) match {
          case Some(param) =>
            profile.url_group = new String(Base64.decode(param.group(1).replaceAll("=", ""), Base64.URL_SAFE), "UTF-8")
          case _ => null
        }

        profile
      case _ => null
    }
  }
  catch {
    case ex: Exception =>
      Log.e(TAG, "parser error: " + m.source, ex)// Ignore
      null
  }).filter(_ != null)
}

