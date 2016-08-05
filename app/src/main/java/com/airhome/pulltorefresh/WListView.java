package com.airhome.pulltorefresh;

import android.content.Context;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.view.MotionEvent;
import android.view.WindowManager;
import android.view.animation.DecelerateInterpolator;
import android.widget.ListView;
import android.widget.Scroller;

import com.airhome.pulltorefresh.WListViewHeader.State;


/**
 * Created by airhome on 2015/11/5.
 */
public class WListView extends ListView {
    private static final float OFFSET_RATIO = 1.8f;
    private static final int SCROLL_DURATION = 300;
    private static int REFRESHING_HEIGHT = 70;
    private static int REFRESH_READY_HEIGHT = 70;
    private WListViewHeader mHeaderView;
    private OnRefreshListener mListener;
    private Scroller mScroller;
    private float startY = 0;

    public WListView(Context context) {
        super(context);
        initView(context);
    }

    public WListView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView(context);
    }

    public WListView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView(context);
    }

    private void initView(Context context) {
        DisplayMetrics metrics = new DisplayMetrics();
        ((WindowManager) context.getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay().
                getMetrics(metrics);
        float screenDensity = metrics.density;
        REFRESH_READY_HEIGHT = (int) (REFRESH_READY_HEIGHT * screenDensity);
        REFRESHING_HEIGHT = (int) (REFRESHING_HEIGHT * screenDensity);
        mScroller = new Scroller(context, new DecelerateInterpolator());
        mHeaderView = new WListViewHeader(context);
        addHeaderView(mHeaderView);
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        if (mHeaderView.getState() != State.REFRESHING) {
            switch (ev.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    startY = ev.getY();
                    break;
                case MotionEvent.ACTION_MOVE:
                    float deltaY = ev.getY() - startY;
                    if (deltaY > 5 && getFirstVisiblePosition() == 0 &&
                            mHeaderView.getVisibleHeight() == 0) {
                        mHeaderView.setState(State.PULL);
                    }
                    State state = mHeaderView.getState();
                    if (state == State.PULL || state == State.REFRESHABLE) {
                        startY = ev.getY();
                        updateHeaderHeight(deltaY / OFFSET_RATIO);
                    }
                    break;
                case MotionEvent.ACTION_UP:
                    int visibleHeight = mHeaderView.getVisibleHeight();
                    if (mHeaderView.getState() == State.REFRESHABLE) {
                        mHeaderView.setState(State.REFRESHING);
                        mScroller.startScroll(0, visibleHeight, 0,
                                REFRESHING_HEIGHT - visibleHeight, SCROLL_DURATION);
                        invalidate();
                        if (mListener != null) {
                            mListener.doWhileRefreshing();
                        } else {
                            try {
                                throw new Exception("WListView didn't register listener!");
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    } else {
                        mHeaderView.setState(State.NORMAL);
                        mScroller.startScroll(0, visibleHeight, 0,
                                -visibleHeight, SCROLL_DURATION);
                        invalidate();
                    }
                    break;
                default:
                    break;
            }
        }
        return super.onTouchEvent(ev);
    }

    private void setHeaderHeight(int headerHeight) {
        mHeaderView.setVisibleHeight(headerHeight);
    }

    private void updateHeaderHeight(float changeHeight) {
        mHeaderView.setVisibleHeight((int) changeHeight + mHeaderView.getVisibleHeight());
        int visibleHeight = mHeaderView.getVisibleHeight();
        if (visibleHeight == 0) {
            mHeaderView.setState(State.NORMAL);
        } else if (visibleHeight >= REFRESH_READY_HEIGHT) {
            mHeaderView.setState(State.REFRESHABLE);
        } else if (visibleHeight < REFRESH_READY_HEIGHT) {
            mHeaderView.setState(WListViewHeader.State.PULL);
        }
        setSelection(0);
    }

    @Override
    public void computeScroll() {
        super.computeScroll();
        if (mScroller.computeScrollOffset()) {
            setHeaderHeight(mScroller.getCurrY());
            postInvalidate();
        }
    }

    public void finishRefresh() {
        mHeaderView.setState(State.NORMAL);
        setHeaderHeight(0);
    }

    public void setOnRefreshListener(OnRefreshListener listener) {
        mListener = listener;
    }

    public interface OnRefreshListener {
        void doWhileRefreshing();
    }
}
