package com.vpn.mine.database

import android.util.Log
import com.vpn.mine.MyApplication.app
/**
  * Created by coder on 17-7-13.
  */
object SSRSubManager {
  private final val TAG = "SSRSubManager"
}

class SSRSubManager(dbHelper: DBHelper) {
  import SSRSubManager._

  var ssrsubAddedListener: SSRSub => Any = _
  def setSSRSubAddedListener(listener: SSRSub => Any) = this.ssrsubAddedListener = listener

  def createSSRSub(p: SSRSub = null): SSRSub = {
    val ssrsub = if (p == null) new SSRSub else p
    ssrsub.id = 0
    try {
      dbHelper.ssrsubDao.createOrUpdate(ssrsub)

      if (ssrsubAddedListener != null) ssrsubAddedListener(ssrsub)
    } catch {
      case ex: Exception =>
        Log.e(TAG, "addSSRSub", ex)
        app.track(ex)
    }
    ssrsub
  }

  def updateSSRSub(ssrsub: SSRSub): Boolean = {
    try {
      dbHelper.ssrsubDao.update(ssrsub)
      true
    } catch {
      case ex: Exception =>
        Log.e(TAG, "updateSSRSub", ex)
        app.track(ex)
        false
    }
  }

  def getSSRSub(id: Int): Option[SSRSub] = {
    try {
      dbHelper.ssrsubDao.queryForId(id) match {
        case ssrsub: SSRSub => Option(ssrsub)
        case _ => None
      }
    } catch {
      case ex: Exception =>
        Log.e(TAG, "getSSRSub", ex)
        app.track(ex)
        None
    }
  }

  def delSSRSub(id: Int): Boolean = {
    try {
      dbHelper.ssrsubDao.deleteById(id)
      true
    } catch {
      case ex: Exception =>
        Log.e(TAG, "delSSRSub", ex)
        app.track(ex)
        false
    }
  }

  def getFirstSSRSub = {
    try {
      val result = dbHelper.ssrsubDao.query(dbHelper.ssrsubDao.queryBuilder.limit(1L).prepare)
      if (result.size == 1) Option(result.get(0)) else None
    } catch {
      case ex: Exception =>
        Log.e(TAG, "getAllSSRSubs", ex)
        app.track(ex)
        None
    }
  }

  def getAllSSRSubs: Option[List[SSRSub]] = {
    try {
      import scala.collection.JavaConversions._
      Option(dbHelper.ssrsubDao.query(dbHelper.ssrsubDao.queryBuilder.prepare).toList)
    } catch {
      case ex: Exception =>
        Log.e(TAG, "getAllSSRSubs", ex)
        app.track(ex)
        None
    }
  }

  def createDefault(): SSRSub = {
    val ssrsub = new SSRSub {
      url = "https://raw.githubusercontent.com/breakwa11/breakwa11.github.io/master/free/freenodeplain.txt"
      url_group = "FreeSSR-public"
    }
    createSSRSub(ssrsub)
  }
}

