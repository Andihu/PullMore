package com.example.hujian.pullmore.wiget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.ViewTreeObserver;
import android.view.animation.DecelerateInterpolator;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.Scroller;
import android.widget.TextView;

import com.example.hujian.pullmore.R;

public class PullListView extends ListView{
    private float mLastY = -1; // save event y
    private Scroller mScroller; // used for scroll back
    private OnScrollListener mScrollListener; // user's scroll listener
    private int mScrollBack;
    private HeadView mHeaderView;
    private boolean isForbinRefresh = false;
    private final static int SCROLLBACK_HEADER = 0;
    private LinearLayout mHeaderViewContent;
    private TextView mHeaderTimeView;

    private final static int SCROLL_DURATION = 400;
    private int mHeaderViewHeight; // header view's height
    private boolean mEnablePullRefresh = true;
    private boolean mPullRefreshing = false; // is refreashing.

    private final static float OFFSET_RADIO = 1.8f;

    public PullListView(Context context) {
        this(context,null);
    }

    public PullListView(Context context, AttributeSet attrs) {
        this(context, attrs,0);
    }

    public PullListView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initWithContext(context);
    }
    private void initWithContext(Context context) {
        mScroller = new Scroller(context, new DecelerateInterpolator());


        mHeaderView = new HeadView(context);
        mHeaderViewContent = (LinearLayout) mHeaderView
                .findViewById(R.id.xlistview_header_content);
        addHeaderView(mHeaderView);

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
                mLastY = -1; // reset
//                if (getFirstVisiblePosition() == 0) {
//
//                    if (mEnablePullRefresh
//                            && mHeaderView.getVisiableHeight() > mHeaderViewHeight) {
//                        mPullRefreshing = true;
//                        mHeaderView.setState(XListViewHeader.STATE_REFRESHING);
//                    }
//
//                } else if (getLastVisiblePosition() == mTotalItemCount - 1) {
//
//                }
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
        if (height<=mHeaderViewHeight){
            mScroller.startScroll(0, height, 0, 0, SCROLL_DURATION);
        }
        int finalHeight = 0;
        //正在刷新，并且下拉头部完全显示
        if ( height > mHeaderViewHeight) {
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
            if (mScrollBack == SCROLLBACK_HEADER) {
                mHeaderView.setVisiableHeight(mScroller.getCurrY());//改变头部高度，实现回滚
            } else {

            }
            postInvalidate();
        }
        super.computeScroll();
    }

    /**
     * stop refresh, reset header view.
     * 停止刷新，重置头部
     */
    public void stopRefresh() {
        if (mPullRefreshing == true) {
            mPullRefreshing = false;
            resetHeaderHeight();
        }
    }
}