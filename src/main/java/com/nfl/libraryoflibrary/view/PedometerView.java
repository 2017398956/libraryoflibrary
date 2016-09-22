package com.nfl.libraryoflibrary.view;

import android.animation.ValueAnimator;
import android.app.Activity;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.DashPathEffect;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PathEffect;
import android.graphics.Rect;
import android.os.Looper;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.MotionEvent;
import android.view.View;

import com.nfl.libraryoflibrary.utils.DateTool;

import java.util.Calendar;

/**
 * Created by fuli.niu on 2016/7/6.
 */
public class PedometerView extends View {

    private Context context;
    private Paint headerPaint;// 头部画笔
    private Paint todayNumberPaint ;// 今天步数画笔
    private Paint middleTextPaint ; // "1w"的画笔
    private Rect headerPaintRect;// 用于绘制头部文字区域，如：“步数”
    private int headerPaintHeight;// 头部的高度
    private Paint bottomPaint;// 底部日期画笔
    private Rect bottomPaintRect;// 底部日期文字 区域
    private int bottomPaintHeight;// 底部日期的高度
    private Paint brokenLinePaint;// 折线图画笔
    private Paint stepsNumberPaint;// 步数弹出文字画笔
    private float stepsNumberX;// 步数弹出文字x坐标
    private float stepsNumberY;// 步数弹出文字y坐标
    //    private String stepsNubmer; // 步数弹出文字的值
    private String todayStepsNubmer; // 今天的步数
    private float[] positionXs;// 用于记住每个日期的x坐标
    private float[] positionYs;// 和positionXs的个数一直步数Y坐标
    private int clickPosition;// 哪个日期被点击了
    private int pedometerViewWidth;// 画布宽度
    private int pedometerViewHeight;// 画布高度
    private float headerTextSize;// 头部字体大小
    private float bottomTextSize;// 底部日期字体大小
    private final int spaceV = 30; // 垂直方向的间隔
    private int line01Height;// 第一条线的高度
    private int line02Height;// 第二天线的高度
    private int line03Height; // 第三条线的高度
    private int contentHeight = 400; // 从第一条线到第三条线的高度,默认显示20000步
    private int maxStep = 20000; // 默认最大步数为10000 ；
    private long  maxTime ;

    public PedometerView(Context context) {
        this(context, null);
    }

