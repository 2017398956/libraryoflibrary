package com.nfl.libraryoflibrary.view;

import android.animation.ValueAnimator;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Shader;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.LinearInterpolator;

import com.nfl.libraryoflibrary.R;
import com.nfl.libraryoflibrary.utils.ConvertTool;
import com.nfl.libraryoflibrary.utils.LogTool;

/**
 * Created by fuli.niu on 2018/1/26.
 */

public class WaveView extends View {
    private int width = 0;
    private int height = 0;
    private int baseLine = 0;// 基线，用于控制水位上涨的，这里是写死了没动，你可以不断的设置改变。
    private Paint mPaint;
    private int waveHeight = ConvertTool.dp2px(25);// 波浪的最高度
    private int waveWidth;//波长
    private int waveSpeed = 3000;// 波浪滚动速度，即整个动画所需的时间，单位：毫秒
    private float quadrant = 0;
    private float offset = 0f;//偏移量

    public WaveView(Context context, AttributeSet attrs) {
        super(context, attrs);
        LogTool.i("WaveView:WaveView");
        TypedArray array = context.obtainStyledAttributes(attrs, R.styleable.WaveView);
        quadrant = array.getFloat(R.styleable.WaveView_quadrant, 0);
        quadrant = 0 ;
        array.recycle();
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
        LogTool.i("WaveView:onLayout");
        width = getMeasuredWidth();//获取屏幕宽度
        height = getMeasuredHeight();//获取屏幕高度
        waveWidth = width;
        baseLine = height - waveHeight;
        initView();
        updateXControl();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.drawPath(getPathRight2Left(), mPaint);
    }

    //初始化paint，没什么可说的。
    private void initView() {
        mPaint = new Paint();
        mPaint.setColor(Color.parseColor("#F5C8C8"));
        mPaint.setStyle(Paint.Style.FILL);
        Paint paint = new Paint();  //定义一个Paint
        Shader mShader = new LinearGradient(0, baseLine - waveHeight, waveWidth, baseLine - waveHeight,
                new int[]{Color.parseColor("#F5C8C8"), Color.parseColor("#F5DCDC"), Color.parseColor("#F5E6F0")},
                null, Shader.TileMode.REPEAT);
        // 新建一个线性渐变，前两个参数是渐变开始的点坐标，第三四个参数是渐变结束的点的坐标。连接这2个点就拉出一条渐变线了，玩过PS的都懂。然后那个数组是渐变的颜色。
        // 下一个参数是渐变颜色的分布，如果为空，每个颜色就是均匀分布的。最后是模式，这里设置的是循环渐变
        paint.setShader(mShader);
    }

    /**
     * 不断的更新偏移量，并且循环。
     */
    private void updateXControl() {
        //设置一个波长的偏移
        ValueAnimator mAnimator = ValueAnimator.ofFloat(0, waveWidth);
        mAnimator.setInterpolator(new LinearInterpolator());
        mAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {

            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                float animatorValue = (float) animation.getAnimatedValue();
                offset = animatorValue ;//不断的设置偏移量，并重画
                postInvalidate();
            }
        });
        mAnimator.setDuration(waveSpeed);
        mAnimator.setRepeatCount(ValueAnimator.INFINITE);
        mAnimator.start();
    }

    /**
     * 核心代码，计算path
     *
     * @return
     */
    private Path getPathLeft2Right() {
        int itemWidth = waveWidth / 2;//半个波长
        Path mPath = new Path();
        mPath.moveTo(-itemWidth * 3 + quadrant * itemWidth , baseLine);//起始坐标
        //核心的代码就是这里
        for (int i = -3; i < 2; i++) {
            float startX = i * itemWidth + quadrant * itemWidth ;
            mPath.quadTo(
                    startX + itemWidth / 2 + offset,//控制点的X,（起始点X + itemWidth/2 + offset)
                    getWaveHeigh(i),//控制点的Y
                    startX + itemWidth + offset,//结束点的X
                    baseLine//结束点的Y
            );//只需要处理完半个波长，剩下的有for循环自已就添加了。
        }
        //下面这三句话很重要，它是形成了一封闭区间，让曲线以下的面积填充一种颜色，大家可以把这3句话注释了看看效果。
        mPath.lineTo(width, height);
        mPath.lineTo(0, height);
        mPath.close();
        return mPath;
    }

    private Path getPathRight2Left() {
        int itemWidth = waveWidth / 2;//半个波长
        Path mPath = new Path();
        mPath.moveTo(itemWidth * 5 - quadrant * itemWidth, baseLine);//起始坐标
        //核心的代码就是这里
        for (int i = 5; i > 0; i--) {
            float startX = i * itemWidth - quadrant * itemWidth;
            mPath.quadTo(
                    startX - itemWidth / 2 - offset,//控制点的X,（起始点X + itemWidth/2 + offset)
                    getWaveHeigh(i),//控制点的Y
                    startX - itemWidth - offset,//结束点的X
                    baseLine//结束点的Y
            );//只需要处理完半个波长，剩下的有for循环自已就添加了。
        }
        //下面这三句话很重要，它是形成了一封闭区间，让曲线以下的面积填充一种颜色，大家可以把这3句话注释了看看效果。
        mPath.lineTo(0, height);
        mPath.lineTo(width, height);
        mPath.close();
        return mPath;
    }

    //奇数峰值是正的，偶数峰值是负数
    private int getWaveHeigh(int num) {
        if (num % 2 == 0) {
            return baseLine + waveHeight;
        }
        return baseLine - waveHeight;
    }
}
