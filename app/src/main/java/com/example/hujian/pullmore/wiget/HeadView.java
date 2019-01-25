package com.example.hujian.pullmore.wiget;

import android.content.Context;
import android.database.Cursor;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.example.hujian.pullmore.GridviewAdapter;
import com.example.hujian.pullmore.PAdapter;
import com.example.hujian.pullmore.QuickAppBean;
import com.example.hujian.pullmore.R;
import com.example.hujian.pullmore.StudentDao;

import java.util.ArrayList;
import java.util.List;

public class HeadView extends LinearLayout {
    private LinearLayout mContainer;// 下拉布局主体
    private ExpendPoint mExpendPoint;

    float containerHeight = UIHelper.dip2px(60);
    float listHeight = UIHelper.dip2px(120);

    boolean arrivedListHeight = false;

    private GridView gridView;
    private ViewPager viewPager;
    private Cursor cursor;
    private GridviewAdapter gridviewAdapter;
    private List<QuickAppBean> beans;
    private LayoutParams lp;

    public HeadView(Context context) {
        this(context, null);
    }

    public HeadView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public HeadView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                LayoutParams.FILL_PARENT, 0);

        //绑定头部布局
        mContainer = (LinearLayout) LayoutInflater.from(context).inflate(
                R.layout.layout, null);
        addView(mContainer, lp);
        setGravity(Gravity.CENTER);
        //控件绑定
        viewPager = mContainer.findViewById(R.id.viewpager);
        mExpendPoint=mContainer.findViewById(R.id.expend_point);
        initdata();
        initView();
    }

    private void initdata() {
        beans = new ArrayList<>();
        beans = getStu();
    }

    public void onReset() {
        mExpendPoint.setVisibility(VISIBLE);
        mExpendPoint.setAlpha(1);
        mExpendPoint.setTranslationY(0);
        arrivedListHeight = false;
    }
    public void onArrivedListHeight() {
        arrivedListHeight = true;
    }
    public int getListSize() {
        return (int) (listHeight);
    }


    public void onPull(int offset) {
        if (!arrivedListHeight) {
            mExpendPoint.setVisibility(VISIBLE);
            float percent = Math.abs(offset) / containerHeight;
            int moreOffset = Math.abs(offset) - (int) containerHeight;
            if (percent <= 1.0f) {
                mExpendPoint.setPercent(percent);
                mExpendPoint.setTranslationY(-Math.abs(offset) / 2 + mExpendPoint.getHeight() / 2);
                viewPager.setTranslationY(-containerHeight);
            } else {
                float subPercent = (moreOffset) / (listHeight - containerHeight);
                subPercent = Math.min(1.0f, subPercent);
                mExpendPoint.setTranslationY(-(int) containerHeight / 2 + mExpendPoint.getHeight
                        () / 2 + (int) containerHeight * subPercent / 2);
                mExpendPoint.setPercent(1.0f);
                float alpha = (1 - subPercent * 2);
                mExpendPoint.setAlpha(Math.max(alpha, 1));
                viewPager.setTranslationY(-(1 - subPercent) * containerHeight);
            }
        }
        if (Math.abs(offset) >= listHeight) {
            mExpendPoint.setVisibility(INVISIBLE);
            viewPager.setTranslationY(-(Math.abs(offset) - listHeight) / 2);
        }
    }

    public void setVisiableHeight(int height) {
        if (height < 0)
            height = 0;
        lp = (LayoutParams) mContainer
                .getLayoutParams();
        lp.height = height;
        mContainer.setLayoutParams(lp);
    }

    public int getVisiableHeight() {
        return mContainer.getLayoutParams().height;
    }

    /**
     * 创建gridview集
     */
    private void initView() {
        int PagerCount;
        int ItemCount = cursor.getCount();

        List<GridView> mgGridViews = new ArrayList<>();
        //viewpager的页数向上取整
        PagerCount = (int) Math.ceil((float) ItemCount / 4);
        Log.e("+++++PagerCount+++++", "ItemCount:" + ItemCount + "up:" + PagerCount + "");

        for (int i = 0; i < PagerCount; i++) {
            gridView = new GridView(getContext());
            gridView.setAdapter(new GridviewAdapter(beans, getContext(), i * 4));
            gridView.setNumColumns(4);
            mgGridViews.add(gridView);
        }
        viewPager = mContainer.findViewById(R.id.viewpager);
        viewPager.setAdapter(new PAdapter(mgGridViews));
    }


    /**
     * 获取快应用转化为QuickAppBean集合
     *
     * @return
     */
    public List<QuickAppBean> getStu() {
        List<QuickAppBean> beans = new ArrayList<>();
        StudentDao studentDao = new StudentDao(getContext());
        cursor = (Cursor) studentDao.getStudent();
        cursor.moveToFirst();
        for (int i = 0; i < cursor.getCount(); i++) {
            QuickAppBean bean;
            bean = new QuickAppBean(cursor.getString(cursor.getColumnIndex("name")), 1);
            beans.add(bean);
            Log.e("QuickAppBean", "getStu: " + bean.Appname);
            cursor.moveToNext();
        }
        return beans;
    }


}
