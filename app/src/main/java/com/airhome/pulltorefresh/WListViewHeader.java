package com.airhome.pulltorefresh;

import android.content.Context;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

/**
 * Created by airhome on 2015/11/5.
 */
public class WListViewHeader extends LinearLayout {
    private LinearLayout mHeaderContainer;
    private TextView mStateTextView;
    private ProgressBar mRefreshingBar;
    private State mState = State.NORMAL;

    public enum State {
        NORMAL, PULL, REFRESHABLE, REFRESHING
    }

    public WListViewHeader(Context context) {
        super(context);
        initView(context);
    }

    public WListViewHeader(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView(context);
    }

    public WListViewHeader(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView(context);
    }

    private void initView(Context context) {
        LinearLayout.LayoutParams layoutParams = new LayoutParams(LayoutParams.MATCH_PARENT, 0);
        mHeaderContainer = (LinearLayout) LayoutInflater.from(context).inflate(R.layout.item_header, null);
        addView(mHeaderContainer, layoutParams);
        setGravity(Gravity.BOTTOM);

        mStateTextView = (TextView) mHeaderContainer.findViewById(R.id.refresh_state_message);
        mRefreshingBar = (ProgressBar) mHeaderContainer.findViewById(R.id.refreshing_bar);
    }

    private void updateState(State state) {
        mRefreshingBar.setVisibility(View.GONE);
        switch (state) {
            case NORMAL:
                mStateTextView.setText("下拉刷新...");
                break;
            case PULL:
                mStateTextView.setText("下拉刷新...");
                break;
            case REFRESHABLE:
                mStateTextView.setText("松开刷新...");
                break;
            case REFRESHING:
                mStateTextView.setText("正在刷新...");
                mRefreshingBar.setVisibility(View.VISIBLE);
                break;
            default:
                break;
        }
    }

    public State getState() {
        return mState;
    }

    public void setState(State state) {
        mState = state;
        updateState(state);
    }

    public void setVisibleHeight(int height) {
        if (height < 0) {
            height = 0;
        }
        LinearLayout.LayoutParams layoutParams = (LayoutParams) mHeaderContainer.getLayoutParams();
        layoutParams.height = height;
        mHeaderContainer.setLayoutParams(layoutParams);
    }

    public int getVisibleHeight() {
        return mHeaderContainer.getLayoutParams().height;
    }
}
