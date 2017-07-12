package com.vpn.mine.entity;

import android.graphics.drawable.Drawable;

/**
 * Created by coder on 17-7-4.
 */
public class AppInfo {

    private Drawable image;
    private String appName;

    //设置状态  默认为选中状态
    private boolean select = true;

    public AppInfo(Drawable image, String appName) {
        this.image = image;
        this.appName = appName;

        //默认为选中状态
        this.select = true;
    }
    public AppInfo() {

    }

    public Drawable getImage() {
        return image;
    }

    public void setImage(Drawable image) {
        this.image = image;
    }

    public String getAppName() {
        return appName;
    }

    public void setAppName(String appName) {
        this.appName = appName;
    }

    public boolean isSelect() {
        return select;
    }

    public void setSelect(boolean select) {
        this.select = select;
    }
}
