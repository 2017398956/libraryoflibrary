package com.nfl.libraryoflibrary.view;

import android.animation.ValueAnimator;
import android.app.Activity;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.DashPathEffect;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PathEffect;
import android.graphics.Rect;
import android.os.Looper;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;

import com.nfl.libraryoflibrary.R;
import com.nfl.libraryoflibrary.utils.ConvertTool;
import com.nfl.libraryoflibrary.utils.LogTool;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by fuli.niu on 2017/9/25.
 */

public class LineChart extends View {

    private Context context;
    private String title;
    private String titleDescription; // 标题后展示的附加信息
    private String unit;// 计量单位
    private Paint headerPaint;// 头部画笔
    private Paint titleDescriptionPaint;// 标题附加信息画笔
    private Paint middleTextPaint; // "1w"的画笔
    private Rect headerPaintRect;// 用于绘制头部文字区域
    private int headerPaintHeight;// 头部的高度
    private Paint bottomPaint;// x 轴画笔
    private Rect bottomPaintRect;// x 轴区域
    private int bottomPaintHeight;// x 轴描述文字的高度
    private Paint brokenLinePaint;// 折线图画笔
    private Paint popupPaint;// 弹出文字画笔
    private float popupX;// 弹出文字x坐标
    private float popupY;// 弹出文字y坐标
    private List<String> xAxis = new ArrayList<>();// x坐标轴文字
    private List<Float> yAxis = new ArrayList<>();// y 上的值
    private List<Float> xValues = new ArrayList<>();// 用于记住每个日期的x坐标
    private float xAxisGap; // 两个 x 坐标的间隔
    private List<Float> yValues = new ArrayList<>();// y 坐标的值和 xValues 的个数一致
    private int clickPosition;// 哪个日期被点击了
    private int pedometerViewWidth;// 画布宽度
    private int pedometerViewHeight;// 画布高度
    private float headerTextSize;// 头部字体大小
    private float bottomTextSize;// 底部日期字体大小
    private final int spaceV = 30; // 垂直方向的间隔
    private int spaceH = 20;// 图表左右的 padding , 单位 dp
    private int line01Height;// 第一条线的高度
    private int line02Height;// 第二条线的高度
    private int line03Height; // 第三条线的高度
    private int contentHeight = 400; // 从第一条线到第三条线的高度,默认显示20000步
    private int baseMount = 1; //  中间虚线的基准量
    private float maxYValue = 2 * baseMount;
    private OnCustomItemClickListener onCustomItemClickListener;

    public LineChart(Context context) {
        this(context, null);
    }

    public LineChart(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public LineChart(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.context = context;
        DisplayMetrics dm = new DisplayMetrics();
        ((Activity) context).getWindowManager().getDefaultDisplay().getMetrics(dm);
        contentHeight = (int) (contentHeight * (dm.density / 3.5));
        clickPosition = -1;
        headerTextSize = ConvertTool.sp2px(26);
        bottomTextSize = ConvertTool.sp2px(12);
        spaceH = ConvertTool.dp2px(spaceH);
        titleDescription = " ";

        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.line_chart);
        title = typedArray.getString(R.styleable.line_chart_title);
        if (TextUtils.isEmpty(title)) {
            title = "标题";
        }
        unit = typedArray.getString(R.styleable.line_chart_unit);
        if (TextUtils.isEmpty(unit)) {
            unit = " ";
        }
        baseMount = typedArray.getInt(R.styleable.line_chart_base_mount, 1);

        // 先准备画笔，得到view的高度
        drawHeader();
        drawBottom();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (xAxis.size() > yAxis.size()) {
            for (int i = yAxis.size(); i < xAxis.size(); i++) {
                yAxis.add(0f);
            }
        }
        yValues.clear();
        yValues.addAll(yAxis);
        initXValues();
        drawHeader(canvas);
        drawBottom(canvas, clickPosition);
        drawBrokenLineGraph(canvas);
        drawPopupString(canvas, clickPosition > -1 && clickPosition < yAxis.size() ? yAxis.get(clickPosition) + "" : "");
    }

