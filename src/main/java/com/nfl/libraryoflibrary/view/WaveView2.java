package com.nfl.libraryoflibrary.view;

import android.animation.ValueAnimator;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PointF;
import android.graphics.Shader;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;

import com.nfl.libraryoflibrary.R;

/**
 * Created by fuli.niu on 2018/1/29.
 */

public class WaveView2 extends View {

    private float mWavePeak = 35f; // 波峰
    private float mWaveTrough = 35f; // 波槽
    private float mWaveHeight = 25f; // 水位
    private int delay = 0;
    private Paint mPaint;
    private LinearGradient linearGradient;
    private ValueAnimator valueAnimator;
    private Path mPath;
    private int mWaterColor = 0xBB0000FF;
    private float mWidth, mHeight;
    private PointF mStart, mLeft1, mLeft2, mFirst, mSecond, mRight;
    private PointF mControlLeft1, mControlLeft2, mControlFirst, mControlSecond;
    private boolean mHasInit = false;

    public WaveView2(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.WaveView2);
        delay = typedArray.getInteger(R.styleable.WaveView2_delay, 0);
        typedArray.recycle();
    }

    public WaveView2(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public WaveView2(Context context) {
        this(context, null);
    }

    private void init() {
        mPath = new Path();
        mPaint = new Paint();
        mPaint.setAntiAlias(true);
        mPaint.setStyle(Paint.Style.FILL);
        mPaint.setColor(mWaterColor);
        setRotationY(180);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        if (!mHasInit) {
            mWidth = w;
            mHeight = h;
            mHasInit = true;
            linearGradient = new LinearGradient(0, mHeight - mWaveHeight - mWavePeak - mWaveTrough,
                    0, mHeight,
                    Color.parseColor("#F5C8C8"), Color.parseColor("#F5E6F0"),
                    Shader.TileMode.CLAMP);
            mPaint.setShader(linearGradient);
            initPoints();
            initAnim();
            startAnim(delay);
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (!mHasInit) {
            return;
        }
        mPath.reset();
        mPath.moveTo(mLeft1.x, mLeft1.y);
        mPath.quadTo(mControlLeft1.x, mControlLeft1.y, mLeft2.x, mLeft2.y);
        mPath.quadTo(mControlLeft2.x, mControlLeft2.y, mFirst.x, mFirst.y);
        mPath.quadTo(mControlFirst.x, mControlFirst.y, mSecond.x, mSecond.y);
        mPath.quadTo(mControlSecond.x, mControlSecond.y, mRight.x, mRight.y);
        mPath.lineTo(mRight.x, mHeight);
        mPath.lineTo(mLeft1.x, mHeight);
        mPath.lineTo(mLeft1.x, mLeft1.y);
        canvas.drawPath(mPath, mPaint);
    }

    /**
     * 初始化需要描绘的点和控制点
     */
    private void initPoints() {
        // 开始的点（最左侧的点）
        mStart = new PointF(-mWidth, mHeight - mWaveHeight);
        // 初始化需要绘制的点
        mLeft1 = new PointF(-mWidth, mHeight - mWaveHeight);
        mLeft2 = new PointF(mLeft1.x + mWidth / 2f, mHeight - mWaveHeight);
        mFirst = new PointF(mLeft2.x + mWidth / 2f, mHeight - mWaveHeight);
        mSecond = new PointF(mFirst.x + mWidth / 2f, mHeight - mWaveHeight);
        mRight = new PointF(mSecond.x + mWidth / 2f, mHeight - mWaveHeight);
        // 设置控制点
        mControlLeft1 = new PointF(mLeft1.x + mWidth / 4f, mLeft1.y + mWavePeak);
        mControlLeft2 = new PointF(mLeft2.x + mWidth / 4f, mLeft2.y - mWaveTrough);
        mControlFirst = new PointF(mFirst.x + mWidth / 4f, mFirst.y + mWavePeak);
        mControlSecond = new PointF(mSecond.x + mWidth / 4f, mSecond.y - mWaveTrough);
    }

    private void initAnim() {
        valueAnimator = ValueAnimator.ofFloat(mStart.x, 0);
        valueAnimator.setInterpolator(new LinearInterpolator());
        valueAnimator.setDuration(2000);
        valueAnimator.setRepeatCount(Animation.INFINITE);
        // 动画效果重复
        // valueAnimator.setRepeatMode(Animation.RESTART);
        valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                mLeft1.x = (float) animation.getAnimatedValue();
                mLeft2 = new PointF(mLeft1.x + mWidth / 2f, mHeight - mWaveHeight);
                mFirst = new PointF(mLeft2.x + mWidth / 2f, mHeight - mWaveHeight);
                mSecond = new PointF(mFirst.x + mWidth / 2f, mHeight - mWaveHeight);
                mRight = new PointF(mSecond.x + mWidth / 2f, mHeight - mWaveHeight);
                //
                mControlLeft1 = new PointF(mLeft1.x + mWidth / 4f, mLeft1.y + mWavePeak);
                mControlLeft2 = new PointF(mLeft2.x + mWidth / 4f, mLeft2.y - mWaveTrough);
                mControlFirst = new PointF(mFirst.x + mWidth / 4f, mFirst.y + mWavePeak);
                mControlSecond = new PointF(mSecond.x + mWidth / 4f, mSecond.y - mWaveTrough);
                invalidate();
            }
        });
    }

    public void startAnim() {
        startAnim(0);
    }

    public void startAnim(int delay) {
        if (null != valueAnimator) {
            postDelayed(new Runnable() {
                @Override
                public void run() {
                    if (valueAnimator.isRunning()) {
                        valueAnimator.cancel();
                        valueAnimator.start();
                    } else {
                        valueAnimator.start();
                    }
                }
            }, delay);
        }
    }

    public void cancelAnim() {
        if (null != valueAnimator) {
            valueAnimator.cancel();
        }
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        if (null != valueAnimator) {
            valueAnimator.cancel();
        }
    }
}
