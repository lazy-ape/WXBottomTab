
package com.lyape.bottomtabbar;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.widget.ImageView;

/**
 * Created by wuyexiong on 4/25/15.
 */
public class TabIconImageView extends ImageView {

    private Paint mPaint;
    private Bitmap mSelectedIcon;
    private Bitmap mNormalIcon;
    private Rect mSelectedRect;
    private Rect mNormalRect;
    private int mSelectedAlpha = 0;

    public TabIconImageView(Context context) {
        super(context);
    }

    public TabIconImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public TabIconImageView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public final void init(int normal, int selected) {
        setImageDrawable(null);
        this.mNormalIcon = createBitmap(normal);
        this.mSelectedIcon = createBitmap(selected);
        this.mPaint = new Paint(1);
    }

    private Bitmap createBitmap(int resId) {
        return BitmapFactory.decodeResource(getResources(), resId);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if(mSelectedRect == null || mNormalRect == null){
            this.mNormalRect = new Rect(0, 0, this.getWidth() == 0 ? this.mNormalIcon.getWidth():this.getWidth(),
                    this.getHeight() == 0? this.mNormalIcon.getHeight() : this.getHeight());
            this.mSelectedRect = new Rect(0, 0, this.getWidth() == 0 ? this.mSelectedIcon.getWidth() : this.getWidth(),
                    this.getHeight() == 0 ? this.mSelectedIcon.getHeight() : this.getHeight());
        }
        if (this.mPaint == null) {
            return;
        }
        this.mPaint.setAlpha(255 - this.mSelectedAlpha);
        canvas.drawBitmap(this.mNormalIcon, null, this.mNormalRect, this.mPaint);
        this.mPaint.setAlpha(this.mSelectedAlpha);
        canvas.drawBitmap(this.mSelectedIcon, null, this.mSelectedRect, this.mPaint);
    }

    public final void changeSelectedAlpha(int alpha) {
        this.mSelectedAlpha = alpha;
        invalidate();
    }

    public final void transformPage(float offset) {
        changeSelectedAlpha((int) (255 * (1 - offset)));
    }
}
