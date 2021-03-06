package com.vpn.mine.database

import android.util.Log
import com.vpn.mine.MyApplication.app
import com.vpn.mine.entity.Node
import com.vpn.mine.utils.DataSaver
/**
  * Created by coder on 17-7-13.
  */
object ProfileManager {
  private final val TAG = "ProfileManager"
}

class ProfileManager(dbHelper: DBHelper) {
  import ProfileManager._

  var profileAddedListener: Profile => Any = _
  def setProfileAddedListener(listener: Profile => Any) = this.profileAddedListener = listener

  def createProfile(p: Profile = null): Profile = {
    val profile = if (p == null) new Profile else p
    profile.id = 0
    try {
      app.currentProfile match {
        case Some(oldProfile) =>
          // Copy Feature Settings from old profile
          profile.route = oldProfile.route
          profile.ipv6 = oldProfile.ipv6
          profile.proxyApps = oldProfile.proxyApps
          profile.bypass = oldProfile.bypass
          profile.individual = oldProfile.individual
          profile.udpdns = oldProfile.udpdns
        case _ =>
      }
      val last = dbHelper.profileDao.queryRaw(dbHelper.profileDao.queryBuilder.selectRaw("MAX(userOrder)")
        .prepareStatementString).getFirstResult
      if (last != null && last.length == 1 && last(0) != null) profile.userOrder = last(0).toInt + 1
      dbHelper.profileDao.createOrUpdate(profile)
      if (profileAddedListener != null) profileAddedListener(profile)
    } catch {
      case ex: Exception =>
        Log.e(TAG, "addProfile", ex)
        app.track(ex)
    }
    profile
  }

  def createProfile_dr(p: Profile = null): Profile = {
    val profile = if (p == null) new Profile else p
    profile.id = 0
    try {
      app.currentProfile match {
        case Some(oldProfile) =>
          // Copy Feature Settings from old profile
          profile.route = oldProfile.route
          profile.ipv6 = oldProfile.ipv6
          profile.proxyApps = oldProfile.proxyApps
          profile.bypass = oldProfile.bypass
          profile.individual = oldProfile.individual
          profile.udpdns = oldProfile.udpdns
          profile.dns = oldProfile.dns
          profile.china_dns = oldProfile.china_dns
        case _ =>
      }
      val last = dbHelper.profileDao.queryRaw(dbHelper.profileDao.queryBuilder.selectRaw("MAX(userOrder)")
        .prepareStatementString).getFirstResult
      if (last != null && last.length == 1 && last(0) != null) profile.userOrder = last(0).toInt + 1

      val last_exist = dbHelper.profileDao.queryBuilder()
        .where().eq("name", profile.name)
        .and().eq("host", profile.host)
        .and().eq("remotePort", profile.remotePort)
        .and().eq("password", profile.password)
        .and().eq("protocol", profile.protocol)
        .and().eq("protocol_param", profile.protocol_param)
        .and().eq("obfs", profile.obfs)
        .and().eq("obfs_param", profile.obfs_param)
        .and().eq("url_group", profile.url_group)
        .and().eq("method", profile.method).queryForFirst()
      if (last_exist == null)
      {
        dbHelper.profileDao.createOrUpdate(profile)
        if (profileAddedListener != null) profileAddedListener(profile)
      }
    } catch {
      case ex: Exception =>
        Log.e(TAG, "addProfile", ex)
        app.track(ex)
    }
    profile
  }

  def createProfile_sub(p: Profile = null): Int = {
    val profile = if (p == null) new Profile else p
    profile.id = 0
    try {
      app.currentProfile match {
        case Some(oldProfile) =>
          // Copy Feature Settings from old profile
          profile.route = oldProfile.route
          profile.ipv6 = oldProfile.ipv6
          profile.proxyApps = oldProfile.proxyApps
          profile.bypass = oldProfile.bypass
          profile.individual = oldProfile.individual
          profile.udpdns = oldProfile.udpdns
          profile.dns = oldProfile.dns
          profile.china_dns = oldProfile.china_dns
        case _ =>
      }
      val last = dbHelper.profileDao.queryRaw(dbHelper.profileDao.queryBuilder.selectRaw("MAX(userOrder)")
        .prepareStatementString).getFirstResult
      if (last != null && last.length == 1 && last(0) != null) profile.userOrder = last(0).toInt + 1

      val last_exist = dbHelper.profileDao.queryBuilder()
        .where().eq("name", profile.name)
        .and().eq("host", profile.host)
        .and().eq("remotePort", profile.remotePort)
        .and().eq("password", profile.password)
        .and().eq("protocol", profile.protocol)
        .and().eq("protocol_param", profile.protocol_param)
        .and().eq("obfs", profile.obfs)
        .and().eq("obfs_param", profile.obfs_param)
        .and().eq("url_group", profile.url_group)
        .and().eq("method", profile.method).queryForFirst().asInstanceOf[Profile]
      if (last_exist == null) {
        dbHelper.profileDao.createOrUpdate(profile)
        0
      } else {
        last_exist.id
      }
    } catch {
      case ex: Exception =>
        Log.e(TAG, "addProfile", ex)
        app.track(ex)
        0
    }
  }