    public PedometerView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public PedometerView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.context = context;
        maxTime = System.currentTimeMillis() ;
        DisplayMetrics dm = new DisplayMetrics();
        ((Activity)context).getWindowManager().getDefaultDisplay().getMetrics(dm);
        contentHeight = (int) (contentHeight * ( dm.density / 3.5)) ;
//        stepsNubmer = "9999";// 默认9999
        positionXs = new float[7];
        positionYs = new float[]{0, 0, 0, 0, 0, 0, 0};
        clickPosition = -1;
        headerTextSize = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, 26,
                getResources().getDisplayMetrics());
        bottomTextSize = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, 12,
                getResources().getDisplayMetrics());
        todayStepsNubmer = (int) positionYs[6] + "";
        // 先准备画笔，得到view的高度
        drawHeader();
        drawBottom();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        drawHeader(canvas);
        drawBottom(canvas, clickPosition);
        drawBrokenLineGraph(canvas);
        drawStepsNumber(canvas, clickPosition > -1 ? (int) positionYs[clickPosition] + "" : "9999");
    }


    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        pedometerViewWidth = getMeasuredWidth();
        pedometerViewHeight = getMeasuredHeight();
        setMeasuredDimension(pedometerViewWidth, bottomPaintHeight);
    }

    /**
     * 绘制折线图头部，且字体大小固定
     */
    private void drawHeader() {
        headerPaint = new Paint();
        headerPaint.setStyle(Paint.Style.STROKE);
        headerPaint.setAntiAlias(true); //去锯齿
        headerPaint.setTextSize(headerTextSize);
        headerPaintRect = new Rect();
        headerPaint.getTextBounds(todayStepsNubmer, 0, todayStepsNubmer.length(), headerPaintRect);
        line01Height = headerPaintRect.height() + 2 * spaceV;
        line03Height = line01Height + contentHeight;
        todayNumberPaint = new Paint();
        todayNumberPaint.setStyle(Paint.Style.STROKE);
        todayNumberPaint.setAntiAlias(true); //去锯齿
        todayNumberPaint.setTextSize(headerTextSize);
        todayNumberPaint.setColor(Color.WHITE);

        middleTextPaint = new Paint();
        middleTextPaint.setStyle(Paint.Style.STROKE);
        middleTextPaint.setAntiAlias(true); //去锯齿
        middleTextPaint.setTextSize(bottomTextSize);
        middleTextPaint.setColor(Color.parseColor("#55D5D8"));
    }

    private void drawHeader(Canvas canvas) {
        line02Height = line03Height - 10000 * contentHeight / maxStep;
        headerPaint.setStyle(Paint.Style.STROKE);
        headerPaint.setAntiAlias(true); //去锯齿
        headerPaint.setTextSize(headerTextSize);
        headerPaint.setColor(Color.WHITE);
        canvas.drawText("步数", 40, line01Height - spaceV, headerPaint);
        headerPaint.getTextBounds(todayStepsNubmer, 0, todayStepsNubmer.length(), headerPaintRect);
        todayNumberPaint.getTextBounds(todayStepsNubmer, 0, todayStepsNubmer.length(), headerPaintRect);
        canvas.drawText(todayStepsNubmer, pedometerViewWidth - headerPaintRect.width() - 40 ,
                line01Height - spaceV, todayNumberPaint);

        canvas.drawLine(40, line01Height, pedometerViewWidth - 40, line01Height, headerPaint);// 画第一条线
        // 下面为画虚线和“1w”

        headerPaint.setTextSize(bottomTextSize);
        headerPaint.setColor(Color.parseColor("#55D5D8"));

        Rect rect = new Rect();
        middleTextPaint.getTextBounds("1w", 0, 2, rect);
        canvas.drawText("1w", pedometerViewWidth - 40 - 40, line02Height + rect.height() / 2, middleTextPaint);

        canvas.drawLine(40, line03Height, pedometerViewWidth - 40, line03Height, headerPaint);// 画的第三条线

        Path path = new Path();
        path.moveTo(40, line02Height);
        path.lineTo(pedometerViewWidth - 40 - rect.width(), line02Height);
        path.close();
        PathEffect pathEffect = new DashPathEffect(new float[]{5, 5, 5, 5}, 1);
        headerPaint.setPathEffect(pathEffect);
        canvas.drawPath(path, headerPaint);// 画的虚线


    }

    /**
     * 绘制底部日期
     */
    private void drawBottom() {
        bottomPaint = new Paint();
        bottomPaint.setStyle(Paint.Style.FILL);
        bottomPaint.setAntiAlias(true); //去锯齿
        bottomPaint.setTextSize(bottomTextSize);
        bottomPaintRect = new Rect();
        bottomPaint.getTextBounds("月", 0, 1, bottomPaintRect);
        bottomPaintHeight = bottomPaintRect.height() + line03Height + spaceV;
    }

    /**
     * 绘制底部日期
     *
     * @param canvas
     * @param clickPosition 根据位置更改日期的颜色，默认为-1（在onDraw（）中），不需要更改日期的颜色，
     */
    private void drawBottom(Canvas canvas, int clickPosition) {
        for (int i = 0; i < 7; i++) {
            bottomPaint.setColor(clickPosition == i ? Color.WHITE : Color.parseColor("#55D5D8"));
            positionXs[i] = pedometerViewWidth / 14 * (1 + 2 * i);
            Calendar calendar = Calendar.getInstance();
            calendar.setTimeInMillis(maxTime - 6 * 24 * 60 * 60 * 1000);
            int maxDayOfMonth = calendar.getMaximum(Calendar.DAY_OF_MONTH);
            String text;
            if (i == 0) {
                text = calendar.get(Calendar.MONTH) + 1 + "月" + calendar.get(Calendar.DAY_OF_MONTH);
            } else {
                if(i + calendar.get(Calendar.DAY_OF_MONTH) == maxDayOfMonth){
                    text = maxDayOfMonth + "" ;
                }else {
                    text = (i + calendar.get(Calendar.DAY_OF_MONTH)) % maxDayOfMonth + "";
                }
            }
            canvas.drawText(text, positionXs[i] - text.length() * bottomPaintRect.width() / 2, bottomPaintHeight - spaceV / 2, bottomPaint);
        }
    }

    /**
     * 绘制折线图
     *
     * @param canvas
     */
    private void drawBrokenLineGraph(Canvas canvas) {
        brokenLinePaint = new Paint();
        brokenLinePaint.setStyle(Paint.Style.FILL);
        brokenLinePaint.setAntiAlias(true);
        brokenLinePaint.setColor(Color.WHITE);
        brokenLinePaint.setStrokeWidth(2);
        for (int i = 0; i < 6; i++) {
            float start = line03Height - positionYs[i] * contentHeight / maxStep;
            float end = line03Height - positionYs[i + 1] * contentHeight / maxStep;
            canvas.drawLine(positionXs[i], start, positionXs[i + 1], end, brokenLinePaint);
            canvas.drawCircle(positionXs[i], start, 5, brokenLinePaint);
        }
        // 最后一个点变大
        canvas.drawCircle(positionXs[6], line03Height - positionYs[6] * contentHeight / maxStep, 10, brokenLinePaint);
    }

    /**
     * 绘制弹出步数，默认在屏幕外
     *
     * @param canvas
     */
    private void drawStepsNumber(Canvas canvas, String stepsNumber) {
        if (clickPosition == -1) {
            stepsNumberX = -100;
            stepsNumberY = -100;
        }
        stepsNumberPaint = new Paint();
        stepsNumberPaint.setStyle(Paint.Style.FILL);
        stepsNumberPaint.setAntiAlias(true); //去锯齿
        stepsNumberPaint.setTextSize(bottomTextSize);
        stepsNumberPaint.setColor(Color.parseColor("#55D5D8"));
        Rect stepsNumberPaintRect = new Rect();
        stepsNumberPaint.getTextBounds(stepsNumber, 0, stepsNumber.length(), stepsNumberPaintRect);
        canvas.drawText(stepsNumber, (int) (stepsNumberX - stepsNumberPaintRect.width() / 2), stepsNumberY, stepsNumberPaint);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        /**
         * 确定哪一天的步数被点击了
         */
        for (int i = 6; i > -1; i--) {
            if (positionXs[i] - pedometerViewWidth / 14 < event.getX()) {
                clickPosition = i;
                break;
            }
        }
//        Toast.makeText(context , event.getX() + " | " + event.getY() + " | " + clickPosition  ,Toast.LENGTH_SHORT).show();
        /**
         * 修改弹出步数文字的绘制位置和大小
         */
        stepsNumberX = positionXs[clickPosition];
        stepsNumberY = line03Height - positionYs[clickPosition] * contentHeight / maxStep;
//        stepsNubmer = 9999 - clickPosition * 100 + "";
        startValueAnimator();
        return super.onTouchEvent(event);
    }

    /**
     * 开启绘制上升步数动画
     */
    private void startValueAnimator() {
        ValueAnimator valueAnimator = ValueAnimator.ofInt(1, 2);
        valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                stepsNumberY = stepsNumberY > line01Height + spaceV ? stepsNumberY -= 20 : line01Height + spaceV;
                invalidateView();
            }
        });
        valueAnimator.setDuration(600);
        valueAnimator.start();
    }

    /**
     * 重绘
     */
    private void invalidateView() {
        if (Looper.getMainLooper() == Looper.myLooper()) {
            invalidate();
        } else {
            postInvalidate();
        }
    }

    public void setPositionYs(float[] positionYs , String time) {
        this.positionYs = positionYs;
        float max = 20000f;
        for (int i = 0; i < positionYs.length; i++) {
            max = max > positionYs[i] ? max : positionYs[i];
        }
        if (max > 20000) {
            int a = ((int) max) / 10000;
            int b = ((int) max) % 10000;
            if (b > 0) {
                a++;
            }
            maxStep = a * 10000;
        }
        todayStepsNubmer = ((int) positionYs[6]) + "";
        headerPaint.getTextBounds(todayStepsNubmer, 0, todayStepsNubmer.length(), headerPaintRect);
        maxTime = DateTool.turnString2Date(time).getTime() ;
        setMeasuredDimension(pedometerViewWidth, bottomPaintHeight + 2 * spaceV);
        invalidateView();
    }
}
