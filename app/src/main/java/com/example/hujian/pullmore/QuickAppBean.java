package com.example.hujian.pullmore;

/**
 * <p>文件描述：<p>
 * <p>作者：hujian<p>
 * <p>创建时间：2018/10/18/018<p>
 * <p>更改时间：2018/10/18/018<p>
 * <p>版本号：1<p>
 */
public class QuickAppBean {
    public String Appname;
    public int Appicon;

    public String getAppname() {
        return Appname;
    }

    public void setAppname(String appname) {
        Appname = appname;
    }

    public QuickAppBean(String appname, int appicon) {
        Appname = appname;
        Appicon = appicon;
    }
}