  def updateProfile(profile: Profile): Boolean = {
    try {
      dbHelper.profileDao.update(profile)
      true
    } catch {
      case ex: Exception =>
        Log.e(TAG, "updateProfile", ex)
        app.track(ex)
        false
    }
  }

  def updateAllProfile_String(key:String, value:String): Boolean = {
    try {
      dbHelper.profileDao.executeRawNoArgs("UPDATE `profile` SET " + key + " = '" + value + "';")
      true
    } catch {
      case ex: Exception =>
        Log.e(TAG, "updateProfile", ex)
        app.track(ex)
        false
    }
  }

  def updateAllProfile_Boolean(key:String, value:Boolean): Boolean = {
    try {
      if (value) {
        dbHelper.profileDao.executeRawNoArgs("UPDATE `profile` SET " + key + " = '1';")
      } else {
        dbHelper.profileDao.executeRawNoArgs("UPDATE `profile` SET " + key + " = '0';")
      }
      true
    } catch {
      case ex: Exception =>
        Log.e(TAG, "updateProfile", ex)
        app.track(ex)
        false
    }
  }

  def getProfile(id: Int): Option[Profile] = {
    try {
      //从数据库里读取
      /*dbHelper.profileDao.queryForId(id) match {
        case profile: Profile => Option(profile)
        case _ => None
      }*/

      println("选择的序号是:"+id)
//      val node : Node = DataSaver.NODES.get(id)
//      print("选择的服务器是:"+node.getNodeName)
      var profile : Profile = null
      if (id != 0){
       profile = new Profile
         /*profile.name=node.getNodeName
        profile.host=node.getAddress
        profile.remotePort=Integer.parseInt(node.getPort)
        profile.password=node.getPassword
        profile.protocol=node.getProtocol
        profile.obfs=node.getPbfs
        profile.method=node.getMethod*/
        profile.name="香港"
        profile.host="47.89.29.181"
        profile.remotePort=2025
        profile.password="837b23"
        profile.protocol="origin"
        profile.obfs="plain"
        profile.method="rc4-md5"
      }
      Option(profile)
    } catch {
      case ex: Exception =>
        Log.e(TAG, "getProfile", ex)
        app.track(ex)
        None
    }
  }

  def delProfile(id: Int): Boolean = {
    try {
      dbHelper.profileDao.deleteById(id)
      true
    } catch {
      case ex: Exception =>
        Log.e(TAG, "delProfile", ex)
        app.track(ex)
        false
    }
  }

  def getFirstProfile = {
    try {
      val result = dbHelper.profileDao.query(dbHelper.profileDao.queryBuilder.limit(1L).prepare)
      if (result.size == 1) Option(result.get(0)) else None
    } catch {
      case ex: Exception =>
        Log.e(TAG, "getAllProfiles", ex)
        app.track(ex)
        None
    }
  }

  def getAllProfiles: Option[List[Profile]] = {
    try {
      import scala.collection.JavaConversions._
      Option(dbHelper.profileDao.query(dbHelper.profileDao.queryBuilder.orderBy("userOrder", true).prepare).toList)
    } catch {
      case ex: Exception =>
        Log.e(TAG, "getAllProfiles", ex)
        app.track(ex)
        None
    }
  }

  def getAllProfilesByGroup(group:String): Option[List[Profile]] = {
    try {
      import scala.collection.JavaConversions._
      Option(dbHelper.profileDao.query(dbHelper.profileDao.queryBuilder.where().eq("url_group", group).prepare).toList)
    } catch {
      case ex: Exception =>
        Log.e(TAG, "getAllProfiles", ex)
        app.track(ex)
        None
    }
  }

  def getAllProfilesByElapsed: Option[List[Profile]] = {
    try {
      import scala.collection.JavaConversions._
      Option(dbHelper.profileDao.query(dbHelper.profileDao.queryBuilder.orderBy("elapsed", true).where().not().eq("elapsed", 0).prepare).toList
        ++ dbHelper.profileDao.query(dbHelper.profileDao.queryBuilder.orderBy("elapsed", true).where().eq("elapsed", 0).prepare).toList)
    } catch {
      case ex: Exception =>
        Log.e(TAG, "getAllProfiles", ex)
        app.track(ex)
        None
    }
  }

  def createDefault(): Profile = {
    val profile = new Profile {
      name = "Android SSR Default"
      host = "137.74.141.42"
      remotePort = 80
      password = "androidssr"
      protocol = "auth_chain_a"
      obfs = "http_simple"
      method = "none"
      url_group = "FreeSSR-public"
    }
    createProfile(profile)
  }
}
