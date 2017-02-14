package com.lyape.bottomtabbar;

import android.animation.ArgbEvaluator;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

/**
 * Created by xuqiang on 2017/2/13.
 */

public class BottomTabLayout extends LinearLayout {
    public BottomTabLayout(Context context) {
        super(context);
        init();
    }

    public BottomTabLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public BottomTabLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public BottomTabLayout(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init();
    }

    private ArgbEvaluator mColorEvaluator;
    private int mLastPosition;
    private int mSelectedPosition;
    private float mSelectionOffset;
    private void init(){
        mColorEvaluator = new ArgbEvaluator();
    }

    private TabItemProvider mTabItemProvider;
    private ViewPager mViewPager;
    public BottomTabLayout setUp(TabItemProvider provider,ViewPager viewPager){
        this.mTabItemProvider = provider;
        this.mViewPager = viewPager;
        populateTabLayout();
        return  this;
    }

    private ViewPager.OnPageChangeListener mViewPagerPageChangeListener;
    public BottomTabLayout setOnPageChangeListener(ViewPager.OnPageChangeListener onPageChangeListener){
        this.mViewPagerPageChangeListener = onPageChangeListener;
        return  this;
    }

    private View[] mTabItemLayouts;
    private int mSelectedTextColor;
    private int mUnSelectedTextColor;
    private void populateTabLayout(){
        checkedData();
        removeAllViews();

        TabClickListener tabClickListener = new TabClickListener();
        mViewPager.setOnPageChangeListener(new InternalViewPagerListener());
        mSelectedTextColor = getResources().getColor(mTabItemProvider.getSelectedColor());
        mUnSelectedTextColor = getResources().getColor(mTabItemProvider.getUnSelectedColor());

        mTabItemLayouts = new View[mTabItemProvider.getCount()];
        for(int i = 0 ; i < mTabItemProvider.getCount(); i ++ ){
            View view = mTabItemProvider.getItem(i,this);
            mTabItemLayouts[i] = view;
            view.setOnClickListener(tabClickListener);

            TabIconImageView imageView = (TabIconImageView) view.findViewById(R.id.bottom_tab_image);
            imageView.init(mTabItemProvider.getUnSelectedIcon(i),mTabItemProvider.getSelectedIcon(i));

            TextView textView = (TextView) view.findViewById(R.id.bottom_tab_text);
            textView.setTextColor(mUnSelectedTextColor);
            textView.setText(mTabItemProvider.getTitle(i));

            LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT,1);
            lp.gravity = Gravity.CENTER;
            addView(view,lp);
            if(i == mViewPager.getCurrentItem()){
                imageView.transformPage(0);
                view.setSelected(true);
                textView.setTextColor(mSelectedTextColor);
            }

        }
    }

    private class InternalViewPagerListener implements ViewPager.OnPageChangeListener {
        private int mScrollState;

        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            Log.e("BottomTabLayout","onPageScrolled ---position=" + position + ",positionOffset=" + positionOffset);
            onViewPagerPageChanged(position, positionOffset);

            if (mViewPagerPageChangeListener != null) {
                mViewPagerPageChangeListener.onPageScrolled(position, positionOffset, positionOffsetPixels);
            }
        }

        @Override
        public void onPageSelected(int position) {
            Log.e("BottomTabLayout","onPageSelected ----position=" + position );
            for (int i = 0; i < getChildCount(); i++) {
                ((TabIconImageView) mTabItemLayouts[i].findViewById(R.id.bottom_tab_image))
                        .transformPage(position == i ? 0 : 1);
                ((TextView) mTabItemLayouts[i].findViewById(R.id.bottom_tab_text))
                        .setTextColor(position == i ? mSelectedTextColor: mUnSelectedTextColor);
            }

            if (mScrollState == ViewPager.SCROLL_STATE_IDLE) {
                onViewPagerPageChanged(position, 0f);
            }

            for (int i = 0, size = getChildCount(); i < size; i++) {
                getChildAt(i).setSelected(position == i);
            }


            if (mViewPagerPageChangeListener != null) {
                mViewPagerPageChangeListener.onPageSelected(position);
            }
        }

        @Override
        public void onPageScrollStateChanged(int state) {
            mScrollState = state;

            if (mViewPagerPageChangeListener != null) {
                mViewPagerPageChangeListener.onPageScrollStateChanged(state);
            }
        }
    }

    private void onViewPagerPageChanged(int position, float positionOffset) {
        mSelectedPosition = position;
        mSelectionOffset = positionOffset;
        if (positionOffset == 0f && mLastPosition != mSelectedPosition) {
            mLastPosition = mSelectedPosition;
        }
        invalidate();
    }

    private void checkedData(){
        if(mTabItemProvider == null){
            throw new RuntimeException("TabItemProvider must not null, please invoke setUp() to set");
        }
        if(mViewPager == null){
            throw new RuntimeException("ViewPager must not null, please invoke setUp() to set");
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        checkedData();
        final int childCount = getChildCount();
        if (childCount > 0) {
            if (mSelectionOffset > 0f && mSelectedPosition < (getChildCount() - 1)) {
                View selectedTab = getChildAt(mSelectedPosition);
                View nextTab = getChildAt(mSelectedPosition + 1);

                TabIconImageView selectedIconView = (TabIconImageView) selectedTab.findViewById(R.id.bottom_tab_image);
                TabIconImageView nextIconView = (TabIconImageView) nextTab.findViewById(R.id.bottom_tab_image);

                TextView selectedTextView = (TextView) selectedTab.findViewById(R.id.bottom_tab_text);
                TextView nextTextView = (TextView) nextTab.findViewById(R.id.bottom_tab_text);

                selectedIconView.transformPage(mSelectionOffset);
                nextIconView.transformPage(1 - mSelectionOffset);
                //draw text color
                Integer selectedColor = (Integer) mColorEvaluator.evaluate(mSelectionOffset,
                        mSelectedTextColor,
                        mUnSelectedTextColor);
                Integer nextColor = (Integer) mColorEvaluator.evaluate(1 - mSelectionOffset,
                        mSelectedTextColor,
                        mUnSelectedTextColor);

                selectedTextView.setTextColor(selectedColor);
                nextTextView.setTextColor(nextColor);
            }
        }
    }

    private class TabClickListener implements OnClickListener {
        @Override
        public void onClick(View v) {
            for (int i = 0; i < getChildCount(); i++) {
                if (v == getChildAt(i)) {
                    mViewPager.setCurrentItem(i,false);
                    return;
                }
            }
        }
    }

}
