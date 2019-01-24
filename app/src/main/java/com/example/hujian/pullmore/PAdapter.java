package com.example.hujian.pullmore;

import android.support.annotation.NonNull;
import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;

import java.util.List;

/**
 * <p>文件描述：<p>
 * <p>作者：hujian<p>
 * <p>创建时间：2018/10/18/018<p>
 * <p>更改时间：2018/10/18/018<p>
 * <p>版本号：1<p>
 */
public class PAdapter extends PagerAdapter {
    private List<GridView> views;
    public PAdapter(List<GridView> views) {
        this.views = views;
    }
    @Override
    public int getCount() {
        return views.size();
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object o) {
        return view==o;
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        View v=views.get(position);
        container.addView(v);
        return v;
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        container.removeView(views.get(position));
    }
}