    private void initXValues() {
        if (null == xAxis || xAxis.size() == 0) {
            return;
        }
        xValues.clear();
        xAxisGap = ((float) pedometerViewWidth - 2 * spaceH) / (xAxis.size() - 1);
        for (int i = 0; i < xAxis.size(); i++) {
            xValues.add(i * xAxisGap + spaceH);
        }
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
        headerPaint.getTextBounds(title, 0, title.length(), headerPaintRect);
        line01Height = headerPaintRect.height() + 2 * spaceV;
        line03Height = line01Height + contentHeight;
        titleDescriptionPaint = new Paint();
        titleDescriptionPaint.setStyle(Paint.Style.STROKE);
        titleDescriptionPaint.setAntiAlias(true); //去锯齿
        titleDescriptionPaint.setTextSize(headerTextSize);
        titleDescriptionPaint.setColor(Color.WHITE);

        middleTextPaint = new Paint();
        middleTextPaint.setStyle(Paint.Style.STROKE);
        middleTextPaint.setAntiAlias(true); //去锯齿
        middleTextPaint.setTextSize(bottomTextSize);
        middleTextPaint.setColor(Color.parseColor("#55D5D8"));
    }

    private void drawHeader(Canvas canvas) {
        line02Height = getLine02Height();
        headerPaint.setStyle(Paint.Style.STROKE);
        headerPaint.setAntiAlias(true); //去锯齿
        headerPaint.setTextSize(headerTextSize);
        headerPaint.setColor(Color.WHITE);
        canvas.drawText(title, spaceH, line01Height - spaceV, headerPaint);
        headerPaint.getTextBounds(titleDescription, 0, titleDescription.length(), headerPaintRect);
        titleDescriptionPaint.getTextBounds(titleDescription, 0, titleDescription.length(), headerPaintRect);
        canvas.drawText(titleDescription, pedometerViewWidth - headerPaintRect.width() - spaceH,
                line01Height - spaceV, titleDescriptionPaint);

        canvas.drawLine(spaceH, line01Height, pedometerViewWidth - spaceH, line01Height, headerPaint);// 画第一条线
        // 下面为画虚线和“1w”

        headerPaint.setTextSize(bottomTextSize);
        headerPaint.setColor(Color.parseColor("#55D5D8"));

        Rect rect = new Rect();
        middleTextPaint.getTextBounds(baseMount + unit, 0, (baseMount + unit).length(), rect);
        canvas.drawText(baseMount + unit, pedometerViewWidth - spaceH - rect.width(), line02Height + rect.height() / 2, middleTextPaint);

        canvas.drawLine(spaceH, line03Height, pedometerViewWidth - spaceH, line03Height, headerPaint);// 画的第三条线

        Path path = new Path();
        path.moveTo(spaceH, line02Height);
        path.lineTo(pedometerViewWidth - spaceH - rect.width(), line02Height);
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
        bottomPaint.getTextBounds("11", 0, 1, bottomPaintRect);
        bottomPaintHeight = bottomPaintRect.height() + line03Height + spaceV;
    }

    /**
     * 绘制底部日期
     *
     * @param canvas
     * @param clickPosition 根据位置更改日期的颜色，默认为-1（在onDraw（）中），不需要更改日期的颜色，
     */
    private void drawBottom(Canvas canvas, int clickPosition) {
        if (null == xAxis || xAxis.size() == 0) {
            return;
        }
        Rect rectTemp = new Rect();
        for (int i = 0; i < xAxis.size(); i++) {
            bottomPaint.setColor(clickPosition == i ? Color.WHITE : Color.parseColor("#55D5D8"));
            bottomPaint.getTextBounds(xAxis.get(i) , 0, xAxis.get(i).length() , rectTemp);
            canvas.drawText(xAxis.get(i), xValues.get(i) - rectTemp.width() / 2, bottomPaintHeight - spaceV / 2, bottomPaint);
        }
    }

    /**
     * 绘制折线图
     *
     * @param canvas
     */
    private void drawBrokenLineGraph(Canvas canvas) {
        if (null == xAxis || xAxis.size() == 0) {
            return;
        }
        brokenLinePaint = new Paint();
        brokenLinePaint.setStyle(Paint.Style.FILL);
        brokenLinePaint.setAntiAlias(true);
        brokenLinePaint.setColor(Color.WHITE);
        brokenLinePaint.setStrokeWidth(2);
        for (int i = 0; i < xAxis.size() - 1; i++) {
            float start = line03Height - yValues.get(i) * contentHeight / maxYValue;
            float end = line03Height - yValues.get(i + 1) * contentHeight / maxYValue;
            canvas.drawLine(xValues.get(i), start, xValues.get(i + 1), end, brokenLinePaint);
            canvas.drawCircle(xValues.get(i), start, 5, brokenLinePaint);
        }
        // 最后一个点变大
        canvas.drawCircle(xValues.get(xValues.size() - 1), line03Height - yValues.get(xValues.size() - 1) * contentHeight / maxYValue, 10, brokenLinePaint);
    }

