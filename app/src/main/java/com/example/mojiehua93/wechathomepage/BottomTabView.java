package com.example.mojiehua93.wechathomepage;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.os.Looper;
import android.os.Parcelable;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.View;

/**
 * Created by MOJIEHUA93 on 2017/10/22.
 */

public class BottomTabView extends View {
    public static final int DEFAULT_COLOR = 0xFF45C01A;
    public static final String INSTANCE_STATUS = "instance_status";
    public static final String STATUS_ALPHA = "status_alpha";
    private Bitmap mIcon;
    private int mColor = 0xFF45C01A;
    private String mText;
    private int mTextSize;

    private Canvas mCanvas;
    private Bitmap mBitmap;
    private Paint mPaint;

    private float mAlpha;

    private Rect mIconRect;
    private Rect mTextBound;

    private Paint mTextPaint;
    public BottomTabView(Context context) {
        this(context, null);
    }

    public BottomTabView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public BottomTabView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.BottomTabView);
        int n = a.getIndexCount();
        for (int i=0; i < n; i++){
            int attr = a.getIndex(i);
            switch (attr){
                case R.styleable.BottomTabView_icon:
                    BitmapDrawable drawable = (BitmapDrawable) a.getDrawable(attr);
                    mIcon = drawable.getBitmap();
                    break;

                case R.styleable.BottomTabView_color:
                    mColor = a.getColor(attr, DEFAULT_COLOR);
                    break;

                case R.styleable.BottomTabView_text:
                    mText = a.getString(attr);
                    break;

                case R.styleable.BottomTabView_text_size:
                    mTextSize = a.getDimensionPixelSize(attr,pxToSp(context, 12));
                    break;
            }
        }
        a.recycle();

        mTextBound = new Rect();
        mTextPaint = new Paint();
        mTextPaint.setTextSize(mTextSize);
        mTextPaint.setColor(0xFF555555);
        mTextPaint.getTextBounds(mText, 0, mText.length(),mTextBound);
    }

    private int pxToDp(Context context, int px){
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, px,
                context.getResources().getDisplayMetrics());
    }

    private int pxToSp(Context context, int px){
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, px,
                context.getResources().getDisplayMetrics());
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int iconWidth = Math.min(getMeasuredWidth() - getPaddingLeft() - getPaddingRight(),
                getMeasuredHeight() - getPaddingTop() - getPaddingBottom() -
                        mTextBound.height());
        int left = getMeasuredWidth()/2 - iconWidth/2;
        int top = getMeasuredHeight()/2 - (iconWidth/2 + mTextBound.height()/2);
        mIconRect = new Rect(left, top, left + iconWidth, top + iconWidth);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.drawBitmap(mIcon, null, mIconRect, null);

        int alpha = (int) Math.ceil(255 * mAlpha);

        setupTargetView(alpha);
        drawSourceText(canvas, alpha);
        drawTargetText(canvas, alpha);
        canvas.drawBitmap(mBitmap, 0, 0, null);
    }

    private void drawTargetText(Canvas canvas, int alpha) {
        mTextPaint.setColor(mColor);
        mTextPaint.setAlpha(alpha);
        int x = getMeasuredWidth() / 2 - mTextBound.width() / 2;
        int y = mIconRect.bottom + mTextBound.height();
        canvas.drawText(mText, x, y, mTextPaint);
    }

    private void drawSourceText(Canvas canvas, int alpha) {
        mTextPaint.setColor(0xff333333);
        mTextPaint.setAlpha(255 - alpha);
        int x = getMeasuredWidth() / 2 - mTextBound.width() / 2;
        int y = mIconRect.bottom + mTextBound.height();
        canvas.drawText(mText, x, y, mTextPaint);
    }

    private void setupTargetView(int alpha) {
        mBitmap = Bitmap.createBitmap(getMeasuredWidth(), getMeasuredHeight(),
                Bitmap.Config.ARGB_8888);

        mCanvas = new Canvas(mBitmap);
        mPaint = new Paint();
        mPaint.setColor(mColor);
        mPaint.setAntiAlias(true);
        mPaint.setAlpha(alpha);//default alpha is 0;
        mPaint.setDither(true);
        mCanvas.drawRect(mIconRect, mPaint);

        mPaint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.DST_IN));
        mPaint.setAlpha(255);
        mCanvas.drawBitmap(mIcon, null, mIconRect, mPaint);
    }

    public void setIconAlpha(float alpha) {
        this.mAlpha = alpha;
        invalidateView();
    }

    private void invalidateView() {
        if (Looper.getMainLooper() == Looper.myLooper()){
            invalidate();
        } else {
            postInvalidate();
        }
    }

    @Override
    protected Parcelable onSaveInstanceState() {
        Bundle bundle = new Bundle();
        bundle.putParcelable(INSTANCE_STATUS, super.onSaveInstanceState());
        bundle.putFloat(STATUS_ALPHA, mAlpha);
        return bundle;
    }

    @Override
    protected void onRestoreInstanceState(Parcelable state) {
        if (state instanceof Bundle){
            Bundle bundle = (Bundle) state;
            super.onRestoreInstanceState(bundle.getParcelable(INSTANCE_STATUS));
            mAlpha = bundle.getFloat(STATUS_ALPHA);
            return;
        }
        super.onRestoreInstanceState(state);
    }
}
