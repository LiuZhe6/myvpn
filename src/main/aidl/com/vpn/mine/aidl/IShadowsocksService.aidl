package com.vpn.mine.aidl;

import com.vpn.mine.aidl.IShadowsocksServiceCallback;

interface IShadowsocksService {
  int getState();
  String getProfileName();

  oneway void registerCallback(IShadowsocksServiceCallback cb);
  oneway void unregisterCallback(IShadowsocksServiceCallback cb);

  oneway void use(in int profileId);
  void useSync(in int profileId);
}