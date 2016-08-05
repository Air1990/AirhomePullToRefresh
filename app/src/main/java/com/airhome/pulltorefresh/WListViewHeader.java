package com.airhome.pulltorefresh;

import android.content.Context;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

/**
 * Created by airhome on 2015/11/5.
 */
public class WListViewHeader extends RelativeLayout {
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
        DisplayMetrics metrics = new DisplayMetrics();
        ((WindowManager) context.getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay().
                getMetrics(metrics);
        float screenDensity = metrics.density;

        mHeaderContainer = new LinearLayout(getContext());
        mHeaderContainer.setBackgroundColor(0xCCC);
        mHeaderContainer.setGravity(Gravity.BOTTOM);
        LinearLayout.LayoutParams containerParams = new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, 0);
        mHeaderContainer.setLayoutParams(containerParams);

        RelativeLayout refreshLayout = new RelativeLayout(getContext());
        RelativeLayout.LayoutParams refreshParams = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, (int) (60 * screenDensity));
        refreshLayout.setLayoutParams(refreshParams);


        mStateTextView=new TextView(getContext());
        mStateTextView.setPadding(0,0,0,(int)(20*screenDensity));
        mStateTextView.setTextSize(TypedValue.COMPLEX_UNIT_SP,15);
        RelativeLayout.LayoutParams textParams= new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT);
        textParams.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
        textParams.addRule(RelativeLayout.CENTER_HORIZONTAL);
        mStateTextView.setLayoutParams(textParams);

        mRefreshingBar = new ProgressBar(getContext());
        mRefreshingBar.setVisibility(GONE);
        RelativeLayout.LayoutParams progressParams = new RelativeLayout.LayoutParams((int) (20 * screenDensity),
                (int) (20 * screenDensity));
        progressParams.addRule(RelativeLayout.ABOVE,mStateTextView.getId());
        progressParams.addRule(RelativeLayout.CENTER_HORIZONTAL);
        mRefreshingBar.setLayoutParams(progressParams);

        refreshLayout.addView(mStateTextView);
        refreshLayout.addView(mRefreshingBar);

        mHeaderContainer.addView(refreshLayout);

        addView(mHeaderContainer);
    }

    private void updateState(State state) {
        mRefreshingBar.setVisibility(View.GONE);
        switch (state) {
            case NORMAL:
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
        RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) mHeaderContainer.getLayoutParams();
        layoutParams.height = height;
        mHeaderContainer.setLayoutParams(layoutParams);
    }

    public int getVisibleHeight() {
        return mHeaderContainer.getLayoutParams().height;
    }
}
