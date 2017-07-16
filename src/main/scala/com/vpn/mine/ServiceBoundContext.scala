package com.vpn.mine

import android.content.{ComponentName, Context, Intent, ServiceConnection}
import android.os.{IBinder, RemoteException}
import com.vpn.mine.aidl.{IShadowsocksService, IShadowsocksServiceCallback}
import com.vpn.mine.utils.Action

/**
  * Created by coder on 17-7-13.
  */
trait ServiceBoundContext extends Context with IBinder.DeathRecipient {

  class MyServiceConnection extends ServiceConnection {

    val Tag = "ServiceBoundContext"

    override def onServiceConnected(name: ComponentName, service: IBinder): Unit = {
      println(Tag + ":进入了onServiceConnected")
      binder = service
      service.linkToDeath(ServiceBoundContext.this, 0)
      bgService = IShadowsocksService.Stub.asInterface(service)
      registerCallback
      ServiceBoundContext.this.onServiceConnected()
    }

    override def onServiceDisconnected(name: ComponentName): Unit = {
      unregisterCallback
      ServiceBoundContext.this.onServiceDisconnected()
      bgService = null
      binder = null
    }

  }


  protected def registerCallback = if (bgService != null && callback != null && !callbackRegistered) try {
    bgService.registerCallback(callback)
    callbackRegistered = true
  } catch {
    case ignored: RemoteException => // Nothing
  }

  protected def unregisterCallback = {
    if (bgService != null && callback != null && callbackRegistered) try bgService.unregisterCallback(callback) catch {
      case ignored: RemoteException =>
    }
    callbackRegistered = false
  }

  def onServiceConnected() = ()
  def onServiceDisconnected() = ()
  override def binderDied = ()


  private var callback: IShadowsocksServiceCallback.Stub = _
  private var connection: MyServiceConnection = _
  private var callbackRegistered: Boolean = _

  // Variables
  var binder: IBinder = _
  var bgService: IShadowsocksService = _


  def attachService(callback: IShadowsocksServiceCallback.Stub = null): Unit = {
    this.callback = callback
    if (bgService == null) {
      //默认执行的是VPN连接方式
      val s = classOf[MyVpnService]
      val intent = new Intent(this, s)
      intent.setAction(Action.SERVICE)

      connection = new MyServiceConnection()
      val b = bindService(intent, connection, Context.BIND_AUTO_CREATE)
      println("绑定结果为:" + b)
    }
  }

  def detachService() {
    unregisterCallback
    callback = null
    if (connection != null) {
      try unbindService(connection) catch {
        case _: IllegalArgumentException => // ignore
      }
      connection = null
    }
    if (binder != null) {
      binder.unlinkToDeath(this, 0)
      binder = null
    }
    bgService = null
  }
}
