package com.vpn.mine.activity

import java.util
import java.util.ArrayList

import android.app.Activity
import android.net.VpnService
import android.os.{Bundle, Handler}
import android.support.design.widget.TabLayout
import android.support.v4.app.{Fragment, FragmentManager}
import android.support.v4.view.{PagerAdapter, ViewPager}
import android.support.v7.app.AppCompatActivity
import android.view.{View, ViewGroup}
import com.vpn.mine.aidl.IShadowsocksServiceCallback
import com.vpn.mine.job.SSRSubUpdateJob
import com.vpn.mine.utils.{DataSaver, State}
import com.vpn.mine.{R, ServiceBoundContext}
import com.vpn.mine.MyApplication.app
/**
  * Created by coder on 17-7-14.
  */
class MainActivity extends AppCompatActivity with TabLayout.OnTabSelectedListener with ServiceBoundContext{


  //    四个布局
  private val connectView: View = null
  private val purchaseView: View = null
  private val helpView: View = null
  private val optionView: View = null

  private var viewPager: ViewPager = null
  private var fragmentList: util.ArrayList[Fragment] = null

  //  四个标签 和 TabLayout
  private var tabLayout: TabLayout = null
  private var connectTab: TabLayout.Tab = null
  private var purchaseTab: TabLayout.Tab = null
  private var helpTab: TabLayout.Tab = null
  private var optionTab: TabLayout.Tab = null

  private var connectFragment: ConnectFragment = null
  private var purchaseFragment: PurchaseFragment = null
  private var helpFragment: HelpFragment = null
  private var optionFragment: OptionFragment = null

  private val handler = new Handler

  private val callBack : IShadowsocksServiceCallback.Stub = new IShadowsocksServiceCallback.Stub {

    override def stateChanged(s: Int, profileName: String, m: String): Unit = {
      handler.post(() => {
        s match {
          case State.CONNECTING =>
            //正在连接
            println("正在连接")
          case State.CONNECTED =>
            //已连接
            println("已连接")
          case State.STOPPED =>
            //已停止
            println("已停止")
          case State.STOPPING =>
            //正在停止
            println("正在停止")
        }
      })
    }

    override def trafficUpdated(txRate: Long, rxRate: Long, txTotal: Long, rxTotal: Long): Unit = Unit

  }


  override def onCreate(savedInstanceState: Bundle): Unit = {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.index)

    //创建控件和点击事件
    initView()
    initEvent()


    println("执行有关数据库的操作")
    //和数据库有关的操作
    app.ssrsubManager.getFirstSSRSub match {
      case Some(first) => {

      }
      case None => app.ssrsubManager.createDefault()
    }

    println("接下来执行schedule")
    SSRSubUpdateJob.schedule()

    println("调用attachService")
    //调用attachService
    handler.post(() => attachService(callBack)
    )
  }
  protected override def onResume() {
    super.onResume()

    app.refreshContainerHolder

    //待添加更新当前状态
//    updateState(updateCurrentProfile())
  }

  override def onStart() {
    super.onStart()
    registerCallback
  }

  override def onDestroy() {
    super.onDestroy()
    detachService()
    //待添加
    /*if (receiver != null) {
      unregisterReceiver(receiver)
      receiver = null
    }*/
  }



  def attachService: Unit = attachService(callBack)

  def initView(): Unit ={
    connectFragment = new ConnectFragment
    purchaseFragment = new PurchaseFragment
    helpFragment = new HelpFragment
    optionFragment = new OptionFragment
    tabLayout = findViewById(R.id.tabLayout).asInstanceOf[TabLayout]
    viewPager = findViewById(R.id.viewpager).asInstanceOf[ViewPager]

    fragmentList = new util.ArrayList[Fragment]()
    fragmentList.add(connectFragment)
    fragmentList.add(purchaseFragment)
    fragmentList.add(helpFragment)
    fragmentList.add(optionFragment)

    val fragmentManager : FragmentManager = this.getSupportFragmentManager
    viewPager.setAdapter(new MainFragmentPagerAdapter(fragmentManager, fragmentList))
    //默认显示连接页面
    viewPager.setCurrentItem(0)

    //设置tabLayout与viewPager级联
    tabLayout.setupWithViewPager(viewPager)
    //实例化Tab
    connectTab = tabLayout.getTabAt(0)
    purchaseTab = tabLayout.getTabAt(1)
    helpTab = tabLayout.getTabAt(2)
    optionTab = tabLayout.getTabAt(3)

    //重新设置标签名称，上面会清除全部标签
    connectTab.setText("连接")
    purchaseTab.setText("购买")
    helpTab.setText("帮助")
    optionTab.setText("选项")

  }


  def initEvent(): Unit ={
    tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener {

      override def onTabSelected(tab: TabLayout.Tab): Unit = {
        if (tab eq tabLayout.getTabAt(0)) viewPager.setCurrentItem(0)
        else if (tab eq tabLayout.getTabAt(1)) viewPager.setCurrentItem(1)
        else if (tab eq tabLayout.getTabAt(2)) viewPager.setCurrentItem(2)
        else if (tab eq tabLayout.getTabAt(3)) viewPager.setCurrentItem(3)
      }

      override def onTabUnselected(tab: TabLayout.Tab): Unit = Unit

      override def onTabReselected(tab: TabLayout.Tab): Unit = Unit
    })
  }

  override def onTabSelected(tab: TabLayout.Tab): Unit = Unit

  override def onTabReselected(tab: TabLayout.Tab): Unit = Unit

  override def onTabUnselected(tab: TabLayout.Tab): Unit = Unit

  class NewPagerAdapter(views : ArrayList[View]) extends PagerAdapter{

    var viewList :ArrayList[View] = _

    this.viewList = views

    override def getCount: Int = viewList.size

    override def isViewFromObject(view: View, o: scala.Any): Boolean = view == o

    override def destroyItem(container: ViewGroup, position: Int, `object`: scala.Any): Unit = {
      container.removeView(viewList.get(position))
    }

    override def getItemPosition(`object`: scala.Any): Int = {
      super.getItemPosition(`object`)
    }

    override def instantiateItem(container: ViewGroup, position: Int): AnyRef = {
      container.addView(viewList.get(position))
      viewList.get(position)
    }
  }

  def prepareStartService(): Unit ={
//    println("进来了")
    //连接VPn
    val vpnIntent = VpnService.prepare(this)
    //如果当前系统中没有VPN连接，或者存在的VPN连接不是本程序建立的
    //则VpnService.prepare函数会返回一个intent。这个intent就是用来触发确认对话框的
    if (vpnIntent != null) startActivityForResult(vpnIntent, 0)
    else { //如果当前系统中有VPN连接，并且这个连接就是本程序建立的，则函数会返回null，就不需要用户再确认了
      onActivityResult(0, Activity.RESULT_OK, null)
    }
  }

  def serviceLoad(): Unit ={
    //更新profileId的值
    app.profileId(DataSaver.NODE_INDEX)
//    println(bgService != null)
    bgService.use(app.profileId)
    println("use方法调用结束")
    //待添加改变连接的状态显示

  }
}
