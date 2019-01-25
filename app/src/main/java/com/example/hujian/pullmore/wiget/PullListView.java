package com.example.hujian.pullmore.wiget;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.animation.DecelerateInterpolator;
import android.widget.AbsListView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.Scroller;

import com.example.hujian.pullmore.R;

public class PullListView extends ListView implements AbsListView.OnScrollListener{
    private float mLastY = -1; // save event y

    private OnScrollListener mScrollListener; // user's scroll listener
    private RelativeLayout mHeaderViewContent;
    private HeadView mHeaderView;
    private Scroller mScroller; // used for scroll back
    private ExpendPoint mExpendPoint;

    private final static int SCROLLBACK_HEADER = 0;
    private final static int SCROLL_DURATION = 200;
    private final static float OFFSET_RADIO = 1.8f;

    private int mHeaderViewHeight; // header view's height
    private int mScrollBack;


    private boolean mEnablePullRefresh = true;
    private boolean mPullRefreshing = false; // is refreashing.
    private boolean isForbinRefresh = false;


    public PullListView(Context context) {
        this(context, null);
    }

    public PullListView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public PullListView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initWithContext(context);
        this.setOnScrollListener(this);
    }


    private void initWithContext(Context context) {
        mScroller = new Scroller(context, new DecelerateInterpolator());
        mHeaderView = new HeadView(context);

        //绑定内容部分
        mHeaderViewContent = mHeaderView
                .findViewById(R.id.xlistview_header_content);

        addHeaderView(mHeaderView);

        //获取内容部分高度
        mHeaderView.getViewTreeObserver().addOnGlobalLayoutListener(
                new ViewTreeObserver.OnGlobalLayoutListener() {
                    @Override
                    public void onGlobalLayout() {
                        mHeaderViewHeight = mHeaderViewContent.getHeight();
                        getViewTreeObserver()
                                .removeGlobalOnLayoutListener(this);
                    }
                });
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        if (mLastY == -1) {
            mLastY = ev.getRawY();
        }
        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN:
                mLastY = ev.getRawY();
                break;
            case MotionEvent.ACTION_MOVE:
                final float deltaY = ev.getRawY() - mLastY;
                mLastY = ev.getRawY();
                if (getFirstVisiblePosition() == 0
                        && (mHeaderView.getVisiableHeight() > 0 || deltaY > 0)) {
                    updateHeaderHeight(deltaY / OFFSET_RADIO);
                }
                break;

            default:
                mLastY = -1;
                resetHeaderHeight();
                break;
        }
        return super.onTouchEvent(ev);
    }

    private void updateHeaderHeight(float delta) {
        if (!isForbinRefresh) {

            mHeaderView.setVisiableHeight((int) delta
                    + mHeaderView.getVisiableHeight());
            if (mEnablePullRefresh && !mPullRefreshing) { // 未处于刷新状态，更新箭头
                if (mHeaderView.getVisiableHeight() > mHeaderViewHeight) {

                } else {

                }
            }
            setSelection(0); // scroll to top each time
        }
    }

    /**
     * reset header view's height.
     */
    private void resetHeaderHeight() {
        int height = mHeaderView.getVisiableHeight();
        if (height == 0) // not visible.
            return;
        //正在刷新，或者头部没有完全显示，返回
        if (height <= mHeaderViewHeight/2) {
            mScroller.startScroll(0, height, 0, 0, SCROLL_DURATION);
        }
        int finalHeight = 0;
        //正在刷新，并且下拉头部完全显示
        if (height > mHeaderViewHeight/2||height>mHeaderViewHeight) {
            finalHeight = mHeaderViewHeight;
        }
        mScrollBack = SCROLLBACK_HEADER;
        //从当前位置，返回到头部被隐藏
        mScroller.startScroll(0, height, 0, finalHeight - height, SCROLL_DURATION);
        //重绘HeaderView
        invalidate();
    }

    @Override
    public void computeScroll() {
        if (mScroller.computeScrollOffset()) {
            if (mScrollBack != SCROLLBACK_HEADER)
                return;
            mHeaderView.setVisiableHeight(mScroller.getCurrY());//改变头部高度，实现回滚
            postInvalidate();
        }
        super.computeScroll();
    }

    @Override
    public void onScrollStateChanged(AbsListView view, int scrollState) {

    }

    @Override
    public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
       View firstVisibleItemView = this.getChildAt(1);
        //获取第一个item
//        if(mHeaderView.getVisiableHeight()<=0){
//            boolean eq = firstVisibleItemView == mHeaderView;
//            mHeaderView.onReset();
//         //   Log.d("ListView", "##### 滚动到顶部 #####" + eq);
//        }
//
//        if (mHeaderView.getVisiableHeight()>0){
//            if (mHeaderView.getVisiableHeight()>mHeaderViewContent.getLayoutParams().height){
//                mHeaderView.onArrivedListHeight();
//            }
//            if (mHeaderView.getVisiableHeight()>0){
//                mHeaderView.onPull(mHeaderViewContent.getTop());
//            }
//        }



        if (mHeaderView.getVisiableHeight()<=0) {
            boolean eq = firstVisibleItemView == mHeaderView;
            mHeaderView.onReset();
            Log.d("ListView", "##### 滚动到顶部 #####" + eq);
        }
        if (firstVisibleItem == 0 && firstVisibleItemView != null) {
            Log.d("ListView", "###" + firstVisibleItemView.getTop());

            if (firstVisibleItemView.getTop() >= 0) {
                mHeaderView.onPull(firstVisibleItemView.getTop());
            }
            if (firstVisibleItemView.getTop() >= mHeaderView.getListSize()) {
                mHeaderView.onArrivedListHeight();
            }
        }
    }
}