package com.vpn.mine.database

import android.content.Context
import android.content.pm.ApplicationInfo
import android.database.sqlite.SQLiteDatabase
import com.j256.ormlite.android.apptools.OrmLiteSqliteOpenHelper
import com.j256.ormlite.dao.Dao
import com.j256.ormlite.support.ConnectionSource
import com.j256.ormlite.table.TableUtils

import scala.collection.mutable

/**
  * Created by coder on 17-7-13.
  */
object DBHelper {
  final val PROFILE = "profile.db"
  private var apps: mutable.Buffer[ApplicationInfo] = _

  def isAllDigits(x: String) = !x.isEmpty && (x forall Character.isDigit)

  def updateProxiedApps(context: Context, old: String) = {
    /*synchronized(if (apps == null) apps = context.getPackageManager.getInstalledApplications(0).asScala)
    val uidSet = old.split('|').filter(isAllDigits).map(_.toInt).toSet
    apps.filter(ai => uidSet.contains(ai.uid)).map(_.packageName).mkString("\n")*/
  }
}

class DBHelper(val context: Context)
  extends OrmLiteSqliteOpenHelper(context, DBHelper.PROFILE, null, 24) {
  import DBHelper._

  lazy val profileDao: Dao[Profile, Int] = getDao(classOf[Profile])
  lazy val ssrsubDao: Dao[SSRSub, Int] = getDao(classOf[SSRSub])

  def onCreate(database: SQLiteDatabase, connectionSource: ConnectionSource) {
    TableUtils.createTable(connectionSource, classOf[Profile])
    TableUtils.createTable(connectionSource, classOf[SSRSub])
  }

  /*def onUpgrade(database: SQLiteDatabase, connectionSource: ConnectionSource, oldVersion: Int,
                newVersion: Int) {
    if (oldVersion != newVersion) {
      if (oldVersion < 7) {
        profileDao.executeRawNoArgs("DROP TABLE IF EXISTS 'profile';")
        onCreate(database, connectionSource)
        return
      }

      try {
        if (oldVersion < 8) {
          profileDao.executeRawNoArgs("ALTER TABLE `profile` ADD COLUMN udpdns SMALLINT;")
        }
        if (oldVersion < 9) {
          profileDao.executeRawNoArgs("ALTER TABLE `profile` ADD COLUMN route VARCHAR DEFAULT 'all';")
        } else if (oldVersion < 19) {
          profileDao.executeRawNoArgs("UPDATE `profile` SET route = 'all' WHERE route IS NULL;")
        }
        if (oldVersion < 10) {
          profileDao.executeRawNoArgs("ALTER TABLE `profile` ADD COLUMN auth SMALLINT;")
        }
        if (oldVersion < 11) {
          profileDao.executeRawNoArgs("ALTER TABLE `profile` ADD COLUMN ipv6 SMALLINT;")
        }
        if (oldVersion < 12) {
          profileDao.executeRawNoArgs("BEGIN TRANSACTION;")
          profileDao.executeRawNoArgs("ALTER TABLE `profile` RENAME TO `tmp`;")
          TableUtils.createTable(connectionSource, classOf[Profile])
          profileDao.executeRawNoArgs(
            "INSERT INTO `profile`(id, name, host, localPort, remotePort, password, method, route, proxyApps, bypass," +
              " udpdns, auth, ipv6, individual) " +
              "SELECT id, name, host, localPort, remotePort, password, method, route, 1 - global, bypass, udpdns, auth," +
              " ipv6, individual FROM `tmp`;")
          profileDao.executeRawNoArgs("DROP TABLE `tmp`;")
          profileDao.executeRawNoArgs("COMMIT;")
        } else if (oldVersion < 13) {
          profileDao.executeRawNoArgs("ALTER TABLE `profile` ADD COLUMN tx LONG;")
          profileDao.executeRawNoArgs("ALTER TABLE `profile` ADD COLUMN rx LONG;")
          profileDao.executeRawNoArgs("ALTER TABLE `profile` ADD COLUMN date VARCHAR;")
        }

        if (oldVersion < 15) {
          if (oldVersion >= 12) profileDao.executeRawNoArgs("ALTER TABLE `profile` ADD COLUMN userOrder LONG;")
          var i = 0
          for (profile <- profileDao.queryForAll.asScala) {
            if (oldVersion < 14) profile.individual = updateProxiedApps(context, profile.individual)
            profile.userOrder = i
            profileDao.update(profile)
            i += 1
          }
        }


        if (oldVersion < 16) {
          profileDao.executeRawNoArgs("UPDATE `profile` SET route = 'bypass-lan-china' WHERE route = 'bypass-china'")
        }

        if (oldVersion < 19) {
          profileDao.executeRawNoArgs("ALTER TABLE `profile` ADD COLUMN dns VARCHAR DEFAULT '8.8.8.8:53';")
        }

        if (oldVersion < 20) {
          profileDao.executeRawNoArgs("ALTER TABLE `profile` ADD COLUMN china_dns VARCHAR DEFAULT '114.114.114.114:53,223.5.5.5:53';")
        }

        if (oldVersion < 21) {
          profileDao.executeRawNoArgs("ALTER TABLE `profile` ADD COLUMN protocol_param VARCHAR DEFAULT '';")
        }

        if (oldVersion < 22) {
          profileDao.executeRawNoArgs("ALTER TABLE `profile` ADD COLUMN elapsed LONG DEFAULT 0;")
        }

        if (oldVersion < 23) {
          profileDao.executeRawNoArgs("ALTER TABLE `profile` ADD COLUMN url_group VARCHAR DEFAULT '';")
        }

        if (oldVersion < 24) {
          TableUtils.createTable(connectionSource, classOf[SSRSub])
        }
      } catch {
        case ex: Exception =>
          app.track(ex)
          profileDao.executeRawNoArgs("DROP TABLE IF EXISTS 'profile';")
          onCreate(database, connectionSource)
          return
      }
    }
  }*/
  override def onUpgrade(database: SQLiteDatabase, connectionSource: ConnectionSource, oldVersion: Int, newVersion: Int): Unit = ???
}