    /**
     * 绘制弹出内容，默认在屏幕外
     *
     * @param canvas
     */
    private void drawPopupString(Canvas canvas, String popupString) {
        if (clickPosition == -1) {
            popupX = -100;
            popupY = -100;
        }
        popupPaint = new Paint();
        popupPaint.setStyle(Paint.Style.FILL);
        popupPaint.setAntiAlias(true); //去锯齿
        popupPaint.setTextSize(bottomTextSize);
        popupPaint.setColor(Color.parseColor("#55D5D8"));
        Rect popupPaintRect = new Rect();
        popupPaint.getTextBounds(popupString, 0, popupString.length(), popupPaintRect);
        canvas.drawText(popupString, (int) (popupX - popupPaintRect.width() / 2), popupY, popupPaint);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (null == xAxis || xAxis.size() == 0) {
            return super.onTouchEvent(event);
        }
        /**
         * 确定哪一个 x 轴位置被点击了
         */
        for (int i = xAxis.size() - 1; i > -1; i--) {
            if (xValues.get(i) - xAxisGap / 2 < event.getX()) {
                clickPosition = i;
                if (null != onCustomItemClickListener) {
                    onCustomItemClickListener.onCustomItemClick(clickPosition);
                }
                break;
            }
        }
        /**
         * 修改弹出文字的绘制位置和大小
         */
        if (clickPosition < xValues.size() && clickPosition > -1) {
            popupX = xValues.get(clickPosition);
            popupY = line03Height - yValues.get(clickPosition) * contentHeight / maxYValue;
            startValueAnimator();
        }
        return super.onTouchEvent(event);
    }

    /**
     * 开启绘制上升动画
     */
    private void startValueAnimator() {
        ValueAnimator valueAnimator = ValueAnimator.ofInt(1, 2);
        valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                popupY = popupY > line01Height + spaceV ? popupY -= 20 : line01Height + spaceV;
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

    public interface OnCustomItemClickListener {
        void onCustomItemClick(int position);
    }

    public void setOnCustomItemClickListener(OnCustomItemClickListener onCustomItemClickListener) {
        this.onCustomItemClickListener = onCustomItemClickListener;
    }

    private int getLine02Height() {
        if (yValues.size() == 0) {

        } else {
            maxYValue = yValues.get(0);
            if (null != yValues && yValues.size() > 0) {
                for (float temp : yValues) {
                    if (temp > maxYValue) {
                        maxYValue = temp;
                    }
                }
                maxYValue = ((int) maxYValue / 10 + 1) * 10;
            }
            baseMount = (int) maxYValue / 2;
        }
        return (int) (line03Height - baseMount * contentHeight / maxYValue);
    }

    public void setTitle(String title) {
        this.title = title;
        invalidateView();
    }

    public void setUnit(String unit) {
        this.unit = unit;
        invalidateView();
    }

    public void setBaseMount(int baseMount) {
        this.baseMount = baseMount;
        invalidateView();
    }

    public void setTitleDescription(String titleDescription) {
        this.titleDescription = titleDescription;
        invalidateView();
    }

    public void setXAxis(List<String> xAxis) {
        this.xAxis.clear();
        this.xAxis.addAll(xAxis);
        invalidateView();
    }

    public void setYAxis(List<Float> yAxis) {
        this.yAxis.clear();
        this.yAxis.addAll(yAxis);
        invalidateView();
    }

    public void onlyDrawHeader(final boolean drawHeader) {
        post(new Runnable() {
            @Override
            public void run() {
                ViewGroup.LayoutParams layoutParams = getLayoutParams();
                LogTool.i("layoutParams is null ? " + (null == layoutParams));
                if (null != layoutParams) {
                    if (drawHeader) {
                        layoutParams.height = line01Height;
                    } else {
                        layoutParams.height = pedometerViewHeight;
                    }
                    setLayoutParams(layoutParams);
                    invalidateView();
                }
            }
        });
    }
}
