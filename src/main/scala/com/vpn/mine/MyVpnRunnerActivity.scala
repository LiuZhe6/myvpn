package com.vpn.mine

import android.app.{Activity, KeyguardManager}
import android.content.{BroadcastReceiver, Context, Intent, IntentFilter}
import android.net.VpnService
import android.os.{Bundle, Handler}
import android.util.Log
import com.vpn.mine.MyApplication.app
/**
  * Created by coder on 17-7-14.
  */
object MyVpnRunnerActivity {
  private final val TAG = "ShadowsocksRunnerActivity"
  private final val REQUEST_CONNECT = 1
}

class MyVpnRunnerActivity extends Activity with ServiceBoundContext {
  import MyVpnRunnerActivity._

  val handler = new Handler()

  // Variables
  var receiver: BroadcastReceiver = _

  override def onServiceConnected() {
    handler.postDelayed(() => if (bgService != null) startBackgroundService(), 1000)
  }

  def startBackgroundService() {
    if (app.isNatEnabled) {
      bgService.use(app.profileId)
      finish()
    } else {
      val intent = VpnService.prepare(MyVpnRunnerActivity.this)
      if (intent != null) {
        startActivityForResult(intent, REQUEST_CONNECT)
      } else {
        onActivityResult(REQUEST_CONNECT, Activity.RESULT_OK, null)
      }
    }
  }

  override def onCreate(savedInstanceState: Bundle) {
    super.onCreate(savedInstanceState)
    val km = getSystemService(Context.KEYGUARD_SERVICE).asInstanceOf[KeyguardManager]
    val locked = km.inKeyguardRestrictedInputMode
    if (locked) {
      val filter = new IntentFilter(Intent.ACTION_USER_PRESENT)
      receiver = (context: Context, intent: Intent) => {
        if (intent.getAction == Intent.ACTION_USER_PRESENT) {
          attachService()
        }
      }
      registerReceiver(receiver, filter)
    } else {
      attachService()
    }
    finish
  }

  override def onDestroy() {
    super.onDestroy()
    detachService()
    if (receiver != null) {
      unregisterReceiver(receiver)
      receiver = null
    }
  }

  override def onActivityResult(requestCode: Int, resultCode: Int, data: Intent) {
    resultCode match {
      case Activity.RESULT_OK =>
        if (bgService != null) {
          bgService.use(app.profileId)
        }
      case _ =>
        Log.e(TAG, "Failed to start VpnService")
    }
    finish()
  }
}

