package com.vpn.mine

import android.content.{ComponentName, Context, Intent, ServiceConnection}
import android.os.IBinder
import com.vpn.mine.aidl.{IShadowsocksService, IShadowsocksServiceCallback}
import com.vpn.mine.utils.Action

/**
  * Created by coder on 17-7-13.
  */
trait ServiceBoundContext extends Context with IBinder.DeathRecipient{

  class MyServiceConnection extends ServiceConnection{
    override def onServiceDisconnected(componentName: ComponentName): Unit = ???

    override def onServiceConnected(componentName: ComponentName, iBinder: IBinder): Unit = ???
  }


  private var callback: IShadowsocksServiceCallback.Stub = _
  private var connection: MyServiceConnection = _
  private var callbackRegistered: Boolean = _

  // Variables
  var binder: IBinder = _
  var bgService: IShadowsocksService = _


  def attachService(callback: IShadowsocksServiceCallback.Stub = null): Unit = {
    this.callback = callback
    if (bgService == null){
      println("我就是测试一下看看能不能用")
      /*//默认执行的是VPN连接方式
      val s = classOf[MyVpnService]
      val intent = new Intent(this,s)
      intent.setAction(Action.SERVICE)

      connection = new MyServiceConnection()
      bindService(intent,connection,Context.BIND_AUTO_CREATE)*/


    }
  }

}
