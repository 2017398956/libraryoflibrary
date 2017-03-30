package com.nfl.libraryoflibrary.view;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Rect;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.View;

import com.nfl.libraryoflibrary.constant.ApplicationContext;
import com.nfl.libraryoflibrary.utils.ConvertTool;
import com.nfl.libraryoflibrary.utils.DateTool;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by fuli.niu on 2017/2/16.
 * 水印背景图
 */

public class WaterMarkView extends View {

    private Canvas waterMarkCanvas;// 现将水印绘制在独立的画布上
    private Bitmap waterMarkBitmapTemp;// 将水印形成图片 , 为了方便旋转其长宽应该相等
    private Bitmap waterMarkBitmap;// 水印剪切后的图片
    private double waterMarkBitmapWidth;// waterMarkBitmap 的宽
    private double waterMarkBitmapHeight;// waterMarkBitmap 的高
    private Path path;
    private Paint paint;
    private int waterMarkAlpha = 100;// 水印的透明度
    private int waterMarkColor = Color.argb(waterMarkAlpha, 220, 220, 220);// 水印的颜色
    private float waterMarkTextSize = ConvertTool.sp2px(24);// 水印字体的大小
    private Rect rect;
    private List<String> strs;
    private int maxTextLenght;// 水印的最大长度（即：最长行的长度）
    private int textHeight;// 没行字体的高度（这里所有行都一样）
    private int waterMarkHeight; // 每条水印的高度
    private List<Integer> textLenghts;
    private int screenWidth, screenHeight;
    private int gap = ConvertTool.dp2px(12);// 每条水印间每行的间距（一条水印可能有多行）
    private int waterMarkSpace = 150;// 水印间的间隔，设置好间隔后计算出水印的条数，若屏幕还有多余空间自动增大水印间隔 , 由于水印是旋转的2个水印间会有段距离
    private int waterMarkNumber;// 水印的总条数，应该根据屏幕来自动生成不应该设置
    private float startX = 0f, startY = 0f;// 第一条水印 waterMarkBitmap 初始y坐标 , 由于水印是旋转的，waterMarkBitmap一定会离顶部有段距离
    private int degrees = -20; // 将角度转换为弧度,默认水印倾斜20度 , 顺时针为正，逆时针为负; 旋转角度应该通过xml文件配置
    private double sinDegrees, cosDegrees;
    private double waterMarkerR;// 水印外接圆半径

    public WaterMarkView(Context context) {
        this(context, null);
    }

    public WaterMarkView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public WaterMarkView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        sinDegrees = Math.sin(Math.toRadians(Math.abs(degrees)));
        cosDegrees = Math.cos(Math.toRadians(Math.abs(degrees)));
        strs = new ArrayList<>();
        strs.add("快钱");
        strs.add(TextUtils.isEmpty(ApplicationContext.USERNAMECH)|| "null".equals(ApplicationContext.USERNAMECH) ? ApplicationContext.USERNAME :
                ApplicationContext.USERNAMECH + "(" + ApplicationContext.USERNAME + ")");
        strs.add(DateTool.getDateString(new Date()));
        this.paint = new Paint();
//        this.paint.setAlpha(waterMarkAlpha);
        this.paint.setARGB(waterMarkAlpha, 220, 220, 220);
//        setARGB
        this.paint.setColor(waterMarkColor);
        this.paint.setTextSize(waterMarkTextSize);
        this.paint.setAntiAlias(true);
        rect = new Rect();
        textLenghts = new ArrayList<>();

        if (null != strs) {
            if (strs.size() == 0) {
                waterMarkBitmapWidth = 0;
                waterMarkBitmapHeight = 0;
                waterMarkHeight = 0;
                textHeight = 0;
            } else {
                for (String str : strs) {
                    this.paint.getTextBounds(str, 0, str.length(), rect);
                    textLenghts.add(rect.width());
                    if (rect.width() > maxTextLenght) {
                        maxTextLenght = rect.width();
                    }
                }
                textHeight = rect.height();
            }
        }
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        screenWidth = getMeasuredWidth();
        screenHeight = getMeasuredHeight();
        if (waterMarkBitmapHeight != 0 && waterMarkBitmapWidth != 0) {
            waterMarkNumber = screenHeight / ((int) waterMarkBitmapHeight + waterMarkSpace) + 1;
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        setBackgroundColor(Color.TRANSPARENT);

//        for (int i = 0; i < waterMarkNumber; i++) {
//            canvas.drawBitmap(waterMarkBitmap, x, startY + i * ((float) waterMarkBitmapHeight + waterMarkSpace), paint);
//        }

        // 假设第一行水印的一端在x轴上，计算出y = kx + b的k 和 b ；
        startX = (float) ((screenWidth - textLenghts.get(0) * cosDegrees) / 2);
        startY = (float) (textLenghts.get(0) * sinDegrees);
        float k = startY / (2 * startX - screenWidth);
        float b = k * (startX - screenWidth);
        // 设置2条水印间的距离
        waterMarkSpace = ConvertTool.dp2px(150);
        for (int n = 0; n < 5; n++) {
            for (int i = 0; i < strs.size(); i++) {
                startX = (float) ((screenWidth - textLenghts.get(i) * cosDegrees) / 2);
                double bTemp = b + i * (textHeight + gap) / cosDegrees + n * waterMarkSpace;
                startY = (float) (k * startX + bTemp);
                path = new Path();
                path.moveTo(startX, startY);
                path.lineTo(screenWidth - startX, (float) (startY - textLenghts.get(i) * sinDegrees));
                canvas.drawTextOnPath(strs.get(i), path, 0, 0, paint);
            }
        }
    }

    public List<String> getStrs() {
        return strs;
    }

    public void setStrs(List<String> strs) {
        this.strs = strs;
    }
}
