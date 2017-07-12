package com.vpn.mine.utils;

import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.util.Log;
import com.vpn.mine.entity.AppInfo;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by coder on 17-7-4.
 * 扫描本地安装的应用,工具类
 */
public class ApkTool {
    static String TAG = "ApkTool";
    public static List<AppInfo> mLocalInstallApps = null;

    public static List<AppInfo> scanLocalInstallAppList(PackageManager packageManager) {
        List<AppInfo> appInfoList = new ArrayList<AppInfo>();
        try {
            List<PackageInfo> packageInfos = packageManager.getInstalledPackages(0);
            for (int i = 0; i < packageInfos.size(); i++) {
                PackageInfo packageInfo = packageInfos.get(i);
                //过滤掉系统app
                if ((ApplicationInfo.FLAG_SYSTEM & packageInfo.applicationInfo.flags) != 0) {
                    continue;
                }
                AppInfo app = new AppInfo();
                app.setAppName(packageManager.getApplicationLabel(packageInfo.applicationInfo).toString());
                if (packageInfo.applicationInfo.loadIcon(packageManager) == null) {
                    continue;
                }
                app.setImage(packageInfo.applicationInfo.loadIcon(packageManager));
                appInfoList.add(app);
            }
        } catch (Exception e) {
            Log.e(TAG, "===============获取应用包信息失败");
        }
        return appInfoList;
    }
}
