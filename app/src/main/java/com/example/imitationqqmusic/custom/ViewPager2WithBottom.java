package com.example.imitationqqmusic.custom;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import androidx.viewpager2.widget.ViewPager2;


public class ViewPager2WithBottom extends ViewGroup {

    private ViewPager2 viewPager2;
    private LinearLayout linearLayout;

    private int bottomNumber = -1;

    private Rect mTmpContainerRect = new Rect();
    private final Rect mTmpChildRect = new Rect();

    public ViewPager2WithBottom(Context context) {
        super(context);
        viewPager2 = new ViewPager2(context);
        linearLayout = new LinearLayout(context);
    }

    public ViewPager2WithBottom(Context context, AttributeSet attrs) {
        super(context, attrs);
        viewPager2 = new ViewPager2(context, attrs);
        linearLayout = new LinearLayout(context, attrs);
    }

    public ViewPager2WithBottom(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        viewPager2 = new ViewPager2(context, attrs, defStyleAttr);
        linearLayout = new LinearLayout(context, attrs, defStyleAttr);
    }

    public ViewPager2WithBottom(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        viewPager2 = new ViewPager2(context, attrs, defStyleAttr, defStyleRes);
        linearLayout = new LinearLayout(context, attrs, defStyleAttr, defStyleRes);
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        int width = getMeasuredWidth();
        int height = getMeasuredHeight();

        mTmpContainerRect.left = getPaddingLeft();
        mTmpContainerRect.right = r - l - getPaddingRight();
        mTmpContainerRect.top = getPaddingTop();
        mTmpContainerRect.bottom = b - t - getPaddingBottom();

        Gravity.apply(Gravity.TOP | Gravity.START, width, height, mTmpContainerRect, mTmpChildRect);
        viewPager2.layout(mTmpChildRect.left, mTmpChildRect.top, mTmpChildRect.right, mTmpChildRect.bottom);

        if (bottomNumber == -1 || bottomNumber == 0){
            return;
        }

        @SuppressLint("DrawAllocation") LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
        );
        linearLayout.setOrientation(LinearLayout.HORIZONTAL);
        layoutParams.gravity = Gravity.BOTTOM;

        linearLayout.setBackgroundColor(Color.BLACK);

        addView(linearLayout, layoutParams);
    }

    public void setBottomViewNum(int number){
        this.bottomNumber = number;
    }
}
