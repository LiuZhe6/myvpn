package com.vpn.mine.utils

/**
  * Created by coder on 17-7-13.
  */

object Executable {
  val REDSOCKS = "redsocks"
  val PDNSD = "pdnsd"
  val SS_LOCAL = "ss-local"
  val SS_TUNNEL = "ss-tunnel"
  val TUN2SOCKS = "tun2socks"
  val KCPTUN = "kcptun"
}

object ConfigUtils {

  def EscapedJson(OriginString:String):String = {
    val ProcessString = OriginString.replaceAll("\\\\","\\\\\\\\").replaceAll("\"","\\\\\"")

    ProcessString
  }

  val SHADOWSOCKS = "{\"server\": \"%s\", \"server_port\": %d, \"local_port\": %d, \"password\": \"%s\", \"method\":\"%s\", \"timeout\": %d, \"protocol\": \"%s\", \"obfs\": \"%s\", \"obfs_param\": \"%s\", \"protocol_param\": \"%s\"}"
  val REDSOCKS = "base {\n" +
    " log_debug = off;\n" +
    " log_info = off;\n" +
    " log = stderr;\n" +
    " daemon = off;\n" +
    " redirector = iptables;\n" +
    "}\n" +
    "redsocks {\n" +
    " local_ip = 127.0.0.1;\n" +
    " local_port = 8123;\n" +
    " ip = 127.0.0.1;\n" +
    " port = %d;\n" +
    " type = socks5;\n" +
    "}\n"

  val PROXYCHAINS = "strict_chain\n" +
    "localnet 127.0.0.0/255.0.0.0\n" +
    "[ProxyList]\n" +
    "%s %s %s %s %s"

  val PDNSD_LOCAL =
    """
      |global {
      | perm_cache = 2048;
      | %s
      | cache_dir = "%s";
      | server_ip = %s;
      | server_port = %d;
      | query_method = tcp_only;
      | min_ttl = 15m;
      | max_ttl = 1w;
      | timeout = 10;
      | daemon = off;
      |}
      |
      |server {
      | label = "local";
      | ip = 127.0.0.1;
      | port = %d;
      | reject = %s;
      | reject_policy = negate;
      | reject_recursively = on;
      |}
      |
      |rr {
      | name=localhost;
      | reverse=on;
      | a=127.0.0.1;
      | owner=localhost;
      | soa=localhost,root.localhost,42,86400,900,86400,86400;
      |}
    """.stripMargin

  val PDNSD_DIRECT =
    """
      |global {
      | perm_cache = 2048;
      | %s
      | cache_dir = "%s";
      | server_ip = %s;
      | server_port = %d;
      | query_method = udp_only;
      | min_ttl = 15m;
      | max_ttl = 1w;
      | timeout = 10;
      | daemon = off;
      | par_queries = 4;
      |}
      |
      |%s
      |
      |server {
      | label = "local-server";
      | ip = 127.0.0.1;
      | query_method = tcp_only;
      | port = %d;
      | reject = %s;
      | reject_policy = negate;
      | reject_recursively = on;
      |}
      |
      |rr {
      | name=localhost;
      | reverse=on;
      | a=127.0.0.1;
      | owner=localhost;
      | soa=localhost,root.localhost,42,86400,900,86400,86400;
      |}
    """.stripMargin

  val REMOTE_SERVER =
    """
      |server {
      | label = "remote-servers";
      | ip = %s;
      | port = %d;
      | timeout = 3;
      | query_method = udp_only;
      | %s
      | policy = included;
      | reject = %s;
      | reject_policy = fail;
      | reject_recursively = on;
      |}
    """.stripMargin
}

object Key {
  val id = "profileId"
  val name = "profileName"

  val individual = "Proxyed"

  val isNAT = "isNAT"
  val route = "route"
  val aclurl = "aclurl"

  val isAutoConnect = "isAutoConnect"

  val proxyApps = "isProxyApps"
  val bypass = "isBypassApps"
  val udpdns = "isUdpDns"
  val auth = "isAuth"
  val ipv6 = "isIpv6"

  val host = "proxy"
  val password = "sitekey"
  val method = "encMethod"
  val remotePort = "remotePortNum"
  val localPort = "localPortNum"

  val profileTip = "profileTip"

  val obfs = "obfs"
  val obfs_param = "obfs_param"
  val protocol = "protocol"
  val protocol_param = "protocol_param"
  val dns = "dns"
  val china_dns = "china_dns"

  val tfo = "tcp_fastopen"
  val currentVersionCode = "currentVersionCode"
  val logcat = "logcat"
  val frontproxy = "frontproxy"
  val ssrsub_autoupdate = "ssrsub_autoupdate"
  val group_name = "groupName"
}

object State {
  val CONNECTING = 1
  val CONNECTED = 2
  val STOPPING = 3
  val STOPPED = 4
  def isAvailable(state: Int): Boolean = state != CONNECTED && state != CONNECTING
}

object Action {
  val SERVICE = "com.vpn.mine.SERVICE"
  val CLOSE = "com.vpn.mine.CLOSE"
  val QUICK_SWITCH = "com.vpn.mine.QUICK_SWITCH"
  val SCAN = "com.vpn.mine.intent.action.SCAN"
  val SORT = "com.vpn.mine.intent.action.SORT"
}

object Route {
  val ALL = "all"
  val BYPASS_LAN = "bypass-lan"
  val BYPASS_CHN = "bypass-china"
  val BYPASS_LAN_CHN = "bypass-lan-china"
  val GFWLIST = "gfwlist"
  val CHINALIST = "china-list"
  val ACL = "self"
}